package com.hw2;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.io.*;
import java.nio.ByteBuffer;

public class Decriptor {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = "1234567890123456".getBytes();

    public static Message decrypt(byte[] encrypted) throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedWithCrc = c.doFinal(encrypted);

        if (decryptedWithCrc.length < 2) {
            throw new Exception("Invalid data: too short");
        }

        ByteBuffer buffer = ByteBuffer.wrap(decryptedWithCrc);
        byte[] data = new byte[decryptedWithCrc.length - 2];
        buffer.get(data);
        short receivedCrc = buffer.getShort();

        int calculatedCrc = CRC16.calculate(data);
        if (calculatedCrc != (receivedCrc & 0xFFFF)) {
            throw new Exception("CRC16 mismatch: data corrupted. Expected: " +
                    (receivedCrc & 0xFFFF) + ", got: " + calculatedCrc);
        }

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (Message) in.readObject();
    }
}
