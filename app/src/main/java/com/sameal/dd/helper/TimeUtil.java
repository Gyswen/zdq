package com.sameal.dd.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangj
 * @date 2020/12/22 20:43
 * desc 时间工具类：时间戳转化为时间  比较时间相差多少
 * 修改SimpleDateFormat里的格式，可转换成相应格式时间
 */
public class TimeUtil {
    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getTime() {
        return sf.format(new Date());
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    private static Calendar cd = Calendar.getInstance();

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        return cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static int getHour() {
        return cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static int getMinute() {
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @return
     */
    public static int getSecond() {
        return cd.get(Calendar.SECOND);
    }

    /**
     * 时间戳转时间
     *
     * @param milSecond
     * @return
     */
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Calendar calendar, String strDay) {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11) {
            tempStr = "下午" + " " + strDay;
        } else {
            tempStr = "上午" + " " + strDay;
        }
        return tempStr;
    }

    /**
     * 时间转化为星期
     *
     * @param indexOfWeek 星期的第几天
     */
    public static String getWeekDayStr(int indexOfWeek) {
        String weekDayStr = "";
        switch (indexOfWeek) {
            case 1:
                weekDayStr = "星期日";
                break;
            case 2:
                weekDayStr = "星期一";
                break;
            case 3:
                weekDayStr = "星期二";
                break;
            case 4:
                weekDayStr = "星期三";
                break;
            case 5:
                weekDayStr = "星期四";
                break;
            case 6:
                weekDayStr = "星期五";
                break;
            case 7:
                weekDayStr = "星期六";
                break;
        }
        return weekDayStr;
    }

    /**
     * 时间转化为显示字符串，24小时内显示昨天 + 时间，一周内显示周几
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0)
            return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            return "昨天";
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            if (calendar.before(inputTime)) {
                return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                int year = inputTime.get(Calendar.YEAR);
                int month = inputTime.get(Calendar.MONTH) + 1;
                int day = inputTime.get(Calendar.DAY_OF_MONTH);
                return year + "-" + month + "-" + day;
            }
        }
    }


    /**
     * 对比当前时间，转换时间戳
     *
     * @param cc_time 需要转换的时间戳
     * @return 返回的时间格式：获取时间年份与当前年份一致，返回 MM-dd HH:mm；否则返回yyyy-MM-dd HH:mm
     */
    public static String timeCompareYMDHMinSFigure(long cc_time) {
        Calendar now = Calendar.getInstance();
        String year = now.get(Calendar.YEAR) + "";
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        re_StrTime = sdf.format(new Date(cc_time));
        String years = re_StrTime.substring(0, 4);
        if (!year.equals(years)) {
            return re_StrTime;
        } else {
            return re_StrTime.substring(5, re_StrTime.length());
        }
    }

    /**
     * 时间格式转换
     *
     * @param time 需要转换的时间戳
     * @return 返回时间格式  yyyy年MM月dd日
     */
    public static String timeYMDChinese(long time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        re_StrTime = sdf.format(new Date(time));
        return re_StrTime;
    }


    /**
     * 时间格式转换
     *
     * @param time 需要转换的时间戳
     * @return 返回时间格式  yyyy-MM-dd  HH:mm:ss
     */
    public static String timeYMDHMinSFigure(long time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        re_StrTime = sdf.format(new Date(time));
        return re_StrTime;
    }

    /**
     * 时间格式转换
     *
     * @param time 需要转换的时间戳
     * @return 返回时间格式  yyyy-MM-dd
     */
    public static String timeYMDFigure(long time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        re_StrTime = sdf.format(new Date(time));
        return re_StrTime;

    }

    /**
     * 毫秒 转化为 天 时 分 秒 毫秒
     *
     * @param ms 毫秒数
     * @return dd天HH时mm秒xx毫秒
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分钟");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }

    /**
     * 时间比较
     *
     * @param startTime 起始时间
     * @param endTime   终止时间
     * @param format    转换的时间格式，如：yyyy-MM-dd
     * @return 返回两个时间相差多少天
     */
    public static long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            // Log.d("TimeUTIL", "时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
            if (day >= 1) {
                return day;
            } else {
                if (day == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期加🗡天数
     *
     * @param currentdate
     * @return
     */
    public static String currentDateNext(String currentdate) {
        String next = null;
        int year = 0;
        int month = 0;
        int day = 0;
        Calendar tempCal = Calendar.getInstance();
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat todayFrofat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            tempCal.setTime(todayFrofat.parse(currentdate));
            year = tempCal.get(Calendar.YEAR);
            month = tempCal.get(Calendar.MONTH) + 1;
            day = tempCal.get(Calendar.DAY_OF_MONTH);
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10) {
                if (day < 31) {
                    day = day + 1;
                } else if (day == 31) {
                    day = 1;
                    month = month + 1;
                }
            } else if (month == 2) {
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    if (day < 29) {
                        day = day + 1;
                    } else if (day == 29) {
                        month = month + 1;
                        day = 1;
                    }
                } else {
                    if (day < 28) {
                        day = day + 1;
                    } else if (day == 28) {
                        month = month + 1;
                        day = 1;
                    }
                }
            } else if (month == 12) {
                if (day < 31) {
                    day = day + 1;
                } else if (day == 31) {
                    month = 1;
                    day = 1;
                    year = year + 1;
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11
            ) {
                if (day < 30) {
                    day = day + 1;
                } else if (day == 30) {
                    day = 1;
                    month = month + 1;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        next = FormatCurrentDate(year, month, day);
        return next;
    }

    /**
     * 减日期
     *
     * @param currentdate
     * @return
     */
    public static String currentDatePre(String currentdate) {
        String pre = null;
        int year = 0;
        int month = 0;
        int day = 0;
        Calendar tempCal = Calendar.getInstance();
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat todayFrofat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tempCal.setTime(todayFrofat.parse(currentdate));
            year = tempCal.get(Calendar.YEAR);
            month = tempCal.get(Calendar.MONTH) + 1;
            day = tempCal.get(Calendar.DAY_OF_MONTH);
            if (day > 1) {
                day = day - 1;
            } else if (day == 1) {
                month = month - 1;
                if (month == 1 || month == 3 || month == 5 || month == 7
                        || month == 8 || month == 10 || month == 12) {
                    day = 31;
                } else if (month == 4 || month == 6 || month == 9
                        || month == 11) {
                    day = 30;
                } else if (month == 2) {
                    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                } else if (month < 1) {
                    day = 31;
                    month = 12;
                    year = year - 1;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pre = FormatCurrentDate(year, month, day);

        return pre;
    }

    /**
     * 格式化日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String FormatCurrentDate(int year, int month, int day) {
        String format = null;
        if (month < 10) {
            if (day < 10) {
                format = year + "-0" + month + "-0" + day;
            } else {
                format = year + "-0" + month + "-" + day;
            }
        } else {
            if (day < 10) {
                format = year + "-" + month + "-0" + day;
            } else {
                format = year + "-" + month + "-" + day;
            }
        }
        return format;
    }

    public static Long StringToDate(String date, String pattern) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern);
        Date date1 = null;
        try {
            date1 = simpleDateFormat1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1 != null) {
            return date1.getTime();
        } else {
            return 0l;
        }
    }
}
