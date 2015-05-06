package com.example.user.ohmygod.netservice;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/* 接收tcp连接 */
public class TCPReceive extends Thread {


    private ReceivePacket mOnTCPreceive;

    public TCPReceive(ReceivePacket onTCPreceive) {
        this.mOnTCPreceive = onTCPreceive;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        Socket socket;
        BufferedReader in;
            serverSocket = null;
            socket = null;
            in = null;
            try {
                serverSocket = new ServerSocket(8080);
                while (true) {
                    socket = serverSocket.accept();

                    if (socket != null) {
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String IPstring;
                        IPstring = socket.getInetAddress().getHostAddress(); // source IP

                        // IP address
                        String source_address = IPstring.trim();
                        // data
                        StringBuilder dataString = new StringBuilder();

                        String line = null;
                        while ((line = in.readLine()) != null) {
                            dataString.append(line);
                        }

                        mOnTCPreceive.onResponse(source_address, dataString.toString().trim());
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (in != null)
                        in.close();
                    if (socket != null)
                        socket.close();
                    if (serverSocket != null)
                        serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
