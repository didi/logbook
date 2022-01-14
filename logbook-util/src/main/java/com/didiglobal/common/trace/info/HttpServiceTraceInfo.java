package com.didiglobal.common.trace.info;


import java.util.Map;

public class HttpServiceTraceInfo {

    String remoteAddress;

    String localAddress;

    long startTime;

    long finishTime;

    RequestTraceInfo request;

    ResponseTraceInfo response;

    HttpServiceTraceInfo() {
        this.request = new RequestTraceInfo();
        this.response = new ResponseTraceInfo();
    }

    public static HttpServiceTraceInfo makeDefault() {
        return new HttpServiceTraceInfo();
    }

    public String getMethod() {
        return request.method;
    }

    public String getUri() {
        return request.uri;
    }

    public String getQuery() {
        return request.query;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public Map<String, String> getRequestHeaders() {
        return request.headers;
    }

    public String getRequestBody() {
        return request.body;
    }

    public int getResponseStatus() {
        return response.status;
    }

    public Map<String, String> getResponseHeaders() {
        return response.headers;
    }

    public String getResponseBody() {
        return response.body;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public RequestTraceInfo getRequest() {
        return request;
    }

    public ResponseTraceInfo getResponse() {
        return response;
    }

    public HttpServiceTraceInfo setMethod(String method) {
        request.method = method;
        return this;
    }

    public HttpServiceTraceInfo setUri(String uri) {
        request.uri = uri;
        return this;
    }

    public HttpServiceTraceInfo setQuery(String query) {
        request.query = query;
        return this;
    }

    public HttpServiceTraceInfo setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public HttpServiceTraceInfo setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
        return this;
    }

    public HttpServiceTraceInfo setRequestHeaders(Map<String, String> requestHeaders) {
        request.headers = requestHeaders;
        return this;
    }

    public HttpServiceTraceInfo setRequestBody(String requestBody) {
        request.body = requestBody;
        return this;
    }

    public HttpServiceTraceInfo setResponseStatus(int responseStatus) {
        response.status = responseStatus;
        return this;
    }

    public HttpServiceTraceInfo setResponseHeaders(Map<String, String> responseHeaders) {
        response.headers = responseHeaders;
        return this;
    }

    public HttpServiceTraceInfo setResponseBody(String responseBody) {
        response.body = responseBody;
        return this;
    }

    public HttpServiceTraceInfo setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public HttpServiceTraceInfo setFinishTime(long finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpTraceInfo{");
        sb.append("remoteAddress='").append(remoteAddress).append('\'');
        sb.append(", localAddress='").append(localAddress).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", request=").append(request);
        sb.append(", response=").append(response);
        sb.append('}');
        return sb.toString();
    }

    public static class RequestTraceInfo {

        String method;
        String uri;
        String query;
        Map<String, String> headers;
        String body;

        public String getMethod() {
            return method;
        }

        public RequestTraceInfo setMethod(String method) {
            this.method = method;
            return this;
        }

        public String getUri() {
            return uri;
        }

        public RequestTraceInfo setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public String getQuery() {
            return query;
        }

        public RequestTraceInfo setQuery(String query) {
            this.query = query;
            return this;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public RequestTraceInfo setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public String getBody() {
            return body;
        }

        public RequestTraceInfo setBody(String body) {
            this.body = body;
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("RequestTraceInfo{");
            sb.append("method='").append(method).append('\'');
            sb.append(", uri='").append(uri).append('\'');
            sb.append(", query='").append(query).append('\'');
            sb.append(", headers=").append(headers);
            sb.append(", body='").append(body).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class ResponseTraceInfo {

        int status;
        Map<String, String> headers;
        String body;

        public int getStatus() {
            return status;
        }

        public ResponseTraceInfo setStatus(int status) {
            this.status = status;
            return this;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public ResponseTraceInfo setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public String getBody() {
            return body;
        }

        public ResponseTraceInfo setBody(String body) {
            this.body = body;
            return this;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ResponseTraceInfo{");
            sb.append("status=").append(status);
            sb.append(", headers=").append(headers);
            sb.append(", body='").append(body).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
