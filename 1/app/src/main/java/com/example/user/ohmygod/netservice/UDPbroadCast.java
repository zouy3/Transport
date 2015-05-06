package com.example.user.ohmygod.netservice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by user on 15-5-4.
 */

/* 发送udp多播 */
public  class UDPbroadCast extends Thread {

    private UDPbroadcastResponse mOnResponse;

    byte[] data = new byte[1024];

    public interface UDPbroadcastResponse {
        public void onResponse();
    }

    // data to send
    public UDPbroadCast(UDPbroadcastResponse udPbroadcastResponse, String dataString) {
        data = dataString.getBytes();
        mOnResponse = udPbroadcastResponse;
    }

    @Override
    public void run() {
        try {
            MulticastSocket sender = null;
            DatagramPacket dj = null;
            InetAddress group = null;

            sender = new MulticastSocket();
            group = InetAddress.getByName("224.0.0.1"); // address for multi_broadcast
            dj = new DatagramPacket(data, data.length, group, 6789);
            sender.send(dj);
            sender.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        mOnResponse.onResponse();
    }
}