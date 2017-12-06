package com.lev.accprog.ui.core;

import java.net.*;
import java.io.*;
public class DBConnector {
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

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
            while(true) {
                line = in.readUTF(); // wait client.
                System.out.println("The dumb client just sent me this line : " + line);
                System.out.println("I'm sending it back...");
                out.writeUTF(line); // send line for client.
                out.flush(); // end stream.
                System.out.println("Waiting for the next line...");
                System.out.println();
            }
        } catch(Exception x) { x.printStackTrace(); }
    }
}