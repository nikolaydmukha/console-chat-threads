package ru.home.server;

import ru.home.service.AppConfig;
import ru.home.service.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        Log log = Log.getInstance();

        log.loggingMessage("Starting server");
        while (true) {
            try (
                    ServerSocket server = new ServerSocket(appConfig.getPort())
            ) {
                //акцептим подключения
                Socket socket = server.accept();
                // канал записи в сокет
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                // канал чтения из сокета
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                Thread t = new ServerThread(socket, dos, dis);
                t.start();
                log.loggingMessage("Added new client " + t.getName());
            } catch (IOException e) {
                log.loggingMessage(e.getMessage());
            }
        }
    }
}
