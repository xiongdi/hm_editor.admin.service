package com.hm.editor.adminservice.console.service;

import static com.hm.editor.common.utils.DateUtil.parseUTC;

import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.adminservice.console.repository.DataElementRepository;
import com.hm.editor.adminservice.console.utils.FileUtils;
import com.hm.editor.common.utils.FileUtil;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class DataElementService {

    @Autowired
    private DataElementRepository dataElementRepository;

    public Map<String, Object> getAll(Map<String, Object> param) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> page = (Map<String, Object>) param.get("page");
        Long total = dataElementRepository.getTotal(param);
        page.put("totalRecords", total);
        List<EmrDataElementAdmin> data = dataElementRepository.getAll(param);
        res.put("page", page);
        res.put("dataList", data);
        return res;
    }

    public boolean delDataElenment(String param) {
        return dataElementRepository.delDataElement(param);
    }

    public boolean updDataElenment(Map<String, Object> param) {
        String dataSourceName = String.valueOf(param.get("dataSourceName"));
        String description = String.valueOf(param.get("description"));
        Map<String, String> dataSourceType = (Map<String, String>) param.get("dataSourceType");
        String name = dataSourceType.get("name");
        List<Map> list = dataElementRepository.searchDataElement(
            dataSourceName,
            param.get("templateName") + ""
        );
        boolean flag = false;
        List<String> deList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String oldDescription = String.valueOf(map.get("description"));
            deList.add(oldDescription);

            String oldName = map.get("typeCode").toString();
            nameList.add(oldName);
        }
        if (!deList.contains(description) || !nameList.contains(name)) {
            flag = dataElementRepository.updDataElement(param);
        }
        return flag;
    }

    public boolean intDataElenment(Map<String, Object> param) {
        String dataSourceName = String.valueOf(param.get("dataSourceName"));
        String description = String.valueOf(param.get("description"));
        Map<String, String> dataSourceType = (Map<String, String>) param.get("dataSourceType");
        String dataSourceTypeCode = dataSourceType.get("code");
        List<Map> list = dataElementRepository.searchDataElement(
            dataSourceName,
            param.get("templateName") + ""
        );
        boolean flag = false;
        List<String> deList = new ArrayList<>();
        List<Object> nameList = new ArrayList<>();
        List<Object> nameListCode = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String oldDescription = String.valueOf(map.get("description"));
            deList.add(oldDescription);
            Map<String, String> oldDataSourceType = (Map<String, String>) map.get("dataSourceType");
            if (oldDataSourceType != null) {
                nameList.add(oldDataSourceType);
            } else {
                String oldDataSourceTypeCode = String.valueOf(map.get("typeCode"));
                nameListCode.add(oldDataSourceTypeCode);
            }
        }
        if (
            !deList.contains(description) ||
            (!nameList.contains(dataSourceType) && !nameListCode.contains(dataSourceTypeCode))
        ) {
            flag = dataElementRepository.intDataElement(param);
        }
        return flag;
    }

    public void export(HttpServletResponse response) {
        List<EmrDataElementAdmin> data = dataElementRepository.getAllData();

        List<List<Object>> _d = new ArrayList<>();
        _d.add(EmrDataElementAdmin.excelHeader());
        for (EmrDataElementAdmin d : data) {
            if (d.getCreateDate().endsWith("Z")) {
                d.setCreateDate(parseUTC(d.getCreateDate()));
            }
            _d.add(d.toList());
        }
        String path = "/tmp/datasource.xlsx";
        FileUtil.writeExcel(_d, path, true);
        downFile(response, path);
        FileUtil.deleteFile(path);
    }

    private void downFile(HttpServletResponse response, String path) {
        File file = new File(path);
        try (
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream buffer = new BufferedInputStream(fis)
        ) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=" +
                    new String(
                        file.getName().getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8
                    )
            );
            response.setContentLength((int) file.length());
            // 定义输出类型
            response.setContentType("application/octet-stream");
            // 相当于我们的缓存
            byte[] b = new byte[1024 * 2];
            // 该值用于计算当前实际下载了多少字节
            long k = 0;
            // 从response对象中得到输出流,准备下载
            OutputStream myout = response.getOutputStream();
            // 开始循环下载
            while (k < file.length()) {
                int j = buffer.read(b, 0, 1024);
                k += j;
                myout.write(b, 0, j);
            }
            myout.flush();
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }
    }

    public boolean upload(MultipartFile file) {
        String path = "/tmp/";
        String filename = file.getOriginalFilename();
        try {
            FileUtils.uploadFile(file.getBytes(), path, filename);
            List<List<Map<String, Object>>> res = FileUtil.readExcel2Map(path + filename);

            System.out.println(res);
            if (res != null && !res.isEmpty()) {
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                List<EmrDataElementAdmin> insterData = new ArrayList<>();
                for (List<Map<String, Object>> lm : res) {
                    for (Map<String, Object> m : lm) {
                        String dataSourceName =
                            m.get("数据元名称") == null ? "" : m.get("数据元名称").toString();
                        String description =
                            m.get("数据元定义") == null ? "" : m.get("数据元定义").toString();
                        String typeCode = m.get("类型") == null ? "" : m.get("类型").toString();
                        typeCode = EmrDataElementAdmin.nameToCode(typeCode);

                        String typeItems = m.get("值域") == null ? "" : m.get("值域").toString();
                        String placeholder =
                            m.get("占位文本") == null ? "" : m.get("占位文本").toString();
                        String searchOption =
                            m.get("搜索类型") == null ? "" : m.get("搜索类型").toString();
                        String searchPair =
                            m.get("搜索编码") == null ? "" : m.get("搜索编码").toString();
                        String createDate = now;
                        if (dataSourceName.trim().isEmpty()) {
                            continue;
                        }
                        EmrDataElementAdmin e = new EmrDataElementAdmin();
                        e.setDataSourceName(dataSourceName);
                        e.setDescription(description);
                        e.setTypeItems(typeItems);
                        e.setTypeCode(typeCode);
                        e.setPlaceholder(placeholder);
                        e.setSearchOption(searchOption);
                        e.setSearchPair(searchPair);
                        e.setCreateDate(createDate);
                        insterData.add(e);
                    }
                }
                if (!insterData.isEmpty()) {
                    dataElementRepository.inserts(insterData);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("upload excel error:" + e);
            return false;
        }
    }

    public boolean autoBind(Map<String, Object> templateDataSource) {
        dataElementRepository.autoBind(templateDataSource);
        return true;
    }
}
