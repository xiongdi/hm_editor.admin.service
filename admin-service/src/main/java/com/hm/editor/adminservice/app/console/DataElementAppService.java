package com.hm.editor.adminservice.app.console;

import static com.hm.editor.adminservice.infrastructure.utils.DateUtil.parseUTC;

import com.hm.editor.adminservice.domain.console.gateway.DataElementGateway;
import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import com.hm.editor.adminservice.domain.console.utils.FileUtils;
import com.hm.editor.adminservice.infrastructure.utils.FileUtil;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 数据元应用服务
 */
@Service
public class DataElementAppService {

    private static final Logger log = LoggerFactory.getLogger(DataElementAppService.class);

    @Autowired
    private DataElementGateway dataElementGateway;

    public Map<String, Object> getAll(Map<String, Object> param) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> page = (Map<String, Object>) param.get("page");
        Long total = dataElementGateway.count(param);
        page.put("totalRecords", total);
        List<EmrDataElementAdmin> data = dataElementGateway.findAll(param);
        res.put("page", page);
        res.put("dataList", data);
        return res;
    }

    public boolean delDataElenment(String param) {
        return dataElementGateway.deleteById(param);
    }

    public boolean updDataElenment(Map<String, Object> param) {
        String dataSourceName = String.valueOf(param.get("dataSourceName"));
        String description = String.valueOf(param.get("description"));
        Map<String, String> dataSourceType = (Map<String, String>) param.get("dataSourceType");
        String name = dataSourceType.get("name");
        List<Map<String, Object>> list = dataElementGateway.searchDataElement(
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
            flag = dataElementGateway.update(param);
        }
        return flag;
    }

    public boolean intDataElenment(Map<String, Object> param) {
        String dataSourceName = String.valueOf(param.get("dataSourceName"));
        String description = String.valueOf(param.get("description"));
        Map<String, String> dataSourceType = (Map<String, String>) param.get("dataSourceType");
        String dataSourceTypeCode = dataSourceType.get("code");
        List<Map<String, Object>> list = dataElementGateway.searchDataElement(
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
            flag = dataElementGateway.insert(param);
        }
        return flag;
    }

    public void export(HttpServletResponse response) {
        List<EmrDataElementAdmin> data = dataElementGateway.getAllData();

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
            response.setContentType("application/octet-stream");
            byte[] b = new byte[1024 * 2];
            long k = 0;
            OutputStream myout = response.getOutputStream();
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
                    dataElementGateway.batchInsert(insterData);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("upload excel error:" + e);
            return false;
        }
    }

    public boolean autoBind(Map<String, Object> templateDataSource) {
        dataElementGateway.autoBind(templateDataSource);
        return true;
    }
}
