package ru.home.server;

import ru.home.service.AppConfig;
import ru.home.service.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        Log log = Log.getInstance();
        ServerSocket server = null;

        log.loggingMessage("Starting server");
        while (true) {
            try{
                server = new ServerSocket(appConfig.getPort());
                //акцептим подключения
                Socket socket = server.accept();
                Connection connection = new Connection(socket);
                connections.add(connection);
                connection.start();

                log.loggingMessage("Added new connection " + connection.getName());
            } catch (IOException e) {
                log.loggingMessage(e.getMessage());
            }
//            finally {
//                closeAll(server);
//            }
        }
    }

    private static void closeAll(ServerSocket server) {
        Log log = Log.getInstance();
        try {
            server.close();

            synchronized (connections) {
                Iterator<Connection> iter = connections.iterator();
                while (iter.hasNext()) {
                    iter.next().close();
                }
            }
        } catch (Exception ex) {
            log.loggingMessage(ex.getMessage());
        }
    }
}
