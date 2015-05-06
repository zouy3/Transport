package com.example.user.ohmygod.FileTransport;

import java.io.File;
import java.io.Serializable;

/**
 * Created by zouyun on 15/4/24.
 */
public class FileData implements Serializable{
    private String fileName;
    private int fileLengh;

    public FileData(String path, int fileLengh)
    {
        this.fileName = subFileName(path);
        this.fileLengh = fileLengh;
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getFileLengh()
    {
        return fileLengh;
    }

    private String subFileName(String path)
    {
        String name = path.substring(path.lastIndexOf('/') + 1);
        if (name == null || "".equals(name.trim()))
        {
            name = "NotKnow.tmp";
        }
        return name;
    }

    public String getFilePath(String path, String name) {
        String backPath = path + name;
        String filename = name.substring(0, name.lastIndexOf("."));
        String type = name.substring(name.lastIndexOf(".") + 1);
        if (type == null)
            type = "";
        if (!new File(backPath).exists())
            return backPath;
        else {
            for (int i = 1; ; i++) {
                backPath = path + filename + "(" + i + ")." + type;
                if (!new File(backPath).exists())
                    return backPath;
            }
        }
    }

    public String getFileKey()
    {
        return fileName + fileLengh;
    }
}
