package com.lev.accprog.ui.core;

import java.net.*;
import java.io.*;
public class ServerHttpClient {
    public static void main(String[] ar)    {
        int port = 6666; // port
        try {
            ServerSocket ss = new ServerSocket(port); // create socket
            System.out.println("Waiting for a client...");

            Socket socket = ss.accept(); // talk server for wait client
            System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
            System.out.println();
            // Take input and output socets streams.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            String mWord= "alo";
            byte[] m=mWord.getBytes();
            while(true) {
                sin.read();
                sout.write(m);
                System.out.println("alo");

            }
        } catch(Exception x) { x.printStackTrace(); }
    }
    public void send(){

    }
}