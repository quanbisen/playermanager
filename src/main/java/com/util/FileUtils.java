package com.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /**
     *  根据byte数组，生成文件
     * @param bfile byte字节流
     * @param filePath 文件存放目录
     * @param fileName 文件名称(不带后缀的)
     * @return
     */
    public static File getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&!dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            //创建临时文件的api参数 (文件前缀,文件后缀,存放目录)
            file = File.createTempFile(fileName, ".jpg", dir);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建临时文件失败!" + e.getMessage());
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
