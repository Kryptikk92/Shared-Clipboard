package de.kryptikk.sharedclipboard.client;

import lombok.SneakyThrows;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.Socket;

public class Client extends Thread {
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            InputStream is = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(is));
            Thread t1 = new Thread(() -> {
                while (!socket.isClosed()) {
                    try {
                        String input = in.readLine();
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(input), null);
                        System.out.println(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            OutputStream os = socket.getOutputStream();
            out = new PrintWriter(os, true);
            t1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void run() {
        String text = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (!text.equals("!bye")) {
            text = br.readLine();
            out.println(text);
        }
        socket.close();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Client host port");
            return;
        }
        new Client(args[0], Integer.parseInt(args[1])).start();
    }
}
