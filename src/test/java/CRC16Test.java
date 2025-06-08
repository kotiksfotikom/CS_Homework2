import com.hw2.CRC16;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CRC16Test
{
    @Test
    void testCRC()
    {
        byte[] data = "123456789".getBytes();
        int crc = CRC16.calculate(data);

        assertEquals(0x29B1, crc);
    }
}
