package com.lev.accprog.ui.core;

import java.net.*;
import java.io.*;
import java.nio.Buffer;

public class HttpClient {
    public static void main(String args[]) throws UnsupportedEncodingException {
        String mWord="Import";
        ;
        int serverPort = 6666;


        try {

            System.out.println( " and port " + serverPort + "?");
            Socket socket = new Socket("Localhost", serverPort); // create socket.
            System.out.println("Yes! I just got hold of the program.");


            // Take input and output socets streams
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
           DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
         
                out.write(mWord.getBytes());
                byte buf[]=new byte[64*1024];
                int r=in.read(buf);
                String data=new String(buf,0,r);
                System.out.println(data);
        } catch (Exception x) {
            x.printStackTrace();

        }
    }
}