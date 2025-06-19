package com.hm.editor.common.utils;


import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created by taota on 2017/12/15.
 */
public class FileUtil {

    private static final CSVFormat csvFormat = CSVFormat.EXCEL.withRecordSeparator(System.lineSeparator());

    public static void generateCSVFile(List<List<Object>> objects, String fileName, boolean append) {

        try (FileWriter fileWriter = new FileWriter(fileName, append);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)) {

            for (List<Object> o : objects) {
                csvPrinter.printRecord(o);
            }
            // 刷写数据
            csvPrinter.flush();
            fileWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把文件打成zip
     *
     * @param sourceFilePath 源文件夹名称（包含路径）
     * @param targetZipFile  生成zip文件名（包含路径）
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
            byte buff[] = new byte[BUFFER_SIZE];
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
            e.printStackTrace();
        } finally {

            if (origin != null) {
                try {
                    origin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (target != null) {
                try {
                    target.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
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
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void transcodeUTF8_GB2312(String source, String target) {
        try (BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-8"));
             BufferedWriter write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "GB2312"))) {
            String item = read.readLine();
            while (item != null) {
                write.write(item + "\r\n");
                item = read.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void transcodeUTF8_code(String source, String target,String code) {
        try (BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-8"));
             BufferedWriter write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), code))) {
            String item = read.readLine();
            while (item != null) {
                write.write(item + "\r\n");
                item = read.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String txtToString(String filePath) {
        File file = new File(filePath);
        return txtToString(file);
    }

    public static String jarTxtToString(Class receiverCls, String filePath) {
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
            e.printStackTrace();
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
            OutputStream outputStream = new FileOutputStream(excelPath);
            ExcelWriter excelWrite = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0);
            sheet.setSheetName("sheet");
            if (hasHeader) {
                List<List<String>> head = new ArrayList<>();
                Table table = new Table(1);
                for (Object o : data.get(0)) {
                    List<String> temp = new ArrayList<>();
                    temp.add(o == null ? "" : o.toString());
                    head.add(temp);
                }
                table.setHead(head);
                data.remove(0);
                excelWrite.write0(tranObjectToStr(data), sheet, table);
            } else {
                excelWrite.write0(tranObjectToStr(data), sheet);
            }
            excelWrite.finish();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<List<String>> tranObjectToStr(List<List<Object>> data) {
        List<List<String>> res = new ArrayList<>();
        for (List<Object> lo : data) {
            List<String> temp = new ArrayList<>();
            for (Object o : lo) {
                temp.add(o == null ? "" : o + "");
            }
            res.add(temp);
        }
        return res;
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
        for (File fileTemp :
                files) {
            if (fileTemp.isDirectory()) {
                parentFileNameTemp = StringUtils.isEmpty(parentFileName) ? fileTemp.getName() : parentFileName + File.separator + fileTemp.getName();

                directory(zipOutputStream, fileTemp, parentFileNameTemp);
            } else {
                parentFileNameTemp = StringUtils.isEmpty(parentFileName) ? File.separator : parentFileName + File.separator;
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
            File fs[] = f.listFiles();
            for (File fss : fs) {
                deleteDir(fss.getAbsolutePath());
            }
        }
    }

//    // 针对单sheet页
//    public static void readExcel(String path, ExcelTypeEnum e, AnalysisEventListener listener) throws IOException {
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
                Map tempData = new HashMap();
                for (Object h : excelHead) {
                    if(cellIndex >= len){
                        break;
                    }
                    Object _o = rowData.get(cellIndex++);
                    tempData.put(h.toString(), h.toString().equals("createDate") ? new Date() : _o == null ? "" : _o);
                }
                sheetData.add(tempData);
            }
            data.add(sheetData);
        }
        return data;
    }

    //无表头
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
            //根据文件后缀（xls/xlsx）进行判断
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
                for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        cellData.add(cell.toString());
                    }else{
                        cellData.add("");
                    }
                }
                data.add(cellData);
            }

        }
        return data;
    }
    public static String absoluteTxtToString(Class receiverCls, String filePath) {

        StringBuilder result = new StringBuilder();
        InputStream ips = null;
        BufferedReader br = null;
        try {
            ips = new FileInputStream(filePath);
            br = new BufferedReader(new InputStreamReader(ips));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
        } catch (Exception e) {
            LogUtils.error("absoluteTxtToString error:{},load jarFile:{}",e,filePath);
            return jarTxtToString(receiverCls,filePath);
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
                if( ips != null){
                    ips.close();
                }
            }catch (Exception e){
                LogUtils.error("absoluteTxtToString close() error:{}",e);
            }
        }
        return result.toString();
    }
    public static void writeDBF(List<List<Object>> data,String file,String charset,boolean writeHeader) throws FileNotFoundException{
        DBFWriter dbfWriter = null;
        try{
            dbfWriter = new DBFWriter(new FileOutputStream(file), Charset.forName(charset));
            if(writeHeader) {
                _dbfHeader(dbfWriter,data.get(0),file,charset);
                data.remove(0);
            }
            _dbfBody(dbfWriter,data,file,charset);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException(e.toString());
        }finally {

            if(dbfWriter != null){
                dbfWriter.close();
            }
        }
    }
    private static void _dbfHeader(DBFWriter dbfWriter,List<Object> headers,String file,String charset) throws FileNotFoundException{
        int len = headers.size();
        DBFField[] fields = new DBFField[len];
        int i = 0;
        for (Object h : headers) {
            DBFField dbfField = new DBFField();
            dbfField.setName(h == null ? "" : h.toString());
            dbfField.setType(DBFDataType.CHARACTER);
            dbfField.setLength(250);
            fields[i++] = dbfField;
        }
        dbfWriter.setFields(fields);
    }
    private static void _dbfBody(DBFWriter dbfWriter, List<List<Object>> data, String file, String charset){
        int len = data.get(0).size();
        for(List<Object> row:data){
            Object[] r = new Object[len];
            for(int i=0;i<len;i++){
                Object _rc = row.get(i);
                r[i] = _rc != null && !(_rc instanceof String)?_rc.toString():_rc;
            }
            dbfWriter.addRecord(r);
        }
    }
    public static List<List<Object>> readDBF(String file,String charset){
        List<List<Object>> data = new ArrayList<>();
        DBFReader dbfReader = null;
        FileInputStream is = null;
        try{
            is = new FileInputStream(file);
            dbfReader = new DBFReader(is, Charset.forName(charset));
            Object[] row;
            while((row = dbfReader.nextRecord()) != null){
                data.add(Arrays.asList(row));
            }
            return data;
        }catch (Exception e){
            return data;
        }finally {
            if(is != null){
                try {
                    is.close();
                }catch (IOException e){

                }

            }
            if(dbfReader != null){
                dbfReader.close();
            }
        }
    }

    public static List<Map<String,String>> dbfTitle(String file,String charset) throws FileNotFoundException{
        List<Map<String,String>> title = new ArrayList<>();
        DBFReader dbfReader = null;
        FileInputStream is = null;
        try{

            is = new FileInputStream(file);
            dbfReader = new DBFReader(is,Charset.forName(charset));
            int c = dbfReader.getFieldCount();
            while(--c >= 0){
                DBFField field = dbfReader.getField(c);
                Map<String,String> t = new HashMap<>();
                String name = field.getName();
                String type = field.getType().name();
                String len = field.getLength() + "";
                t.put("name",name);
                t.put("type",type);
                t.put("length",len);
                title.add(t);
            }

        }catch (FileNotFoundException e){
            throw new FileNotFoundException(e.toString());
        }finally {
            if(is != null){
                try {
                    is.close();
                }catch (IOException e){}
            }
            if(dbfReader != null){
                dbfReader.close();
            }
            return title;
        }
    }
}

