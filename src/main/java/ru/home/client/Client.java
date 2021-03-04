package ru.home.client;

import ru.home.service.AppConfig;
import ru.home.service.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        proxyConnect(appConfig);
    }

    private static void proxyConnect(AppConfig appConfig) {
        Scanner scan = new Scanner(System.in);
        Log log = Log.getInstance();

        BufferedReader  in = null;
        PrintWriter out = null;
        Socket socket = null;

        try {
            socket = new Socket(appConfig.getIP(), appConfig.getPort());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Введите имя:");
            out.println(scan.nextLine());

            Resender resend = new Resender(in);
            resend.start();

            String str = "";
            while (!str.equals("exit")) {
                str = scan.nextLine();
                out.println(str);
            }
            resend.setStop();
        } catch (Exception ex) {
            log.loggingMessage(ex.getMessage());
        }finally {
            log.loggingMessage("Close in, out, socket for connection...");
            close(in, out, socket);
            System.exit(11);
        }
    }

    private static void close(BufferedReader in, PrintWriter out, Socket socket) {
        Log log = Log.getInstance();
        try {
            in.close();
            out.close();
            socket.close();
            System.exit(11);
        } catch (Exception ex) {
            log.loggingMessage(ex.getMessage());
        }
    }

}
