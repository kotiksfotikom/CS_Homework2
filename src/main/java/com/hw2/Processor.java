package com.hw2;

public class Processor
{
    public void process(Message message)
    {
        System.out.println("Processed message: " + message.getContent());
    }
}