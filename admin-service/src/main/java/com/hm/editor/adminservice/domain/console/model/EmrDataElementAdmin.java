package com.hm.editor.adminservice.domain.console.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "emrDataElementAdmin")
public class EmrDataElementAdmin {

    @Id
    private String _id;
    private String folderName;
    private String orderNumber;
    private String createDate;
    private String editDate;
    private List<String> templateNames;
    private String templateName;
    private String key;
    private Map<String, Integer> page;
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalRecords;
    private String dataSourceName;
    private String description;
    private Map<String, String> dataSourceType;
    private String elementSet;
    private String folder;
    private String template;
    private Integer __v;
    private String isdisabled;
    private String autoshowcurtime;
    private String enterAutoGrow;
    private String printBorder;
    private String printColor;
    private String printUnderline;
    private String printMinWidth;
    private String printReplacement;
    private String searchPair;
    private String searchOption;
    private String placeholder;
    private String typeItems;
    private String typeCode;

    // Getter and Setter methods (manually added due to Lombok compatibility issues with JDK 25)
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeItems() {
        return typeItems;
    }

    public void setTypeItems(String typeItems) {
        this.typeItems = typeItems;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public String getSearchPair() {
        return searchPair;
    }

    public void setSearchPair(String searchPair) {
        this.searchPair = searchPair;
    }

    public List<Object> toList() {
        List<Object> ls = new ArrayList<>();
        ls.add(dataSourceName);
        ls.add(description);
        ls.add(codeToName(typeCode));
        ls.add(typeItems);
        ls.add(placeholder);
        ls.add(searchOption);
        ls.add(searchPair);
        ls.add(createDate);
        return ls;
    }

    public static List<Object> excelHeader() {
        String[] s = {
            "数据元名称",
            "数据元定义",
            "类型",
            "值域",
            "占位文本",
            "搜索类型",
            "搜索编码",
            "创建时间",
        };
        List<Object> result = new ArrayList<>();
        for (String item : s) {
            result.add(item);
        }
        return result;
    }

    public static String codeToName(String code) {
        String name = "";
        switch (code) {
            case "textbox":
                name = "文本输入框";
                break;
            case "newtextbox":
                name = "新文本输入框";
                break;
            case "textboxwidget":
                name = "文本控件";
                break;
            case "radiobox":
                name = "单选";
                break;
            case "checkbox":
                name = "多选";
                break;
            case "dropbox":
                name = "下拉菜单";
                break;
            case "cellbox":
                name = "单元";
                break;
            case "searchbox":
                name = "搜索";
                break;
            case "time":
                name = "时间";
                break;
            case "date":
                name = "日期";
                break;
            case "month_day":
                name = "月/日";
                break;
            case "datetime":
                name = "日期 时间";
                break;
            case "numbox":
                name = "数字控件";
                break;
            case "button":
                name = "按钮";
                break;
        }
        return name;
    }

    public static String nameToCode(String name) {
        String code = "";
        switch (name) {
            case "文本输入框":
                code = "textbox";
                break;
            case "新文本输入框":
                code = "newtextbox";
                break;
            case "文本控件":
                code = "textboxwidget";
                break;
            case "单选":
                code = "radiobox";
                break;
            case "多选":
                code = "checkbox";
                break;
            case "下拉菜单":
                code = "dropbox";
                break;
            case "单元":
                code = "cellbox";
                break;
            case "搜索":
                code = "searchbox";
                break;
            case "时间":
                code = "time";
                break;
            case "日期":
                code = "date";
                break;
            case "月/日":
                code = "month_day";
                break;
            case "日期 时间":
                code = "datetime";
                break;
            case "数字控件":
            case "时:分:秒":
                code = "numbox";
                break;
            case "年-月-日 时:分:秒":
                code = "fullDateTime";
                break;
            case "yyyy年MM月dd日":
                code = "date_han";
                break;
            case "yyyy年MM月dd日HH时mm分":
                code = "datetime_han";
                break;
            case "按钮":
                code = "button";
                break;
        }
        return code;
    }
}
