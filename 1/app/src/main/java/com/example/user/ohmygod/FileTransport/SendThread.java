package com.example.user.ohmygod.FileTransport;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * Created by zouyun on 15/4/25.
 */
public class SendThread extends Thread {
    private final static String TAG = "SendThread";
    private Sender sender;
    private int threadId;
    private int completeLength;
    private int block;
    private String path;
    private Socket socket;
    private int fileSize;
    private boolean finish = false;
    private final int BUFFER_SIZE = 0x100000;

    public SendThread(Sender sender, int threadId, int completeLength, int block, String path, Socket socket, int fileSize) {
        this.sender = sender;
        this.threadId = threadId;
        this.completeLength = completeLength;
        this.block = block;
        this.path = path;
        this.socket = socket;
        this.fileSize = fileSize;
    }

    public boolean isFinish() {
        return finish;
    }

    @Override
    public void run() {
        RandomAccessFile file = null;
        DataOutputStream dos = null;
        int startPos = (threadId - 1) * block;
        int end = threadId * block - 1;
        int endPos = (end > fileSize - 1) ? fileSize - 1 : end;
        block = endPos - startPos + 1;
        if (completeLength < block) {
            try {
                try {
                    file = new RandomAccessFile(path, "rwd");
                    dos = new DataOutputStream(socket.getOutputStream());
                    startPos += completeLength;
                    sendStart(startPos, socket);
                    file.seek(startPos);
                    int readLength = BUFFER_SIZE;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while (completeLength < block) {
                        if (block - completeLength < BUFFER_SIZE) {
                            readLength = (block - completeLength);
                        }
                        if (readLength == 0) break;
                        int length = file.read(buffer, 0, readLength);
                        completeLength += length;
                        dos.write(buffer, 0, length);
                        dos.flush();
                        sender.append(length);
                        sender.update(threadId, completeLength);
                    }
                    Log.i("SendThreadId" + threadId, "complete");
                    Log.i(threadId + " " + completeLength , "complete");
                    finish = true;
                } finally {
                    if (file != null)
                        file.close();
                    if (dos != null)
                        dos.close();
                }
            } catch (Exception e) {
                Log.i(TAG, e.toString());
                completeLength = -1;
            }
        }
    }

    private void sendStart(int start, Socket sendSocket) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(sendSocket.getOutputStream());
            oos.writeObject((Integer)start);
        }catch (Exception e) {
            Log.i("sendStart", e.toString());
        }

    }

}