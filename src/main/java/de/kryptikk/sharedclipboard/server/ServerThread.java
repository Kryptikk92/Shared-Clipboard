package de.kryptikk.sharedclipboard.server;

import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;

@RequiredArgsConstructor
public class ServerThread extends Thread {

    private final Socket socket;

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);

            String text;
            do {
                text = br.readLine();
                pw.println(text);
            } while (!text.equals("!bye"));
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
