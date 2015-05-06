package com.example.user.ohmygod.FileTransport;

import android.util.Log;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * Created by zouyun on 15/4/25.
 */
public class RecvThread extends Thread {
    private final static String TAG = "RecvThread";
    private Reciver reciver;
    private int threadId;
    private int completeLength;
    private int block;
    private String path;
    private Socket socket;
    private int fileSize;
    private final int BUFFER_SIZE = 0x100000;
    private boolean finish = false;

    public RecvThread (Reciver reciver, int threadId, int completeLength, int block, String path, Socket socket, int fileSize)
    {
        this.reciver = reciver;
        this.threadId = threadId;
        this.completeLength = completeLength;
        this.block = block;
        this.path = path;
        this.socket = socket;
        this.fileSize = fileSize;
    }

    @Override
    public void run() {
        RandomAccessFile file = null;
        DataInputStream dis = null;
        if (completeLength < block) {
            try {
                try {
                    file = new RandomAccessFile(path, "rwd");
                    dis = new DataInputStream(socket.getInputStream());
                    int startPos =getStart(socket);
                    int end = threadId * block - 1;
                    file.seek(startPos);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int length = 0;
                    while ((length = dis.read(buffer, 0, BUFFER_SIZE)) > 0) {
                        file.write(buffer, 0, length);
                        completeLength += length;
                        reciver.append(length);
                        reciver.update(threadId, completeLength);
                    }
                    finish = true;
                } finally {
                    if (file != null)
                        file.close();
                    if (dis != null)
                        dis.close();

                }
            } catch (Exception e) {
                Log.i(TAG + threadId, e.toString());
                completeLength = -1;
            }
        }
    }

    public boolean isFinish(){
        return finish;
    }


    private int getStart(Socket recvSocket) {
        ObjectInputStream ois = null;
        int start = 0;
        try{
            ois = new ObjectInputStream(recvSocket.getInputStream());
            start = (Integer)ois.readObject();
        }catch (Exception e) {
            Log.i("getStart", e.toString());
        }
        return start;
    }
}
