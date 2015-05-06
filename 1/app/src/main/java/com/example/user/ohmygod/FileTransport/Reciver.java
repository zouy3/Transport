package com.example.user.ohmygod.FileTransport;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zouyun on 15/4/24.
 */
public class Reciver {
    private final static int threadsNum = 3;
    private final static String TAG = "Reciver";
    private final int BUFFER_SIZE = 1024;
    private int port = 7093;
    private FileService fileService;
    private ServerSocket serverSocket;
    private Context context;
    private int saveLength = 0;
    private Map<Integer, Integer> data;
    private String key;
    private RecvThread[] threads;
    private boolean finish = false;
    private boolean shutdown = false;

    public Reciver(Context context) {
        this.context = context;
        fileService = new FileService(context);
    }

    public void listen() {
        try {
            serverSocket = new ServerSocket(port);
            print("listening");
            while (true) {
                Socket recvSocket = new Socket();
                recvSocket = serverSocket.accept();
                print("accpet");
                final Socket finalRecvSocket = recvSocket;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recv(finalRecvSocket);
                    }
                }).start();
            }
        } catch (Exception e) {
            print(e.toString());
        }
    }


    private void recv(Socket recvSocket) {
        RandomAccessFile file = null;
        DataInputStream dis = null;
        FileData fileData = null;
        threads = new RecvThread[threadsNum];
        ServerSocket recv = null;
        try {
            try {
                fileData = recvTest(recvSocket);
                print(fileData.getFileLengh() + "");
                String path;
                key = fileData.getFileKey();
                Map<Integer, Integer> logData = fileService.getData(key);
                data = new HashMap<Integer, Integer>();
                if (logData.size() == threadsNum) {
                    for (int i = 0; i < threadsNum; i++)
                        data.put(i + 1, logData.get(i + 1));
                    path = fileService.getpath(key);
                } else {
                    for (int i = 0; i < threadsNum; i++)
                        data.put(i + 1, 0);
                    path = fileData.getFilePath(Environment.getExternalStorageDirectory().getPath() + "/", fileData.getFileName());
                    fileService.save(key, data);
                    fileService.savepath(key, path);
                }
                file = new RandomAccessFile(path, "rwd");
                file.setLength(fileData.getFileLengh());
                for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
                    saveLength += entry.getValue();
                }
                sendData(data, recvSocket);
                int fileLength = fileData.getFileLengh();
                int block = (fileLength % this.threads.length) == 0 ? fileLength / this.threads.length : fileLength / this.threads.length + 1;
                recv = getBind(recvSocket);
                for (int i = 0; i < threadsNum; i++) {
                    Socket ss = new Socket();
                    ss = recv.accept();
                    threads[i] = new RecvThread(this, i + 1, data.get(i + 1), block, path, ss, fileLength);
                    threads[i].setPriority(7);
                    threads[i].start();
                }
                while (!finish) {
                    Thread.sleep(900);
                    finish = true;
                    for (int i = 0; i < threadsNum; i++) {
                        if (!threads[i].isFinish())
                            finish = false;
                    }
                }
                if (finish) {
                    print("Recv() complete");
                    fileService.delete(key);
                    threads = null;
                }
            } finally {
                if (recvSocket != null)
                    recvSocket.close();
                if (file != null)
                    file.close();
                if (dis != null)
                    dis.close();
                if (recv != null)
                    recv.close();
            }
        } catch (Exception e) {
            print(e.toString());
        }
    }

    private FileData recvTest(Socket recvSocket) {
        ObjectInputStream ois = null;
        FileData fileData = null;
        try {
            ois = new ObjectInputStream(recvSocket.getInputStream());
            Object tmp = ois.readObject();
            fileData = (FileData) tmp;
            print(fileData.getFileName());
        } catch (Exception e) {
            Log.i("recv", e.toString());
        }
        return fileData;
    }


    private void sendData(Map<Integer, Integer> data, Socket sendSocket) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(sendSocket.getOutputStream());
            oos.writeObject(data);
        } catch (Exception e) {
            print(e.toString());
        }
    }




    protected synchronized void append(int size) {
        saveLength += size;
    }

    protected synchronized void update(int threadId, int pos) {
        this.data.put(threadId, pos);
        this.fileService.update(this.key, this.data);
    }

    private ServerSocket getBind(Socket socket) {
        int port = 11000;
        ServerSocket ss = null;
        ObjectOutputStream oos = null;
        while (true) {
            try {
                ss = new ServerSocket(port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(port);
                oos.flush();
                break;
            } catch (Exception e) {
                port++;
            }

        }
        return ss;
    }

    private void print(String e) {
        Log.i(TAG, e);
    }
}
