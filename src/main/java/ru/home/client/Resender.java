package ru.home.client;

import ru.home.service.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class Resender extends Thread {

    private boolean stopped;
    private Log log = Log.getInstance();
    private BufferedReader in;


    public Resender(BufferedReader in) {
        this.in = in;
    }

    public void setStop() {
        stopped = true;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                String str = in.readLine();
                System.out.println("sssss");
                System.out.println(str);
            }
        } catch (IOException ex) {
            log.loggingMessage(ex.getMessage());
        }
    }
}