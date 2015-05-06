package com.example.user.ohmygod.FileTransport;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zouyun on 15/4/29.
 */
public class PauseThread extends Thread {
    private final static String TAG = "pauseThread";
    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            try {
                serverSocket = new ServerSocket(12222);
                while (true) {
                    Socket ss = serverSocket.accept();
                    String key = getKey(ss);
                    SocketPool.pause(key);
                }
            } finally {

            }
        } catch (Exception e) {

        }
    }

    private String getKey(Socket ss) {
        ObjectInputStream ois = null;
        String key = "null";
        try {
            ois = new ObjectInputStream(ss.getInputStream());
            key = (String)ois.readObject();
        }catch (Exception e) {

        }
        return key;
    }

}
