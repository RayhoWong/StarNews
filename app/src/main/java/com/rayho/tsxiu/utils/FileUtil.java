package com.rayho.tsxiu.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static com.rayho.tsxiu.base.Constant.CACHE_TIME;

/**
 * Created by Rayho on 2019/3/9
 **/
public class FileUtil {

    /**
     * 写入文件
     *
     * @param ser  javabean对象
     * @param file 文件名(唯一确定)
     * @return
     */
    public static boolean saveObjectByFile(Context context, Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取文件
     *
     * @param file 文件名(唯一确定)
     * @return 返回javabean对象
     */
    public static Serializable readObjectByFile(Context context, String file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * 判断是否需要读取文章缓存
     * 当用户在指定间隔时间内读取同一数据源时，从本地获取，超过这个时间间隔从网络获取，
     * 这样做的目的是节省用户的流量，同时也避免了每次从网络获取数据造成的界面延迟。
     *
     * @param cachefile 文件名(唯一确定)
     * @return true 不需要
     */
    public static boolean isCacheDataFailure(Context context, String cachefile) {
        boolean failure = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists()
                && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            //文件缓存的时间大于指定的间隔
            failure = true;
        else if (!data.exists())
            //文件不存在
            failure = true;

        return failure;
    }


    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public static void copyFile(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
