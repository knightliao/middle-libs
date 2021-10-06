package com.github.knightliao.middle.utils.trans;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liaoqiqi
 * @version 2014-8-21
 */
public class NetBytesSwitchUtil {

    private static final Logger logger = LoggerFactory
            .getLogger(NetBytesSwitchUtil.class);

    private static String STR_ENCODE = "GBK";

    public static byte[] IntToBytes(int x) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (x >> i * 8 & 0xFF);
        }

        return b;
    }

    public static byte[] LongToBytes(long x) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (int) (x >> i * 8 & 0xFF);
        }

        return b;
    }

    public static byte[] LongToLongBytes(long x) {
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (int) (x >> i * 8 & 0xFF);
        }

        return b;
    }

    public static int BytesToInt(byte[] x) {
        int iOutcome = 0;

        for (int i = 0; i < 4; i++) {
            byte bLoop = x[i];
            iOutcome += ((bLoop & 0xFF) << 8 * i);
        }

        return iOutcome;
    }

    public static long BytesToLong(byte[] x) {
        long iOutcome = 0L;

        for (int i = 0; i < 4; i++) {
            byte bLoop = x[i];
            iOutcome += ((bLoop & 0xFFL) << 8 * i);
        }

        return iOutcome;
    }

    public static byte[] StringToBytes(String input) {
        byte[] result = (byte[]) null;
        if (input != null) {
            try {
                result = input.getBytes(STR_ENCODE);
            } catch (UnsupportedEncodingException e) {
                logger.debug("StringToBytes", e);
            }
        }
        return result;
    }

    public static String bytesToString(byte[] input) {
        String result = null;
        if (input != null) {
            try {
                result = new String(input, STR_ENCODE).trim();
            } catch (Exception e) {
                throw new RuntimeException("This jdk does not support GBK");
            }
        }
        return result;
    }

    public static byte[] StringToBytes(String input, int length) {
        byte[] tmp = StringToBytes(input);
        byte[] bytes = new byte[length];
        System.arraycopy(tmp, 0, bytes, 0, tmp.length);
        return bytes;
    }

    public static long BytesToLong2(byte[] x) throws Exception {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(x.length);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.put(x);
            return buffer.getLong(0);
        } catch (Exception e) {
        }
        throw new Exception("exception in transfer byte to long...");
    }

}