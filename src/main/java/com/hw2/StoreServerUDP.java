package com.hw2;

import java.io.*;
import java.net.*;

public class StoreServerUDP
{
    private int port = 9877;
    private DatagramSocket socket;
    private byte[] buffer = new byte[4096];

    public void startServer()
    {
        try
        {
            socket = new DatagramSocket(port);
            System.out.println("UDP Server started on port " + port);

            while (true)
            {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                Thread handlerThread = new Thread(new PacketHandler(packet));
                handlerThread.start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    class PacketHandler implements Runnable
    {
        private DatagramPacket receivedPacket;

        public PacketHandler(DatagramPacket packet)
        {
            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());
            this.receivedPacket = new DatagramPacket(data, data.length,
                    packet.getAddress(), packet.getPort());
        }

        public void run()
        {
            try {
                byte[] data = new byte[receivedPacket.getLength()];
                System.arraycopy(receivedPacket.getData(), 0, data, 0, receivedPacket.getLength());

                Message message = Decriptor.decrypt(data);
                System.out.println("Received UDP message: " + message.getContent());

                Processor processor = new Processor();
                processor.process(message);

                Message ackMessage = new Message("ACK: " + message.getContent());
                byte[] ackData = Encriptor.encrypt(ackMessage);

                DatagramPacket responsePacket = new DatagramPacket(ackData, ackData.length,
                        receivedPacket.getAddress(), receivedPacket.getPort());

                socket.send(responsePacket);
                System.out.println("Sent ACK to client");

            } catch (Exception e) {
                System.out.println("Error processing packet: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        StoreServerUDP server = new StoreServerUDP();
        server.startServer();
    }
}