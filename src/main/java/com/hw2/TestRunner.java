package com.hw2;

public class TestRunner
{

    public static void main(String[] args)
    {
        System.out.println("Starting network tests...");

        testTCP();

        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        testUDP();
    }

    private static void testTCP()
    {
        System.out.println("\n=== Testing TCP ===");

        Thread serverThread = new Thread(new Runnable() {
            public void run() {
                StoreServerTCP server = new StoreServerTCP();
                server.startServer();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        for (int i = 1; i <= 2; i++)
        {
            final int clientNum = i;
            Thread clientThread = new Thread(new Runnable()
            {
                public void run() {
                    StoreClientTCP client = new StoreClientTCP();
                    if (client.connectToServer())
                    {
                        client.sendMessage("Hello from TCP client " + clientNum);
                        client.sendMessage("Message 2 from client " + clientNum);
                        client.disconnect();
                    }
                }
            });
            clientThread.start();
        }

        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("TCP test finished");
    }

    private static void testUDP()
    {
        System.out.println("\n=== Testing UDP ===");

        Thread serverThread = new Thread(new Runnable()
        {
            public void run() {
                StoreServerUDP server = new StoreServerUDP();
                server.startServer();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        for (int i = 1; i <= 2; i++)
        {
            final int clientNum = i;
            Thread clientThread = new Thread(new Runnable()
            {
                public void run() {
                    StoreClientUDP client = new StoreClientUDP();
                    client.sendMessage("Hello from UDP client " + clientNum);
                    client.sendMessage("Message 2 from client " + clientNum);
                    client.close();
                }
            });
            clientThread.start();
        }

        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("UDP test finished");
    }
}