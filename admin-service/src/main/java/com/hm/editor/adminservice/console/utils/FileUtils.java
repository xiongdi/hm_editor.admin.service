package com.hm.editor.adminservice.console.utils;

import java.io.*;
import java.util.List;

/**
  * <p>文件工具类</p>
  * @author: wanglei
  */
public class FileUtils {
    private final static String CHAR_SET = "UTF-8";
    public static boolean writeStrToFile(String path,String str) throws IOException{
        return writeStrToFile(path, str,CHAR_SET);
    }
    public static boolean writeStrToFile(String path,String str,String charSet) throws IOException{
        OutputStreamWriter os = null;
        FileOutputStream fos = null;
        File f = newFile(path);
        try {
            fos = new FileOutputStream(f);
            os = new OutputStreamWriter(fos,charSet);
            os.write(str);
            os.flush();
        }catch (IOException e){
            delFile(f);
            throw e;
        }finally {
            close(fos);
            close(os);
        }
        return true;
    }


    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(filePath + fileName)) {
            out.write(file);
            out.flush();
            out.close();
        }
    }

    public static boolean delFile(String path){
        return delFile(file(path));
    }
    public static boolean delFile(File f){
        if(!f.exists()) return true;
        if(f.isFile()){
            f.delete();
        }else {
            for(File lf:f.listFiles()){
                delFile(lf);
            }
            f.delete();
        }

        return true;
    }
    public static boolean isDir(File f){
        return f == null?false:(f.exists() && f.isDirectory());
    }
    public static File file(String path){
        return new File(path);
    }
    public static File newFile(File file) throws IOException{
        if(file.exists()){
            file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }
    public static File newFile(String path) throws IOException {
        return newFile(file(path));
    }

    public static List<Object> readObject(File f,Class cla) throws IOException{
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object r = ois.readObject();
            return (List)r;
        }catch (Exception ioe){
            throw new IOException(ioe.toString());
        }finally {
            close(fis);
            close(ois);
        }
    }
    public static String separatorChar(){
        return File.separator;
    }

    public static void writeObject(List<? extends Object> data,String dir) throws IOException{
        String path = data.get(0).getClass().getSimpleName()+".obj";
        File f = newFile(dir+separatorChar()+path);
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
        }catch (Exception ioe){
            FileUtils.delFile(f);
            throw new IOException(ioe.toString());
        }finally {
            close(fos);
            close(oos);
        }

    }
    public static void close(OutputStream os) throws IOException{
        if(null != null) os.close();
    }
    public static void close(OutputStreamWriter os) throws IOException{
        if(null != null) os.close();
    }
    public static void close(InputStream is) throws IOException{
        if(null != null) is.close();
    }

}
