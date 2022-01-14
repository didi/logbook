package com.didiglobal.common.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {

    // instance
    public static final MessageManager INSTANCE = custom()
            .defaultLocale(Locale.SIMPLIFIED_CHINESE)
            .resourceBundleLoader(new ClassPathPropertiesResourceBundleLoader())
            .build();

    // inner properties
    Locale defaultLocale;
    ResourceBundleLoader resourceBundleLoader;


    // constructor
    MessageManager(Locale defaultLocale,ResourceBundleLoader resourceBundleLoader) {
        this.defaultLocale = defaultLocale;
        this.resourceBundleLoader = resourceBundleLoader;
    }

    // getter
    public String getMessage(String key) {
        return getMessageWithLocale(key, defaultLocale);
    }

    public String getMessageWithLocale(String key, Locale locale) {
        ResourceBundle rb = resourceBundleLoader.loadResourceBundle(locale);
        if (null == rb) {
            return key;
        }
        if (!rb.containsKey(key)) {
            return key;
        }
        String message = rb.getString(key);
        if(message.length() == 0){
            return key;
        }
        return message;
    }

    public String getMessage(String key, String... params) {
        return getMessageWithLocale(key, defaultLocale, params);
    }

    public String getMessageWithLocale(String key, Locale locale, String... params) {
        ResourceBundle rb = resourceBundleLoader.loadResourceBundle(locale);
        if (null == rb) {
            return key;
        }
        if (!rb.containsKey(key)) {
            return key;
        }
        String message = rb.getString(key);
        if (message.length() == 0) {
            return key;
        }
        if (null == params || 0 == params.length) {
            return message;
        }
        return new MessageFormat(message, locale).format(params);
    }


    // builder
    public static Builder custom() {
        return new Builder();
    }

    public static class Builder {

        Locale defaultLocale;

        ResourceBundleLoader resourceBundleLoader;

        public Builder() {
        }

        public Builder(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
        }

        public Builder defaultLocale(Locale defaultLocale) {
            this.defaultLocale = defaultLocale;
            return this;
        }

        public Builder resourceBundleLoader(ResourceBundleLoader resourceBundleLoader) {
            this.resourceBundleLoader = resourceBundleLoader;
            return this;
        }

        public MessageManager build() {
            return new MessageManager(defaultLocale, resourceBundleLoader);
        }
    }
}