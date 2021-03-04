package ru.home.server;

import lombok.SneakyThrows;
import ru.home.service.AppConfig;
import ru.home.service.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Log log = Log.getInstance();


    public ServerThread(Socket socket, DataOutputStream dos, DataInputStream dis) {
        this.socket = socket;
        this.dos = dos;
        this.dis = dis;
    }

    @SneakyThrows
    @Override
    public void run() {
        String entry;

        try {

            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт(пока поток не прерван)
            while (!this.isInterrupted()) {
                // ждем получения данных клиента
                entry = dis.readUTF();
                // логгируем сообщение клиента
                log.loggingMessage("User utterance >>>" + entry + "\n");
                System.out.println(entry);
                // если условие окончания работы не верно - продолжаем работу
                if (entry.equalsIgnoreCase("exit")) {
                    this.interrupt();
                }
                dos.writeUTF("Hello " + Thread.currentThread().getName());
                dos.flush();
            }
            // если условие выхода - верно выключаем соединения
            // закрываем сначала каналы сокета !
            dis.close();
            dos.close();
            // потом закрываем сам сокет общения на стороне сервера!
            socket.close();
        } catch (Exception ex) {
            log.loggingMessage(ex.getMessage());
        }
    }
}
