package com.example.user.ohmygod.FileTransport;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Map;

/**
 * Created by zouyun on 15/4/24.
 */
public class Sender {
    private final static int threadsNum = 3;
    private final static String TAG = "sender";
    private Socket sendSocket;
    private final int BUFFER_SIZE = 0x100000;
    private int port = 7093;
    private int saveLength = 0;
    private Context context;
    private SendThread[] threads;
    private int fileLength;
    private Map<Integer, Integer> data;

    public Sender(Context context) {
        this.context = context;
    }

    public void send(String url, String path) {
        RandomAccessFile file = null;
        DataOutputStream dos = null;
        threads = new SendThread[threadsNum];
        try {
            try {
                file = new RandomAccessFile(path, "rwd");
                fileLength = (int)file.length();
                print(fileLength + "");
                sendSocket = new Socket(url, port);
                byte[] buffer = new byte[BUFFER_SIZE];
                FileData fileData = new FileData(path, fileLength);
                sendTest(sendSocket, fileData);
                data = recvData(sendSocket);
                dos = new DataOutputStream(sendSocket.getOutputStream());
                long start = System.currentTimeMillis();
                int block = (fileLength % this.threads.length) == 0?fileLength / this.threads.length : fileLength / this.threads.length + 1;
                for (int i = 0; i < threadsNum; i ++) {
                    saveLength += data.get(i + 1);
                    print(i + 1 + ":" +  data.get(i + 1) + "complete");
                }
                int sendport = recvPort(sendSocket);
                for (int i = 0; i < threadsNum; i ++)
                {
                    Socket ss = new Socket(url, sendport);
                    threads[i] = new SendThread(this, i + 1, data.get(i + 1), block, path, ss, fileLength);
                    threads[i].setPriority(7);
                    threads[i].start();
                }
                boolean finish = false;
                while (!finish) {
                    Thread.sleep(900);
                    finish = true;
                    for (int i = 0;i < threadsNum; i ++) {
                        if (!threads[i].isFinish())
                            finish = false;
                    }
                }
                System.out.println("Spend: " + (double) (System.currentTimeMillis() - start) / 1000 + "s");
            } finally {
                if (sendSocket != null)
                    sendSocket.close();
                if (dos != null)
                    dos.close();
                if (file != null)
                    file.close();
            }
        } catch (Exception e) {
            print(e.toString());
        }
    }


    public void sendTest(Socket sendSocket, FileData fileData)
    {
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(sendSocket.getOutputStream());
            oos.writeObject(fileData);
        }
        catch (Exception e)
        {
            Log.i("send", e.toString());
        }
    }

    private Map<Integer, Integer> recvData(Socket recvSocet)
    {
        ObjectInputStream ois = null;
        Map<Integer, Integer> data = null;
        try
        {
            ois = new ObjectInputStream(recvSocet.getInputStream());
            data = (Map<Integer, Integer>)ois.readObject();
        }
        catch(Exception e)
        {
            print(e.toString());
        }
        return data;
    }

    private int recvPort(Socket socket) {
        ObjectInputStream ois = null;
        int port = 0;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            port = (Integer) ois.readObject();
        } catch (Exception e) {

        }
        return port;
    }

    protected synchronized void append(int size) {
        saveLength += size;
        print("complete" + saveLength * 100.0 / fileLength + "%");
    }

    protected synchronized void update(int threadId, int pos) {
        this.data.put(threadId, pos);
    }

    private void print(String e)
    {
        Log.i(TAG, e);
    }
}
