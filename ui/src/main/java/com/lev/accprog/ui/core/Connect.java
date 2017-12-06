package com.lev.accprog.ui.core;

import java.net.*;
import java.io.*;

public class DBClient {
    public static void main(String args[]) {
        String mWord="Import";
        byte[] m=mWord.getBytes();
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
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            // Create stream and read from keyboard
            String line = "alo";
            System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
            System.out.println();
            out.writeUTF(mWord);
            mWord=in.readUTF();
            out.flush();
            while (true) {
                line = keyboard.readLine(); // wait when user write smdy
                System.out.println("Sending this line to the server...");
                out.writeUTF(line); // send line .
                out.flush(); // end send line.
                line = in.readUTF(); // wait when server answer.
                System.out.println("The server was very polite. It sent me this : " + line);
                System.out.println("Looks like the server is pleased with us. Go ahead and enter more lines.");
                System.out.println();
                if(line.equals("collection")){
                    System.out.println("collectionfsadsa");
                }


            }

        } catch (Exception x) {
            x.printStackTrace();

        }
    }
}