package com.gangdan.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NioUtils {

    private static Logger logger = LoggerFactory.getLogger(NioUtils.class);

    public static byte[] zip(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                gos.write(bytes);
                gos.close();
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static byte[] unzip(byte[] bytes) {
        try {
            GZIPInputStream       gis  = new GZIPInputStream(new ByteArrayInputStream(bytes));
            ByteArrayOutputStream bos  = new ByteArrayOutputStream();
            byte[]                buff = new byte[512];
            int                   read = gis.read(buff);
            while (read > 0) {
                bos.write(buff, 0, read);
                read = gis.read(buff);
            }
            gis.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
