package com.hw2;

import java.io.*;
import java.net.*;

public class StoreClientTCP
{
    private String serverHost = "localhost";
    private int serverPort = 9876;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean isConnected = false;

    public boolean connectToServer()
    {
        try
        {
            socket = new Socket(serverHost, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isConnected = true;
            System.out.println("Connected to server");
            return true;
        } catch (IOException e)
        {
            System.out.println("Cannot connect to server: " + e.getMessage());
            isConnected = false;
            return false;
        }
    }

    public void sendMessage(String messageText)
    {
        if (!isConnected)
        {
            System.out.println("Not connected to server");
            return;
        }

        try
        {
            Message message = new Message(messageText);
            byte[] encryptedData = Encriptor.encrypt(message);

            output.writeObject(encryptedData);
            output.flush();

            byte[] responseData = (byte[]) input.readObject();
            Message response = Decriptor.decrypt(responseData);
            System.out.println("Server response: " + response.getContent());

        } catch (IOException e)
        {
            System.out.println("Connection lost: " + e.getMessage());
            isConnected = false;

            System.out.println("Trying to reconnect...");
            if (connectToServer())
            {
                System.out.println("Reconnected successfully");
                sendMessage(messageText);
            } else
            {
                System.out.println("Failed to reconnect");
            }

        } catch (Exception e)
        {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    public void disconnect()
    {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            isConnected = false;
            System.out.println("Disconnected from server");
        } catch (IOException e)
        {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        StoreClientTCP client = new StoreClientTCP();

        if (client.connectToServer())
        {
            client.sendMessage("Hello from TCP client!");
            client.sendMessage("Test message 1");
            client.sendMessage("Test message 2");

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            client.sendMessage("Final message");
        }

        client.disconnect();
    }
}
