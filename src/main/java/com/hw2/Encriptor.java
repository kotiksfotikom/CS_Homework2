package com.hw2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.io.*;
import java.nio.ByteBuffer;

public class Encriptor {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = "1234567890123456".getBytes();

    public static byte[] encrypt(Message message) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(message);
        out.flush();

        byte[] data = bos.toByteArray();

        int crc = CRC16.calculate(data);

        ByteBuffer buffer = ByteBuffer.allocate(data.length + 2);
        buffer.put(data);
        buffer.putShort((short) crc);
        byte[] dataWithCrc = buffer.array();

        Key key = new SecretKeySpec(keyValue, ALGO);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(dataWithCrc);
    }
}