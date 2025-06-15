import com.hw2.CRC16;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CRC16Test {

    @Test
    void testCRC() {
        byte[] data = "123456789".getBytes();
        int crc = CRC16.calculate(data, 0, data.length);
        assertEquals(0xBB3D, crc);
    }
}