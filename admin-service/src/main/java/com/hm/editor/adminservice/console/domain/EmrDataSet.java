package com.hm.editor.adminservice.console.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class EmrDataSet {
  private String _id;
  private String folderName;
  private int orderNumber;
  private Date createDate;
  private Date editDate;
  private List<String> templateNames;
  private String key;
  private Map<String, Integer> page;
  private int currentPage;
  private int pageSize;
  private int totalRecords;
  private String dataSourceName;
  private String description;
  private Map<String, String> dataSourceType;
  private List<Object> elementSet;
  private String folder;
  private List<Object> template;
  private int __v;
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
}
