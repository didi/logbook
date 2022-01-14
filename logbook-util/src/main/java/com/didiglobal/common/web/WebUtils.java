package com.didiglobal.common.web;


import com.didiglobal.common.web.wrapper.ContentCachingRequestWrapper;
import com.didiglobal.common.web.wrapper.ContentCachingResponseWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

public class WebUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    /**
     * Standard Servlet 2.3+ spec request attributes for include URI and paths.
     * <p>If included via a RequestDispatcher, the current resource will see the
     * originating request. Its own URI and paths are exposed as request attributes.
     */
    public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
    public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
    public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
    public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
    public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

    /**
     * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
     * <p>If forwarded to via a RequestDispatcher, the current resource will see its
     * own URI and paths. The originating URI and paths are exposed as request attributes.
     */
    public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
    public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
    public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
    public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
    public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    public static final String DEFAULT_UNPRINTABLE_REQUEST_BODY = "** unprintable request body **";

    public static final String DEFAULT_CANNOT_RETRIEVE_REQUEST_BODY =
            "** cannot retrieve request body **";

    public static final String DEFAULT_RETRIEVE_REQUEST_BODY_FAILED =
            "** retrieve request body failed **";

    public static final String DEFAULT_UNPRINTABLE_RESPONSE_BODY = "** unprintable response body **";

    public static final String DEFAULT_CANNOT_RETRIEVE_RESPONSE_BODY =
            "** cannot retrieve response body **";

    public static final String DEFAULT_RETRIEVE_RESPONSE_BODY_FAILED =
            "** retrieve response body failed **";


    public static final Set<String> PRINTABLE_CONTENT_TYPE_WILDCARD = ImmutableSet
            .<String>builder()
            .add("text/")
            .add("application/json")
            .add("application/x-www-form-urlencoded")
            .add("application/text")
            .add("application/xml")
            .add("application/html")
            .add("application/xhtml")
            .add("application/javascript")
            .build();

    public static final Map<String, Boolean> PRINTABLE_CONTENT_TYPE_MAP = ImmutableMap
            .<String, Boolean>builder()
            .put("application/json", true)
            .put("application/x-www-form-urlencoded", true)
            .put("application/text", true)
            .put("application/xml", true)
            .put("application/html", true)
            .put("application/xhtml", true)
            .put("application/javascript", true)
            .build();

    public static Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = Collections.EMPTY_MAP;
        Enumeration<String> headerNames = request.getHeaderNames();
        if (null == headerNames || !headerNames.hasMoreElements()) {
            return headers;
        }
        while (headerNames.hasMoreElements()) {
            if (Collections.EMPTY_MAP == headers) {
                headers = Maps.newHashMap();
            }
            String headerName = headerNames.nextElement();
            if (headers.containsKey(StringUtils.lowerCase(headerName))) {
                continue;
            }
            headers.put(StringUtils.lowerCase(headerName),
                    request.getHeader(headerName));
        }
        return headers;
    }

    public static Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Map<String, String> headers = Collections.EMPTY_MAP;
        Collection<String> headerNames = response.getHeaderNames();
        if (null == headerNames || headerNames.isEmpty()) {
            return headers;
        }
        for (String headerName : headerNames) {

            if (Collections.EMPTY_MAP == headers) {
                headers = Maps.newHashMap();
            }
            if (headers.containsKey(StringUtils.lowerCase(headerName))) {
                continue;
            }
            headers.put(StringUtils.lowerCase(headerName),
                    response.getHeader(headerName));

        }
        return headers;
    }

    private static boolean isTrue(Boolean b) {
        return null == b ? false : b;
    }

    public static boolean isPrintableContentType(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return true;
        }
        if (isTrue(PRINTABLE_CONTENT_TYPE_MAP.get(contentType))) {
            return true;
        }
        for (String p : PRINTABLE_CONTENT_TYPE_WILDCARD) {
            if (StringUtils.startsWithIgnoreCase(contentType, p)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRequestHasBody(HttpServletRequest request) {
        String method = request.getMethod();
        return !(StringUtils.equalsIgnoreCase("GET", method)
                        || StringUtils.equalsIgnoreCase("HEAD", method)
                        || StringUtils.equalsIgnoreCase("OPTIONS", method)
                        || StringUtils.equalsIgnoreCase("DELETE", method));
    }

    public static String getRequestBody(HttpServletRequest request) {
        if (!isRequestHasBody(request)) {
            return null;
        }
        if (!isPrintableContentType(request.getContentType())) {
            return DEFAULT_UNPRINTABLE_REQUEST_BODY;
        }
        try {
            return requestBodyToString(request);
        } catch (Exception e) {
            LOGGER.warn("caught error when retrieve request body: ", e);
            return DEFAULT_RETRIEVE_REQUEST_BODY_FAILED + e.getMessage();
        }
    }

    protected static String requestBodyToString(HttpServletRequest request)
            throws IOException {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            return DEFAULT_CANNOT_RETRIEVE_REQUEST_BODY;
        }
        ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
        return new String(wrappedRequest.getContentAsByteArray(),
                wrappedRequest.getCharacterEncoding());
    }

    protected static String responseBodyToString(HttpServletResponse response) {
        if (!(response instanceof ContentCachingResponseWrapper)) {
            return DEFAULT_CANNOT_RETRIEVE_RESPONSE_BODY;
        }
        ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) response;
        return StringUtils.toEncodedString(wrappedResponse.getContentAsByteArray(),
                Charset.forName(wrappedResponse.getCharacterEncoding()));
    }

    public static String getResponseBody(HttpServletResponse response) {
        if (!isPrintableContentType(response.getContentType())) {
            return DEFAULT_UNPRINTABLE_RESPONSE_BODY;
        }
        try {
            return responseBodyToString(response);
        } catch (Exception e) {
            LOGGER.warn("caught error when retrieve response body: ", e);
            return DEFAULT_RETRIEVE_RESPONSE_BODY_FAILED + e.getMessage();
        }
    }

    public static String getRequestContentType(HttpServletRequest request) {
        return request.getContentType();
    }

    public static String getResponseContentType(HttpServletResponse response) {
        return response.getContentType();
    }

    public static String getPathWithinApplication(HttpServletRequest request) {
        String contextPath = getContextPath(request);
        String requestUri = getRequestUri(request);
        if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
            // Normal case: URI contains context path.
            String path = requestUri.substring(contextPath.length());
            return (StringUtils.isNotBlank(path) ? path : "/");
        } else {
            // Special case: rather unusual.
            return requestUri;
        }
    }

    public static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return normalize(decodeAndCleanUriString(request, uri));
    }

    private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        uri = decodeRequestString(request, uri);
        int semicolonIndex = uri.indexOf(';');
        return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
    }

    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    public static String getContextPath(HttpServletRequest request) {
        String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        contextPath = normalize(decodeRequestString(request, contextPath));
        if ("/".equals(contextPath)) {
            // the normalize method will return a "/" and includes on Jetty, will also be a "/".
            contextPath = "";
        }
        return contextPath;
    }

    public static String decodeRequestString(HttpServletRequest request, String source) {
        String enc = determineEncoding(request);
        try {
            return URLDecoder.decode(source, enc);
        } catch (UnsupportedEncodingException ex) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Could not decode request string [" + source + "] with encoding '" + enc +
                        "': falling back to platform default encoding; exception message: " + ex
                        .getMessage());
            }
            return URLDecoder.decode(source);
        }
    }

    protected static String determineEncoding(HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = DEFAULT_CHARACTER_ENCODING;
        }
        return enc;
    }

    public static String normalize(String path) {
        return normalize(path, true);
    }

    private static String normalize(String path, boolean replaceBackSlash) {

        if (path == null) {
            return null;
        }

        // Create a place for the normalized path
        String normalized = path;

        if (replaceBackSlash && normalized.indexOf('\\') >= 0) {
            normalized = normalized.replace('\\', '/');
        }

        if (normalized.equals("/.")) {
            return "/";
        }

        // Add a leading "/" if necessary
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0) {
                break;
            }
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0) {
                break;
            }
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0) {
                break;
            }
            if (index == 0) {
                return (null);  // Trying to go outside our context
            }
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);
    }

    /**
     * for internal tools usage
     */

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
                                                 boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        } else {
            StringTokenizer st = new StringTokenizer(str, delimiters);
            ArrayList tokens = new ArrayList();

            while (true) {
                String token;
                do {
                    if (!st.hasMoreTokens()) {
                        return toStringArray(tokens);
                    }

                    token = st.nextToken();
                    if (trimTokens) {
                        token = token.trim();
                    }
                } while (ignoreEmptyTokens && token.length() <= 0);

                tokens.add(token);
            }
        }
    }

    public static String[] toStringArray(Collection collection) {
        return collection == null ? null : (String[]) ((String[]) collection
                .toArray(new String[collection.size()]));
    }


    public static String[] split(String line) {
        return split(line, ',');
    }

    public static String[] split(String line, char delimiter) {
        return split(line, delimiter, '"');
    }

    public static String[] split(String line, char delimiter, char quoteChar) {
        return split(line, delimiter, quoteChar, quoteChar);
    }

    public static String[] split(String line, char delimiter, char beginQuoteChar,
                                 char endQuoteChar) {
        return split(line, delimiter, beginQuoteChar, endQuoteChar, false, true);
    }

    public static String[] split(String aline, char delimiter, char beginquotechar, char endquotechar,
                                 boolean retainquotes, boolean trimtokens) {
        String line = clean(aline);
        if (line == null) {
            return null;
        } else {
            List<String> tokens = new ArrayList();
            StringBuilder sb = new StringBuilder();
            boolean inQuotes = false;

            for (int i = 0; i < line.length(); ++i) {
                char c = line.charAt(i);
                if (c == beginquotechar) {
                    if (inQuotes && line.length() > i + 1 && line.charAt(i + 1) == beginquotechar) {
                        sb.append(line.charAt(i + 1));
                        ++i;
                    } else {
                        inQuotes = !inQuotes;
                        if (retainquotes) {
                            sb.append(c);
                        }
                    }
                } else if (c == endquotechar) {
                    inQuotes = !inQuotes;
                    if (retainquotes) {
                        sb.append(c);
                    }
                } else if (c == delimiter && !inQuotes) {
                    String s = sb.toString();
                    if (trimtokens) {
                        s = s.trim();
                    }

                    tokens.add(s);
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }
            }

            String s = sb.toString();
            if (trimtokens) {
                s = s.trim();
            }

            tokens.add(s);
            return (String[]) tokens.toArray(new String[tokens.size()]);
        }
    }

    public static String clean(String in) {
        String out = in;
        if (in != null) {
            out = in.trim();
            if (out.equals("")) {
                out = null;
            }
        }

        return out;
    }

}