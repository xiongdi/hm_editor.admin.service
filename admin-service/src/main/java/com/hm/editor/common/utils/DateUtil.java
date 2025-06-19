package com.hm.editor.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 2018/1/26.
 */
public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    public static final String defaultStr="yyyy-MM-dd HH:mm:ss";

    public static Date trim(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

        return calendar.getTime();
    }

    public static Date AddDay(Date date,int num){
        Calendar c = Calendar.getInstance();
        c.setTime(date); // Now use today date.
        c.add(Calendar.DATE, num);
        return c.getTime();
    }

    public static Date getDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        return  date;
    }

    public static Date getDateAfter(Date date, int num, String unit){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (null != unit) {
            if ("天".equals(unit)) {
                c.add(Calendar.DATE, num);
            } else if ("小时".equals(unit)) {
                c.add(Calendar.HOUR, num);
            } else if ("分钟".equals(unit)) {
                c.add(Calendar.MINUTE, num);
            } else if ("秒".equals(unit)) {
                c.add(Calendar.SECOND, num);
            }
        }
        return c.getTime();
    }

    public static Date dateFormat(String dateStr, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.error(e.toString());
            return null;
        }
    }

    public static LocalDate DateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date getSevenDaysAgo() {
        LocalDate _today = DateToLocalDate(new Date());
        LocalDate _sevendaysAgo = _today.minus(1, ChronoUnit.WEEKS);
        return localDateToDate(_sevendaysAgo);
    }

    public static Date getSomeDaysAgo(int days) {
        LocalDate _today = DateToLocalDate(new Date());
        LocalDate _someDaysAgo = _today.minus(days, ChronoUnit.DAYS);
        return localDateToDate(_someDaysAgo);
    }

    public static Date getDayBefore(Date now,int day) {
        LocalDate _today = DateToLocalDate(now);
        LocalDate _someDaysAgo = _today.minus(day, ChronoUnit.DAYS);
        return localDateToDate(_someDaysAgo);
    }
    /**
     * <li>功能描述：时间相减得到自然日天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
            LOGGER.info("相隔的天数=" + day);
        } catch (ParseException e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 功能描述：时间相减得计算得到工作日天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static long getWorkDaySub(String beginDateStr, String endDateStr ){
        long workDay = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginDateTime = LocalDate.parse(beginDateStr,formatter); //获取开始时间
        LocalDate endDateTime = LocalDate.parse(endDateStr,formatter);//获取结束时间
        // 循环 直到当前日期在给定日期之前
        while (endDateTime.isAfter(beginDateTime) || endDateTime.equals(beginDateTime)) {
            if (isWorkday(endDateTime.getDayOfWeek())) {
                workDay++;
            }
            endDateTime = endDateTime.minusDays(1);  // 当前日期减去一天
        }
        return workDay-1;
    }

    /**
     * //校验当前日期是否为工作日
     * @param dayOfWeek
     * @return
     */
    private static boolean isWorkday(DayOfWeek dayOfWeek) {
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    public static Date string2Date(String str, String dateFormat) {
        try{
            SimpleDateFormat sdf= new SimpleDateFormat(dateFormat);
            return sdf.parse(stringDateYear(str));
        }catch(ParseException e){
            LOGGER.error(e.toString());
            return null;
        }
    }
    public static Date newstring2Date(String str, String dateFormat) {
        try{
            SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf1.parse(stringDateYear(str));
        }catch(Exception e){
            LOGGER.error(str + " fromat to "+dateFormat+" error:"+e);
            return null;
        }
    }
    public static String stringDateYear(String str){
        if(str.indexOf("年")>-1){
            str=str.replace("年","-");
        }
        if(str.indexOf("月")>-1){
            str=str.replace("月","-");
        }
        if(str.indexOf("日")>-1){
            str=str.replace("日"," ");
        }
        if(str.indexOf("时")>-1){
            str=str.replace("时",":");
        }
        if(str.indexOf("分")>-1){
            str=str.replace("分",":");
        }
        if(str.indexOf("秒")>-1){
            str=str.replace("秒","");
        }
        if(" ".equals(str.substring(str.length()-1)) || ":".equals(str.substring(str.length()-1)) || "-".equals(str.substring(str.length()-1))){
            str=str.substring(0,str.length()-1);
        }
        return DateUtil.formatDateStr(str);
    }

    /**
     * 时间间隔
     * @param start
     * @param end
     * @return
     */
    public static String period(Date start,Date end){

        if(start.getTime() >= end.getTime()){
            return "";
        }

        long day = 1000 * 60*60*24;
        boolean flag = end.getTime() % day < start.getTime() % day;

        return periodDay(flag?AddDay(start,1):start,end)+periodOther(start,end);
    }

    /**
     * 间隔天数
     * @return
     */
    public static String periodDay(Date start,Date end){

        LocalDate s = transDate(start);
        LocalDate e = transDate(end);
        Period p = Period.between(s,e);
        int year = p.getYears();
        int m = p.getMonths();
        int d = p.getDays();
        return (year > 0?year+"年":"")+(m > 0?m+"月":"")+(d > 0?d+"天":"");
    }

    /**
     * 计算到分钟
     * @param start
     * @param end
     * @return
     */
    public static String periodOther(Date start,Date end){
        // 间隔分钟
        long _min = ((end.getTime() - start.getTime()) % (24*60*60*1000)) / (60 * 1000);
        long h = _min / 60;
        long m = _min % 60;
        return (h > 0 ? h+"小时":"")+(m> 0?m+"分钟":"");
    }

    private static LocalDate transDate(Date d){
        return LocalDateTime.ofInstant(d.toInstant(),ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime dataTolocaTime(Date d){
        return LocalDateTime.ofInstant(d.toInstant(),ZoneId.systemDefault());
    }

    public static Long getTimeStampByString(String format, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String defaultFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.defaultStr);
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String formatToString(String pattern,Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static Date setDateTime(Date date, int hour, int minute, int second, int milliSecond) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, hour);
        instance.set(Calendar.MINUTE, minute);
        instance.set(Calendar.SECOND, second);
        instance.set(Calendar.MILLISECOND, milliSecond);
        return instance.getTime();
    }

    public static Date getDateByTimeStamp(long timeStamp){
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeStamp);
        return instance.getTime();
    }

    public static String getHourAndSecond(String dateString) {
        Date date = dateFormat(dateString, "yyyy-MM-dd HH:mm");
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        String hourStr = Integer.toString(hour);
        if (hourStr.length() < 2) {
            hourStr = "0" + hourStr;
        }
        int minute = instance.get(Calendar.MINUTE);
        String minuteString = Integer.toString(minute);
        if (minuteString.length() < 2) {
            minuteString = "0" + minuteString;
        }
        return hourStr + ":" + minuteString;
    }

    public static String getHour(String dateString) {
        Date date = dateFormat(dateString, "yyyy-MM-dd HH:mm");
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        return Integer.toString(hour);
    }

    public static Date addHours(Date date, int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.HOUR_OF_DAY, i);
        return instance.getTime();
    }

    public static String getDayInYear(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    /**
      * <p> 返回格式: yyyy-MM-dd HH:mm:ss</p>
      *
      */
    public static String formatDateStr(String dateStr){
        if(StringUtils.isBlank(dateStr)) return "";
        String ds[] = dateStr.split("\\D+");
        int len = ds.length;
        int index = 0;
        String[] splitChar = new String[]{"-","-"," ",":",":",""};
        StringBuffer stringBuffer = new StringBuffer();
        do{
            String temp = (index == 1 || index == 2)?"01":"00";
            if(index < len){
                temp = ds[index];
                if(temp.length() == 1){
                    stringBuffer.append("0");
                }
            }
            stringBuffer.append(temp);
            stringBuffer.append(splitChar[index]);
        }while (++index < 6);
        return stringBuffer.toString();
    }

    public static Date parseString2Date(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = null;
        if (StringUtils.isNotEmpty(dateStr)) {
            switch (dateStr.length()) {
                case 19:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
                case 16:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    break;
                case 13:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
                    break;
                case 10:
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    break;
            }
            return dateFormat.parse(dateStr);
        }
        return null;
    }

    public static String date2String(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    // 精确到天时间
    public static Date getDayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }

    public static String parseUTC(String utcDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcDate = utcDate.replace("Z", " UTC"); //注意UTC前有空格
        try {
            Date date = utcFormat.parse(utcDate);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<String> getContinuesDate(Date start, Date end) {
        Set<String> dates = new HashSet<>();
        Calendar c1 = Calendar.getInstance();
        Date startDate = DateUtil.setDateTime(start, 0, 0, 0, 0);
        c1.setTime(startDate);
        Calendar c2 = Calendar.getInstance();
        Date endDate = DateUtil.setDateTime(end, 0, 0, 0, 0);
        c2.setTime(endDate);
        while (!c1.after(c2)) {
            dates.add(date2String(c1.getTime(), "yyyy-MM-dd"));
            c1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     *
     * @param start
     * @param end
     * @return
     */
    public static int getAllDay(Date start, Date end) {
        if (start == null) {
            LOGGER.error("计算天数的开始时间为空");
            start = new Date();
        }
        if (end == null) {
            LOGGER.error("计算天数的结束时间为空");
            end = new Date();
        }
        double days = (end.getTime() - start.getTime()) / (86400.0 * 1000);
        if (days == 0) {
            return 1;
        }
        return (int) days;
    }
}
