package com.hm.editor.adminservice.console.utils;

import com.hm.editor.common.utils.StringUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具了了类
 *
 * @author: wanglei
 */
public class ZipUtils {
  public static final String FILE_TYPE = ".zip";

  public static String zip(String path) {
    File f = FileUtils.file(path);
    String fName = f.getAbsolutePath() + FILE_TYPE;
    zip(path, fName);
    return fName;
  }

  public static void zip(String source, String target) {
    File file = FileUtils.file(source);
    ZipOutputStream zipOutputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(target);
      zipOutputStream = new ZipOutputStream(fileOutputStream);
      if (file.isDirectory()) {
        directory(zipOutputStream, file, "");
      } else {
        zipFile(zipOutputStream, file, "");
      }
    } catch (Exception e) {
      throw new RuntimeException("FileUtil toZip error:", e);

    } finally {
      try {
        zipOutputStream.close();
        fileOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void directory(ZipOutputStream zipOutputStream, File file, String parentFileName) {
    File[] files = file.listFiles();
    String parentFileNameTemp = null;
    for (File fileTemp : files) {
      if (fileTemp.isDirectory()) {
        parentFileNameTemp =
            StringUtils.isEmpty(parentFileName)
                ? fileTemp.getName()
                : parentFileName + File.separator + fileTemp.getName();
        directory(zipOutputStream, fileTemp, parentFileNameTemp);
      } else {
        parentFileNameTemp =
            StringUtils.isEmpty(parentFileName) ? File.separator : parentFileName + File.separator;
        zipFile(zipOutputStream, fileTemp, parentFileNameTemp);
      }
    }
  }

  private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName) {
    FileInputStream in = null;
    try {
      ZipEntry zipEntry = new ZipEntry(parentFileName + file.getName());
      zipOutputStream.putNextEntry(zipEntry);
      in = new FileInputStream(file);
      int len;
      byte[] buf = new byte[8 * 1024];
      while ((len = in.read(buf)) != -1) {
        zipOutputStream.write(buf, 0, len);
      }
      zipOutputStream.closeEntry();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static String unzip(String zipFile) throws IOException {
    return unzip(zipFile, FileUtils.file(zipFile).getParentFile().getAbsolutePath());
  }

  public static String unzip(String zipFile, String toDir) throws IOException {

    String name;
    try (ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"))) {
      // 保留压缩包的名字
      name =
          zip.getName()
              .substring(
                  zip.getName().lastIndexOf(FileUtils.separatorChar()) + 1,
                  zip.getName().lastIndexOf('.'));

      for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
        ZipEntry entry = (ZipEntry) entries.nextElement();
        String zipEntryName = entry.getName();
        InputStream in = zip.getInputStream(entry);
        String outPath =
            (toDir + FileUtils.separatorChar() + name + FileUtils.separatorChar() + zipEntryName)
                .replaceAll("\\*", "/");

        // 判断路径是否存在,不存在则创建文件路径
        File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
        if (!file.exists()) {
          file.mkdirs();
        }
        // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
        if (new File(outPath).isDirectory()) {
          continue;
        }

        try (FileOutputStream out = new FileOutputStream(outPath)) {
          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
          }
          in.close();
          out.close();
        }
      }
    }
    return toDir + FileUtils.separatorChar() + name;
  }
}
