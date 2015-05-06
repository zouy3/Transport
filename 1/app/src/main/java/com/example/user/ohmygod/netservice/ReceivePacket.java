package com.example.user.ohmygod.netservice;

/**
 * Created by zhang_000 on 2015/5/5.
 */
public interface ReceivePacket {
    public void onResponse(String sourceIP, String data);
}
