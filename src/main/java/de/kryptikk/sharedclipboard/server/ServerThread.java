package de.kryptikk.sharedclipboard.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.util.List;

@RequiredArgsConstructor
public class ServerThread extends Thread {

    private final Socket socket;
    private final List<ServerThread> clients;
    @Getter
    private PrintWriter out;

    public void run() {

        try {
            InputStream is = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            OutputStream os = socket.getOutputStream();
            out = new PrintWriter(os, true);

            clients.add(this);

            String text;
            do {
                text = in.readLine();
                String finalText = text;
                clients.parallelStream().forEach(c -> c.getOut().println(finalText));
            } while (!text.equals("!bye"));
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
