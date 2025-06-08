package com.hw2;

import java.net.*;
import java.util.concurrent.BlockingQueue;

public class Receiver implements Runnable
{
    private final BlockingQueue<byte[]> queue;
    public Receiver(BlockingQueue<byte[]> queue) { this.queue = queue; }
    public void run()
    {
        try (DatagramSocket socket = new DatagramSocket(9876))
        {
            while (true)
            {
                byte[] buffer = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                queue.put(packet.getData());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
