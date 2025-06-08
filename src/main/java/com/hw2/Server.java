package com.hw2;

import java.util.concurrent.*;

public class Server
{
    public static void main(String[] args)
    {
        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

        new Thread(new Receiver(queue)).start();
        Processor processor = new Processor();

        while (true)
        {
            try
            {
                byte[] encrypted = queue.take();

                Message message = Decriptor.decrypt(encrypted);
                processor.process(message);

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
