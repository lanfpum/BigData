import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The world always makes way for the dreamer
 * Created by 努力常态化 on 2018/8/10 09:12.
 */

public class TestCompression {

    @Test
    public void runCompression() throws IOException {
        long start = System.currentTimeMillis();
        Class<DeflateCodec> deflateCodecClass = DeflateCodec.class;
        CompressionCodec compressionCodec = ReflectionUtils.newInstance(deflateCodecClass, new Configuration());
        FileOutputStream fileOutputStream = new FileOutputStream("D:/com/1.deflate");
        CompressionOutputStream outputStream = compressionCodec.createOutputStream(fileOutputStream);
        IOUtils.copyBytes(new FileInputStream("D:/com/a.txt"), outputStream, 1024);
        outputStream.close();
        System.out.println((System.currentTimeMillis() - start));
    }

    @Test
    public void runcom2() throws IOException {
        Class[] classes = {
                DeflateCodec.class,
                GzipCodec.class,
                BZip2Codec.class,
                Lz4Codec.class,
//                SnappyCodec.class
        };

        for (Class aClass : classes) {
//            zip(aClass);
            unZip(aClass);
        }

    }

    public void zip(Class clazz) throws IOException {
        long start = System.currentTimeMillis();
        CompressionCodec compressionCodec = (CompressionCodec) ReflectionUtils.newInstance(clazz, new Configuration());
        FileOutputStream fileOutputStream = new FileOutputStream("D:/com/1" + compressionCodec.getDefaultExtension());
        CompressionOutputStream outputStream = compressionCodec.createOutputStream(fileOutputStream);
        IOUtils.copyBytes(new FileInputStream("D:/com/a.txt"), outputStream, 1024);
        outputStream.close();
        System.out.println(clazz.getSimpleName() + " : " + (System.currentTimeMillis() - start));
    }

    public void unZip(Class clazz) throws IOException {
        long start = System.currentTimeMillis();
        CompressionCodec compressionCodec = (CompressionCodec) ReflectionUtils.newInstance(clazz, new Configuration());
        FileInputStream fileInputStream = new FileInputStream("D:/com/1" + compressionCodec.getDefaultExtension());
        CompressionInputStream compressionCodecInputStream = compressionCodec.createInputStream(fileInputStream);
        IOUtils.copyBytes(compressionCodecInputStream, new FileOutputStream("D:/com/uzip/1" + clazz.getSimpleName() + ".txt"), 1024);
        compressionCodecInputStream.close();
        System.out.println(clazz.getSimpleName() + " : " + (System.currentTimeMillis() - start));
    }
}

