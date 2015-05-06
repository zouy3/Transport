package com.example.user.ohmygod.netservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.user.ohmygod.LocalData;
import com.example.user.ohmygod.dataStructure.FriendListItem;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by user on 15-5-4.
 */

/*接收udp多播 并 发送tcp 连接*/
public class UdpReceiveAndtcpSend extends Thread {


    private MulticastSocket ms = null;
    private DatagramPacket dp;
    private ReceivePacket mOnUDPreceive;
    private String mReplayData;

    public UdpReceiveAndtcpSend(ReceivePacket onUDPreceive, String replyData) {
        mOnUDPreceive = onUDPreceive;
        mReplayData = replyData;
    }

    @Override
    public void run() {
        Message msg;
        String information;

        byte[] data = new byte[1024];
        try {
            InetAddress groupAddress = InetAddress.getByName("224.0.0.1");
            ms = new MulticastSocket(6789);
            ms.joinGroup(groupAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dp = new DatagramPacket(data, data.length);

        while (true) {
            try {
                if (ms != null) {
                    ms.receive(dp);
                }
            } catch (SocketException e) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            if (dp.getAddress() != null) {
                final String quest_ip = dp.getAddress().toString();

                String host_ip = getLocalHostIp();

                /* 若udp包的ip地址 是 本机的ip地址的话，丢掉这个包(不处理)*/
                if( (!host_ip.equals(""))  && host_ip.equals(quest_ip.substring(1)) ) {
                    continue;
                }

                // data
                final String codeString = new String(data, 0, dp.getLength());

                mOnUDPreceive.onResponse(quest_ip.substring(1), codeString);

                Socket socket = null;
                try {
                    final String target_ip = dp.getAddress().toString().substring(1);

                    socket = new Socket(target_ip, 8080);
                    socket.getOutputStream().write(mReplayData.getBytes());
                    socket.getOutputStream().flush();
                    socket.getOutputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (socket != null)
                        socket.close();
                    //if (ms != null)
                    //    ms.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch(SocketException e)
        {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }
}
