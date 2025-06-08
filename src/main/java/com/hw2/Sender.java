package com.hw2;

import java.net.*;

public class Sender
{
    public void sendMessage(byte[] message, InetAddress target) throws Exception
    {
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(message, message.length, target, 9876);

        socket.send(packet);
        socket.close();
    }
}
