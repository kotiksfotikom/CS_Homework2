import com.hw2.Decriptor;
import com.hw2.Encriptor;
import com.hw2.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncriptorTest
{
    @Test
    void testEncryptionDecryption() throws Exception
    {
        Message msg = new Message("Test");
        byte[] encrypted = Encriptor.encrypt(msg);

        Message decrypted = Decriptor.decrypt(encrypted);
        assertEquals("Test", decrypted.getContent());
    }
}