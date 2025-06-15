package com.hw2;

import java.io.*;
import java.net.*;

public class StoreClientUDP {
    private String serverHost = "localhost";
    private int serverPort = 9877;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int maxRetries = 3;
    private int timeout = 2000; // 2 seconds

    public StoreClientUDP() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            serverAddress = InetAddress.getByName(serverHost);
            System.out.println("UDP Client created");
        } catch (Exception e) {
            System.out.println("Error creating UDP client: " + e.getMessage());
        }
    }

    public boolean sendMessage(String messageText) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("Sending message (attempt " + attempt + "): " + messageText);

                Message message = new Message(messageText);
                byte[] encryptedData = Encriptor.encrypt(message);

                DatagramPacket sendPacket = new DatagramPacket(encryptedData,
                        encryptedData.length, serverAddress, serverPort);
                socket.send(sendPacket);

                byte[] ackBuffer = new byte[4096];
                DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
                socket.receive(ackPacket);

                byte[] ackData = new byte[ackPacket.getLength()];
                System.arraycopy(ackPacket.getData(), 0, ackData, 0, ackPacket.getLength());
                Message ackMessage = Decriptor.decrypt(ackData);

                System.out.println("Received ACK: " + ackMessage.getContent());
                return true;

            } catch (SocketTimeoutException e) {
                System.out.println("Timeout on attempt " + attempt + ", retrying...");
                if (attempt == maxRetries) {
                    System.out.println("Failed to send message after " + maxRetries + " attempts");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error on attempt " + attempt + ": " + e.getMessage());
                if (attempt == maxRetries) {
                    System.out.println("Failed to send message: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("UDP Client closed");
        }
    }

    public static void main(String[] args) {
        StoreClientUDP client = new StoreClientUDP();

        client.sendMessage("Hello from UDP client!");
        client.sendMessage("Test UDP message 1");
        client.sendMessage("Test UDP message 2");
        client.sendMessage("Final UDP message");

        client.close();
    }
}