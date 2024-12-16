package com.example;


import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8080);
            while (true) {
                Socket s = ss.accept();
                MyThread t= new MyThread(s);
                t.start();
            }

        } catch (Exception e) {
            System.out.println("errore server");
            e.printStackTrace();
        }
    }

}