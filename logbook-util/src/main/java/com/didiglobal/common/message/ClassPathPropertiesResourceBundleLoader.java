package com.didiglobal.common.message;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathPropertiesResourceBundleLoader implements ResourceBundleLoader {

    private static final String MINUS = "-";

    private static final String DEFAULT_PREFIX = "message";

    private static final String DEFAULT_SUFFIX = ".properties";

    private static final String DEFAULT_CHARSET = "UTF-8";

    String propertyFilenamePrefix;

    String propertyFilenameSuffix;

    Charset charset;

    // constructors
    public ClassPathPropertiesResourceBundleLoader() {
        this(DEFAULT_PREFIX + DEFAULT_SUFFIX);
    }

    public ClassPathPropertiesResourceBundleLoader(String baseFilename) {
        this(baseFilename, DEFAULT_CHARSET);
    }

    public ClassPathPropertiesResourceBundleLoader(String baseFilename, String charset) {
        init(baseFilename, charset);
    }

    // initial
    private void init(String baseFilename, String charset) {
        if (null == baseFilename) {
            throw new IllegalArgumentException("resource bundle base filename cannot be null!");
        }
        if (!baseFilename.endsWith(DEFAULT_SUFFIX)) {
            throw new IllegalArgumentException("only support properties files!");
        }
        this.propertyFilenamePrefix = baseFilename.replace(DEFAULT_SUFFIX, "");
        this.propertyFilenameSuffix = DEFAULT_SUFFIX;
        this.charset = Charset.forName(charset);
    }

    Map<String/* filename */, PropertyResourceBundle> cacheMap = new ConcurrentHashMap<>();

    @Override
    public ResourceBundle loadResourceBundle(Locale locale) {
        String cacheKey = getCacheKey(locale);
        PropertyResourceBundle resourceBundle = cacheMap.get(cacheKey);
        if (resourceBundle != null) {
            return resourceBundle;
        }
        synchronized (this) {
            resourceBundle = cacheMap.get(cacheKey);
            if (resourceBundle != null) {
                return resourceBundle;
            }
            try {
                resourceBundle = loadResourceFromClassPath(cacheKey);
                cacheMap.put(cacheKey, resourceBundle);
            } catch (Throwable t) {
                String msg = String.format("caught error when load resource bundle: %s", cacheKey);
                throw new IllegalStateException(msg, t.getCause());
            }
        }
        return resourceBundle;
    }

    protected PropertyResourceBundle loadResourceFromClassPath(String cacheKey) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(cacheKey)) {
            Reader reader = new InputStreamReader(is, charset);
            return new PropertyResourceBundle(reader);
        } catch (Exception e) {
            return new PropertyResourceBundle(new ByteArrayInputStream("".getBytes()));
        }
    }

    // file format：${DEFAULT_PREFIX}-${Local.CHINESE}${DEFAULT_SUFFIX}
    // eg: message-zh_CN.properties
    protected String getCacheKey(Locale locale) {
        return getPrefix() + MINUS + locale.toString() + getSuffix();
    }

    protected String getPrefix() {
        if (null == propertyFilenamePrefix) {
            return DEFAULT_PREFIX;
        }
        return propertyFilenamePrefix;
    }

    protected String getSuffix() {
        if (null == propertyFilenameSuffix) {
            return DEFAULT_SUFFIX;
        }
        return propertyFilenameSuffix;
    }
}