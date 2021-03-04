package ru.home.server;

import lombok.SneakyThrows;
import ru.home.service.Log;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

public class Connection extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private Log log = Log.getInstance();


    public Connection(Socket socket) {
        this.socket = socket;
        try {
            // канал записи в сокет
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            log.loggingMessage(ex.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void run() {

        try {
            String name = in.readLine();
            synchronized (Server.connections) {
                Iterator<Connection> iter = Server.connections.iterator();
                while (iter.hasNext()) {
                    iter.next().out.println(name + " cames now");
                }
            }

            String str = "";
            while (true) {
                str = in.readLine();
                if (str.equals("exit")) {
                    break;
                }

                synchronized (Server.connections) {
                    Iterator<Connection> iter = Server.connections.iterator();
                    while (iter.hasNext()) {
                        iter.next().out.println(name + ": " + str);
                    }
                }
            }
        } catch (Exception ex) {
            log.loggingMessage(ex.getMessage());
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();

            Server.connections.remove(this);
            if (Server.connections.size() == 0) {
//                Server.this.closeAll();
                System.exit(0);
            }
        } catch (Exception ex) {
            log.loggingMessage(ex.getMessage());
        }
    }
}
