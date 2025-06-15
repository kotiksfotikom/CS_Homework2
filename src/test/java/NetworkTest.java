package com.hw2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NetworkTest {

    @Test
    void testMessageEncryptionDecryption() throws Exception {
        Message msg = new Message("Test message");
        byte[] encrypted = Encriptor.encrypt(msg);

        Message decrypted = Decriptor.decrypt(encrypted);
        assertEquals("Test message", decrypted.getContent());
    }

    @Test
    void testTCPConnection() throws Exception {
        Thread serverThread = new Thread(() -> {
            StoreServerTCP server = new StoreServerTCP();
            server.startServer();
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(1000);

        StoreClientTCP client = new StoreClientTCP();
        boolean connected = client.connectToServer();
        assertTrue(connected);

        client.disconnect();
    }

    @Test
    void testUDPMessage() throws Exception {
        Thread serverThread = new Thread(() -> {
            StoreServerUDP server = new StoreServerUDP();
            server.startServer();
        });
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(1000);

        StoreClientUDP client = new StoreClientUDP();
        boolean sent = client.sendMessage("Test UDP");
        assertTrue(sent);

        client.close();
    }
}