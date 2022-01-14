package com.didiglobal.common.web.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 用在response cache包装里
 */

abstract class UpdateMessageDigestInputStream extends InputStream {

    /**
     * Update the message digest with the rest of the bytes in this stream.
     * <p>Using this method is more optimized since it avoids creating new
     * byte arrays for each call.
     */
    public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
        int data;
        while ((data = read()) != -1) {
            messageDigest.update((byte) data);
        }
    }

    /**
     * Update the message digest with the next len bytes in this stream.
     * <p>Using this method is more optimized since it avoids creating new
     * byte arrays for each call.
     */
    public void updateMessageDigest(MessageDigest messageDigest, int len) throws IOException {
        int data;
        int bytesRead = 0;
        while (bytesRead < len && (data = read()) != -1) {
            messageDigest.update((byte) data);
            bytesRead++;
        }
    }

}