package com.hw2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.io.*;

public class Decriptor
{
    private static final String ALGO = "AES";
    private static final byte[] keyValue = "1234567890123456".getBytes();

    public static Message decrypt(byte[] encrypted) throws Exception

    {
        Key key = new SecretKeySpec(keyValue, ALGO);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] data = c.doFinal(encrypted);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (Message) in.readObject();
    }
}
