package com.lev.accprog.base;

import java.io.*;
import java.net.Socket;

public class HttpClient {

    private final int PORT = 8989;
    private PrintWriter mOut;
    private BufferedReader mIn;

    public HttpClient() {
        try {
            Socket socket = new Socket("localhost", PORT);
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            mIn = new BufferedReader(new InputStreamReader(sin));
            mOut = new PrintWriter(sout, true);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public String read() throws IOException {
        return mIn.readLine();
    }

    public void write(String s) {
        mOut.write(s);
    }
}