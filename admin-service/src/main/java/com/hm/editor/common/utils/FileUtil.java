package com.hm.editor.common.utils;

import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Created by taota on 2017/12/15. */
public class FileUtil {

    public static void generateCSVFile(
        List<List<Object>> objects,
        String fileName,
        boolean append
    ) {
        try {
            File file = new File(fileName);
            if (!append && file.exists()) {
                file.delete();
            }
            CsvWriter writer = cn.hutool.core.text.csv.CsvUtil.getWriter(
                file,
                StandardCharsets.UTF_8,
                append
            );
            for (List<Object> row : objects) {
                String[] rowArray = new String[row.size()];
                for (int i = 0; i < row.size(); i++) {
                    Object o = row.get(i);
                    rowArray[i] = o == null ? "" : o.toString();
                }
                writer.write(rowArray);
            }
            writer.close();
        } catch (Exception e) {
            LogUtils.error("generateCSVFile error", e);
        }
    }

    /**
     * 把文件打成zip
     *
     * @param sourceFilePath 源文件夹名称（包含路径）
     * @param targetZipFile 生成zip文件名（包含路径）
     * @return
     */
    public static void zipDIR(String sourceFilePath, String targetZipFile) {
        ZipOutputStream out = null;
        FileOutputStream target = null;
        BufferedInputStream origin = null;
        FileInputStream fis = null;

        try {
            target = new FileOutputStream(targetZipFile);
            out = new ZipOutputStream(new BufferedOutputStream(target));
            int BUFFER_SIZE = 1024;
            byte[] buff = new byte[BUFFER_SIZE];
            File file = new File(sourceFilePath);
            if (file.isDirectory()) {
                throw new IllegalArgumentException(sourceFilePath + " 不是一个文件!");
            }
            fis = new FileInputStream(file);
            origin = new BufferedInputStream(fis);
            ZipEntry entry = new ZipEntry(file.getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(buff)) != -1) {
                out.write(buff, 0, count);
            }
            //            file.delete();
        } catch (Exception e) {
            LogUtils.error("zipDIR error", e);
        } finally {
            if (origin != null) {
                try {
                    origin.close();
                } catch (IOException e) {
                    LogUtils.error("zipDIR close origin error", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LogUtils.error("zipDIR close out error", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LogUtils.error("zipDIR close fis error", e);
                }
            }
            if (target != null) {
                try {
                    target.close();
                } catch (IOException e) {
                    LogUtils.error("zipDIR close target error", e);
                }
            }
        }
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        } else if (file.exists() && file.isDirectory()) {
            deleteDir(file);
        }
    }

    public static String txtToString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            LogUtils.error("txtToString error", e);
        }
        return result.toString();
    }

    public static void transcodeUTF8_GB2312(String source, String target) {
        try (
            BufferedReader read = new BufferedReader(
                new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8)
            );
            BufferedWriter write = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(target), "GB2312")
            )
        ) {
            String item = read.readLine();
            while (item != null) {
                write.write(item + "\r\n");
                item = read.readLine();
            }
        } catch (Exception e) {
            LogUtils.error("transcodeUTF8_GB2312 error", e);
        }
    }

    public static void transcodeUTF8_code(String source, String target, String code) {
        try (
            BufferedReader read = new BufferedReader(
                new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8)
            );
            BufferedWriter write = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(target), code)
            )
        ) {
            String item = read.readLine();
            while (item != null) {
                write.write(item + "\r\n");
                item = read.readLine();
            }
        } catch (Exception e) {
            LogUtils.error("transcodeUTF8_code error", e);
        }
    }

    public static String txtToString(String filePath) {
        File file = new File(filePath);
        return txtToString(file);
    }

    public static String jarTxtToString(Class<?> receiverCls, String filePath) {
        InputStream ips = receiverCls.getResourceAsStream(filePath);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ips));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            LogUtils.error("txtToString error", e);
        }
        return result.toString();
    }

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(filePath + fileName)) {
            out.write(file);
            out.flush();
            out.close();
        }
    }

    public static void writeExcel(List<List<Object>> data, String excelPath, boolean hasHeader) {
        try {
            ExcelWriter writer = ExcelUtil.getWriter(excelPath);
            if (hasHeader && !data.isEmpty()) {
                // 设置表头
                List<String> headers = new ArrayList<>();
                for (Object o : data.get(0)) {
                    headers.add(o == null ? "" : o.toString());
                }
                writer.writeHeadRow(headers);
                // 写入数据（跳过第一行表头）
                for (int i = 1; i < data.size(); i++) {
                    List<Object> row = data.get(i);
                    List<String> rowStr = new ArrayList<>();
                    for (Object o : row) {
                        rowStr.add(o == null ? "" : o.toString());
                    }
                    writer.writeRow(rowStr);
                }
            } else {
                // 无表头，直接写入所有数据
                for (List<Object> row : data) {
                    List<String> rowStr = new ArrayList<>();
                    for (Object o : row) {
                        rowStr.add(o == null ? "" : o.toString());
                    }
                    writer.writeRow(rowStr);
                }
            }
            writer.close();
        } catch (Exception e) {
            LogUtils.error("generateExcelFile error", e);
        }
    }

    public static void zipFiles(String source, String target) {
        File file = new File(source);
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
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LogUtils.error("zipFiles close error", e);
            }
        }
    }

    private static void directory(
        ZipOutputStream zipOutputStream,
        File file,
        String parentFileName
    ) {
        File[] files = file.listFiles();
        String parentFileNameTemp = null;
        for (File fileTemp : files) {
            if (fileTemp.isDirectory()) {
                parentFileNameTemp = StringUtils.isEmpty(parentFileName)
                    ? fileTemp.getName()
                    : parentFileName + File.separator + fileTemp.getName();

                directory(zipOutputStream, fileTemp, parentFileNameTemp);
            } else {
                parentFileNameTemp = StringUtils.isEmpty(parentFileName)
                    ? File.separator
                    : parentFileName + File.separator;
                zipFile(zipOutputStream, fileTemp, parentFileNameTemp);
            }
        }
    }

    private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName) {
        try (FileInputStream in = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(parentFileName + file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            int len;
            byte[] buf = new byte[8 * 1024];
            while ((len = in.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            LogUtils.error("zipFile error", e);
        }
    }

    public static void createFile(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
    }

    public static void mkdir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public static void deleteDir(String path) {
        File f = new File(path);
        if (f.isFile()) {
            f.delete();
        } else {
            File[] fs = f.listFiles();
            if (fs != null) {
                for (File fss : fs) {
                    deleteDir(fss.getAbsolutePath());
                }
            }
        }
    }

    //    // 针对单sheet页
    //    public static void readExcel(String path, ExcelTypeEnum e, AnalysisEventListener listener)
    // throws IOException {
    //        ExcelReader er = new ExcelReader(new FileInputStream(path), e, null, listener);
    //        er.read();
    //    }

    // 有表头
    public static List<List<Map<String, Object>>> readExcel2Map(String path) throws Exception {
        List<List<Map<String, Object>>> data = new ArrayList<>();
        List<List<List<Object>>> dataList = readExcel2List(path);
        for (List<List<Object>> _data : dataList) {
            List<Map<String, Object>> sheetData = new ArrayList<>();
            int index = 0;
            List<Object> excelHead = _data.get(index++);
            while (index < _data.size()) {
                List<Object> rowData = _data.get(index++);
                int len = rowData.size();
                int cellIndex = 0;
                Map<String, Object> tempData = new HashMap<>();
                for (Object h : excelHead) {
                    if (cellIndex >= len) {
                        break;
                    }
                    Object _o = rowData.get(cellIndex++);
                    tempData.put(
                        h.toString(),
                        h.toString().equals("createDate") ? new Date() : _o == null ? "" : _o
                    );
                }
                sheetData.add(tempData);
            }
            data.add(sheetData);
        }
        return data;
    }

    // 无表头
    public static List<List<List<Object>>> readExcel2List(String path) throws Exception {
        List<List<List<Object>>> data = new ArrayList<>();
        try {
            Workbook wb = getWb(path);
            int sheetNum = wb.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(i);
                List<List<Object>> sheetData = getSheetData(sheet);
                data.add(sheetData);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return data;
    }

    // sheet - List
    public static Map<String, List<List<Object>>> readExcel2Maps(String path) throws Exception {
        Map<String, List<List<Object>>> data = new HashMap<>();
        try {
            Workbook wb = getWb(path);
            int sheetNum = wb.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(i);
                List<List<Object>> sheetData = getSheetData(sheet);
                data.put(sheet.getSheetName(), sheetData);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return data;
    }

    private static Workbook getWb(String path) throws Exception {
        File excel = new File(path);
        if (excel.isFile() && excel.exists()) {
            String[] split = excel.getName().split("\\.");
            Workbook wb;
            // 根据文件后缀（xls/xlsx）进行判断
            if ("xls".equals(split[1])) {
                FileInputStream fis = new FileInputStream(excel);
                wb = new HSSFWorkbook(fis);
            } else if ("xlsx".equals(split[1])) {
                wb = new XSSFWorkbook(excel);
            } else {
                throw new Exception("file type error :" + path);
            }
            return wb;
        } else {
            throw new Exception("no file :" + path);
        }
    }

    private static List<List<Object>> getSheetData(org.apache.poi.ss.usermodel.Sheet sheet) {
        List<List<Object>> data = new ArrayList<>();
        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
        for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                List<Object> cellData = new ArrayList<>();
                int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();
                for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                    // 遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        cellData.add(cell.toString());
                    } else {
                        cellData.add("");
                    }
                }
                data.add(cellData);
            }
        }
        return data;
    }

    public static String absoluteTxtToString(Class<?> receiverCls, String filePath) {
        StringBuilder result = new StringBuilder();
        try (
            InputStream ips = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(ips))
        ) {
            String s;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
        } catch (Exception e) {
            LogUtils.error("absoluteTxtToString error:{},load jarFile:{}", e, filePath);
            return jarTxtToString(receiverCls, filePath);
        }
        return result.toString();
    }
}
