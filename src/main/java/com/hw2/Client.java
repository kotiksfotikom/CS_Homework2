package com.hw2;

import java.net.*;

public class Client
{
    public static void main(String[] args) throws Exception
    {
        Message message = new Message("Hello from client");
        byte[] encrypted = Encriptor.encrypt(message);
        new Sender().sendMessage(encrypted, InetAddress.getByName("localhost"));
    }
}