package com.hw2;

import java.io.*;
import java.net.*;

public class StoreServerTCP
{
    private int port = 9876;
    private ServerSocket serverSocket;

    public void startServer() {
        try
        {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e)
        {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    class ClientHandler implements Runnable
    {
        private Socket clientSocket;

        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run() {
            try
            {
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

                while (true)
                {
                    try
                    {
                        byte[] encryptedData = (byte[]) input.readObject();

                        Message message = Decriptor.decrypt(encryptedData);
                        System.out.println("Received: " + message.getContent());

                        Processor processor = new Processor();
                        processor.process(message);

                        Message response = new Message("Server received: " + message.getContent());
                        byte[] encryptedResponse = Encriptor.encrypt(response);
                        output.writeObject(encryptedResponse);
                        output.flush();

                    } catch (Exception e)
                    {
                        System.out.println("Client disconnected");
                        break;
                    }
                }

                clientSocket.close();

            } catch (IOException e)
            {
                System.out.println("Error handling client: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        StoreServerTCP server = new StoreServerTCP();
        server.startServer();
    }
}
