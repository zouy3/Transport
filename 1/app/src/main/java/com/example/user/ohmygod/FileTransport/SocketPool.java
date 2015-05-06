package com.example.user.ohmygod.FileTransport;

import android.util.Log;

import java.net.Socket;
import java.util.Map;

/**
 * Created by zouyun on 15/4/29.
 */
public class SocketPool {
    public static Map<String, Socket[]> pool;

    public static void pause(String key) {
        Socket[] tmp = pool.get(key);
        for (int i = 0; i < tmp.length; i++) {
            try {
                if (tmp[i] != null)
                    tmp.clone();
            } catch (Exception e) {
                Log.i("SocketPool", key + "delete wrong");
            }

        }
        pool.remove(key);
    }
}
