package com.hm.editor.common.utils;

import java.util.Calendar;
import java.util.Date;

/** Created by Wuxiang on 2017/11/11. */
public class AgeUtil {

  private String ageNumber; // 与unit配合表示年龄，只显示岁或者月或者天

  private String ageUnit;

  private String ageCount; // 大于3年的显示*岁，大于一年的显示*岁*月，小于一岁大于一个月的显示*月*天，小于一个月的显示*天

  private String getAgeNumber() {
    return ageNumber;
  }

  private String getAgeUnit() {
    return ageUnit;
  }

  public String getAgeCount() {
    return ageCount;
  }

  public void chargeAge(Date birthDay) {
    Calendar cal2 = Calendar.getInstance();
    this.chargeAge(birthDay, cal2);
  }

  public void chargeAge(Date birthDay, Calendar cal2) {
    // ----------------获取当前年、月、日----------------
    int yearNow = cal2.get(Calendar.YEAR);
    int monthNow = cal2.get(Calendar.MONTH) + 1;
    int dayOfMonthNow = cal2.get(Calendar.DAY_OF_MONTH);

    Calendar cal = Calendar.getInstance();
    cal.setTime(birthDay);
    int yearBirth = cal.get(Calendar.YEAR);
    int monthBirth = cal.get(Calendar.MONTH) + 1;
    int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

    int day = dayOfMonthNow - dayOfMonthBirth;
    int month = monthNow - monthBirth;
    int age = yearNow - yearBirth;

    if (day < 0) {
      month -= 1;
      cal2.add(Calendar.MONTH, -1);
      day = day + cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    if (month < 0) {
      month = (month + 12) % 12;
      age--;
    }

    if (age >= 3) {
      ageCount = age + "岁";
    } else if (age >= 1 && age < 3 && month >= 0) {
      ageCount = age + "岁" + (month > 0 ? month + "月" : "");
    } else if (age == 0 && month > 0 && day >= 0) {
      ageCount = month + "月" + (day > 0 ? day + "天" : "");
    } else if (age == 0 && month == 0 && day >= 0) {
      ageCount = day + "天";
    } else if (age < 0) {
      ageCount = 0 + "天";
    }
  }
}
