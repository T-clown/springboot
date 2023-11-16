package com.springboot.utils;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDateTimeUtil {
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MM = "yyyy-MM";
    /**
     * 格式化日期工具
     */
    public static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance(YYYY_MM_DD_HH_MM_SS);

    private static volatile Map<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();
    private static volatile Map<String, SimpleDateFormat> simpleDateFormatMap = new ConcurrentHashMap<>();

    private static DateTimeFormatter formatter(String pattern) {
        DateTimeFormatter formatter = formatterMap.get(pattern);
        if (formatter == null) {
            formatterMap.put(pattern, formatter = DateTimeFormatter.ofPattern(pattern));
        }
        return formatter;
    }

    private static SimpleDateFormat simpleDateFormat(String pattern) {
        SimpleDateFormat formatter = simpleDateFormatMap.get(pattern);
        if (formatter == null) {
            simpleDateFormatMap.put(pattern, formatter = new SimpleDateFormat(pattern));
        }
        return formatter;
    }

    public static String format(LocalDateTime localDateTime) {
        return formatter(YYYY_MM_DD_HH_MM_SS).format(localDateTime);
    }

    public static String format(Date date) {
        return formatter(YYYY_MM_DD_HH_MM_SS).format(dateToLocateDateTime(date));
    }

    /**
     * 当月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMon(Date date) {
        LocalDateTime firstOfDay = dateToLocateDateTime(date).with(TemporalAdjusters.firstDayOfMonth());
        return localDateTimeToDate(firstOfDay);
    }

    /**
     * 当月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMon(Date date) {
        LocalDateTime endOfDay = dateToLocateDateTime(date).with(TemporalAdjusters.lastDayOfMonth()).with(
            LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime
     * @return
     */
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    private static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return
     */
    private static LocalDateTime dateToLocateDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     * @return
     */
    private static LocalDate dateToLocateDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 获得某天最大时间 yyyy-MM-dd 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
            ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
//        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得某天最小时间 yyyy-MM-dd 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
            ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) throws ParseException {
        //LocalDateTime dateTime = LocalDateTime.now();
        //Date date = new Date();
        //System.out.println(FAST_DATE_FORMAT.format(date));
        //System.out.println(formatter(YYYY_MM_DD_HH_MM_SS).format(dateTime));
        //System.out.println(DateFormatUtils.format(date, YYYY_MM_DD_HH_MM_SS));
       // LocalDate parse = LocalDate.parse("2021-06", DateTimeFormatter.ofPattern(YYYY_MM));
        SimpleDateFormat simpleDateFormat = simpleDateFormat(YYYY_MM);
        Date parse = simpleDateFormat.parse("2021-06");
        Date date = DateUtils.parseDate("2021-06", YYYY_MM);

        System.out.println(date);
        System.out.println(parse);
        System.out.println(getStartOfDay(getFirstDayOfMon(new Date())).getTime());
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAgeByBirth(LocalDate birthDay) {
        LocalDate now = LocalDate.now();
        //出生日期晚于当前时间，无法计算
        if (now.isBefore(birthDay)) {
            throw new IllegalArgumentException(
                "The birthDay is before Now.It's unbelievable!");
        }
        //计算整岁数
        int age = now.getYear() - birthDay.getYear();
        int monthNow = now.getMonthValue();
        int dayOfMonthNow = now.getDayOfMonth();
        int monthBirth = birthDay.getMonthValue();
        int dayOfMonthBirth = birthDay.getDayOfMonth();
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //当前日期在生日之前，年龄减一
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //当前月份在生日之前，年龄减一
                age--;
            }
        }
        return age;
    }

}