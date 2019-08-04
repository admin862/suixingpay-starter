/**
 * All rights Reserved, Designed By Suixingpay.
 * 
 * @author: matieli<ma_tl@suixingpay.com>
 * @date: 2017年3月7日 下午2:52:34
 * @Copyright ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.takin.util.time;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期工具类
 * 
 * @author: matieli[ma_tl@suixingpay.com]
 * @date: 2017年3月7日 下午2:52:34
 * @version: V1.0
 */
public abstract class DateMoreUtils {
    /**
     * Number of milliseconds in a standard second.
     */
    public static final long MILLIS_PER_SECOND = 1000;

    /**
     * Number of milliseconds in a standard minute.
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

    /**
     * Number of milliseconds in a standard hour.
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    /**
     * Number of milliseconds in a standard day.
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //////// 日期比较 ///////////
    /**
     * 是否同一天.
     * 
     * @see DateUtils#isSameDay(Date, Date)
     */
    public static boolean isSameDay(final Date date1, final Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * 是否同一时刻.
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameTime(final Date date1, final Date date2) {
        // date.getMillisOf() 比date.getTime()快
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断日期是否在范围内，包含相等的日期
     * 
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static boolean isBetween(final Date date, final Date start, final Date end) {
        if (date == null || start == null || end == null || start.after(end)) {
            throw new IllegalArgumentException(
                    "some date parameters is null or dateBein after dateEnd");
        }
        return !date.before(start) && !date.after(end);
    }

    /////////// 日期设置处理 /////////

    /**
     * 日期往下取整. 如 2016-12-10 07:33:23, 如果filed为Calendar.HOUR，则返回2016-12-10 07:00:00
     * 如果filed为Calendar.MONTH，则返回2016-12-01 00:00:00
     * 
     * @param field Calendar.HOUR,Calendar.Date etc...
     */
    public static Date truncate(final Date date, int field) {
        return DateUtils.truncate(date, field);
    }

    /**
     * 日期往上取整. 如 2016-12-10 07:33:23, 如果filed为Calendar.HOUR，则返回2016-12-10 08:00:00 如果filed为
     * Calendar.MONTH，则返回2017-01-01 00:00:00
     * 
     * @param field Calendar.HOUR,Calendar.Date etc...
     */
    public static Date ceiling(final Date date, int field) {
        return DateUtils.ceiling(date, field);
    }

    //////////// 往前往后滚动时间//////////////

    /**
     * 加amount月
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMonths(final Date date, int amount) {
        return DateUtils.addMonths(date, amount);
    }

    /**
     * 减amount月
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date subtractMonths(final Date date, int amount) {
        return DateUtils.addMonths(date, -amount);
    }

    /**
     * 加amount周
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addWeeks(final Date date, int amount) {
        return DateUtils.addWeeks(date, amount);
    }

    /**
     * 减amount周
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date subtractWeeks(final Date date, int amount) {
        return DateUtils.addWeeks(date, -amount);
    }

    /**
     * 加amount天
     */
    public static Date addDays(final Date date, final int amount) {
        return DateUtils.addDays(date, amount);
    }

    /**
     * 减amount天
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date subtractDays(final Date date, int amount) {
        return DateUtils.addDays(date, -amount);
    }

    /**
     * 加amount个小时
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addHours(final Date date, int amount) {
        return DateUtils.addHours(date, amount);
    }

    /**
     * 减amount个小时
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date subtractHours(final Date date, int amount) {
        return DateUtils.addHours(date, -amount);
    }

    /**
     * 加amount分钟
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinutes(final Date date, int amount) {
        return DateUtils.addMinutes(date, amount);
    }

    /**
     * 减amount分钟
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date subtractMinutes(final Date date, int amount) {
        return DateUtils.addMinutes(date, -amount);
    }

    /**
     * 加amount秒.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date addSeconds(final Date date, int amount) {
        return DateUtils.addSeconds(date, amount);
    }

    /**
     * 减amount秒.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date subtractSeconds(final Date date, int amount) {
        return DateUtils.addSeconds(date, -amount);
    }

    //////////// 直接设置时间//////////////

    /**
     * 设置年份, 公元纪年.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date setYears(final Date date, int amount) {
        return DateUtils.setYears(date, amount);
    }

    /**
     * 设置月份, 0-11.
     */
    public static Date setMonths(final Date date, int amount) {
        return DateUtils.setMonths(date, amount);
    }

    /**
     * 设置日期, 1-31.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date setDays(final Date date, int amount) {
        return DateUtils.setDays(date, amount);
    }

    /**
     * 设置小时, 0-23.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date setHours(final Date date, int amount) {
        return DateUtils.setHours(date, amount);
    }

    /**
     * 设置分钟, 0-59.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date setMinutes(final Date date, int amount) {
        return DateUtils.setMinutes(date, amount);
    }

    /**
     * 设置秒.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date setSeconds(final Date date, int amount) {
        return DateUtils.setSeconds(date, amount);
    }

    /**
     * 设置毫秒.
     * 
     * @param date
     * @param amount
     * @return
     */
    public static Date setMilliseconds(final Date date, int amount) {
        return DateUtils.setMilliseconds(date, amount);
    }

    ///// 获取日期的//////
    /**
     * 获得日期是一周的第几天, 返回值为1是Sunday , 2是Monday.... 可通过Canendar的setFirstDayOfWeek()来改变Monday开始为1
     * 
     * @param date
     * @return
     */
    public static int getDayOfWeek(final Date date) {
        return get(date, Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得日期是一年的第几天，返回值从1开始
     * 
     * @param date
     * @return
     */
    public static int getDayOfYear(final Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得日期是一年的第几天，返回值从1开始. 开始的一周，只要有一天在那个月里都算.
     * 
     * @param date
     * @return
     */
    public static int getWeekOfMonth(final Date date) {
        return get(date, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得日期是一年的第几周，返回值从1开始. 开始的一周，只要有一天在那一年里都算.
     * 
     * @param date
     * @return
     */
    public static int getWeekOfYear(final Date date) {
        return get(date, Calendar.WEEK_OF_YEAR);
    }

    /**
     * get
     * 
     * @param date
     * @param field
     * @return
     */
    private static int get(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    ////// 闰年及每月天数///////
    /**
     * 是否闰年.
     * 
     * @param date
     * @return
     */
    public static boolean isLeapYear(final Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }

    /**
     * 是否闰年，移植Jodd Core的TimeUtil
     * 
     * @param y 公元计数, 如2016
     * @return true or false
     */
    public static boolean isLeapYear(int y) {
        boolean result = false;

        if (((y % 4) == 0) && // must be divisible by 4...
                ((y < 1582) || // and either before reform year...
                        ((y % 100) != 0) || // or not a century...
                        ((y % 400) == 0))) { // or a multiple of 400...
            result = true; // for leap year.
        }
        return result;
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     * 
     * @param date 日期 主要取年和月
     * @return int 天数
     */
    public static int getMonthLength(final Date date) {
        int year = get(date, Calendar.YEAR);
        int month = get(date, Calendar.MONTH);
        return getMonthLength(year, month);
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     * 
     * @param year 年
     * @param month 月
     * @return int 天数
     */
    public static int getMonthLength(int year, int month) {

        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return MONTH_LENGTH[month];
    }
}
