package com.didiglobal.common.web.wrapper;

import com.didiglobal.common.web.WebUtils;
import com.google.common.primitives.Bytes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger         LOGGER = LoggerFactory.getLogger(ContentCachingRequestWrapper.class);
    private              BufferedReader reader;

    private byte[] requestBody = new byte[0];

    private boolean bufferFilled = false;

    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     */
    public ContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public byte[] getRequestBody() throws IOException {
        if (bufferFilled) {
            return Arrays.copyOf(requestBody, requestBody.length);
        }

        InputStream inputStream = super.getInputStream();

        byte[] buffer = new byte[1024]; // 1kb buffer

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            requestBody = Bytes.concat(this.requestBody,
                    Arrays.copyOfRange(buffer, 0, bytesRead));
        }

        bufferFilled = true;

        return requestBody;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ContentCachingInputStream(getRequestBody());
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader =
                    new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    public byte[] getContentAsByteArray() {
        try {
            byte[] raw = getRequestBody();
            return Arrays.copyOf(raw, requestBody.length);
        } catch (IOException e) {
            LOGGER.error("the io exception", e);
        }
        return Arrays.copyOf(requestBody, requestBody.length);
    }

    protected void handleContentOverflow(int contentCacheLimit) {
    }

    private static class ContentCachingInputStream extends ServletInputStream {

        private ByteArrayInputStream buffer;

        public ContentCachingInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }
    }
}
