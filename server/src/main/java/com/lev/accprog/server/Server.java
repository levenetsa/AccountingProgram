package com.lev.accprog.server;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class Server {
    // general
    private static final String module = "ServerHttpClient";

    // SOCKET
    private static final int socketPort = 8989;
    static ServerSocket socket;

    public static void main(String[] ar) throws IOException {
        log("main", "Starting server");

        socket = new ServerSocket(socketPort);
        // listen socket, accept new connections and start handler thread
        try {
            while (true) {
                new Handler(socket.accept()).start();
            }
        } finally {
            socket.close();
        }
    }


    private static class Handler extends Thread {
        private Socket socket;
        private BufferedReader mInput;
        private PrintWriter mOutput;

        public Handler(Socket socket) {
            this.socket = socket;
            log("A new client has been connected");
        }

        public void run() {
            QueueHolder queueHolder = null;
            try {
                mInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                mOutput = new PrintWriter(socket.getOutputStream(), true);
                queueHolder = new QueueHolder();
                while (true) {
                    if (!socket.isClosed()) {
                        String input = mInput.readLine();
                        if (input == null) {
                            return;
                        }
                        log("Received message: " + input);
                        queueHolder.handleCommand(new Command(input));
                        JSONArray array = new JSONArray(queueHolder.getQueue()
                                .stream().map(FoodX::new).collect(Collectors.toList()));
                        String a = array.toString();
                        System.out.println(a);
                        mOutput.println(a);
                        queueHolder.handleCommand(new Command("put data.txt"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void log(String msg) {
        System.out.printf("%s => %s%n", MethodHandles.lookup().lookupClass(), msg);
    }

    private static void log(String method, String msg) {
        System.out.printf("%s::%s => %s%n", MethodHandles.lookup().lookupClass(), method, msg);
    }
}