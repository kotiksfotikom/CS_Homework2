package com.hw2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.io.*;


public class Encriptor
{
    private static final String ALGO = "AES";
    private static final byte[] keyValue = "1234567890123456".getBytes();

    public static byte[] encrypt(Message message) throws Exception

    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(message);
        out.flush();

        byte[] data = bos.toByteArray();
        Key key = new SecretKeySpec(keyValue, ALGO);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(data);
    }
}
