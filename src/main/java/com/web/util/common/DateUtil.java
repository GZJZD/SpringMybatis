package com.web.util.common;



import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class
DateUtil {
    /**
     * 要用到的DATE Format的定义
     */
    public static String DATE_YYYY_MM_DD = "yyyy-MM-dd"; // 年/月/日
    public static String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
    public static SimpleDateFormat sdfDate_Y_M_D_H_M_S = new SimpleDateFormat(DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
    public static SimpleDateFormat sdfDate_Y_M_D = new SimpleDateFormat(DateUtil.DATE_YYYY_MM_DD);
    public static final SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SHORTDATEFORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat LONG_DATE_FORMAT_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat HMS_FORMAT = new SimpleDateFormat("HH:mm:ss");
    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = sdfDate_Y_M_D_H_M_S;
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = sdfDate_Y_M_D_H_M_S;
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = sdfDate_Y_M_D;
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = sdfDate_Y_M_D_H_M_S;
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = sdfDate_Y_M_D_H_M_S;
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @param
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = sdfDate_Y_M_D;
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = sdfDate_Y_M_D;
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }
    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }
    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = sdfDate_Y_M_D_H_M_S;
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }
    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = sdfDate_Y_M_D_H_M_S;
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }
    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat
     *             yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到某一天的最后一秒钟
     */
    public static Date getEndDate(Date now) {
        if(now==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 1);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE), 0, 0, 0);
        calendar.add(Calendar.SECOND, -1);
        now = calendar.getTime();
        return now;
    }

    /*
    *
    * 将yyyyMMddHHmmss 转成 yyyy-MM-dd HH:ss:mm
    *
    * */
    public static String strToStr(String date){
        date=date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + "  "
                + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);

        return date;
    }


    /**
     * 把毫秒转化成日期
     * @param millSec(毫秒数)
     * @return  String
     */

    public static String longToStrDate(Long millSec){
        SimpleDateFormat sdf = sdfDate_Y_M_D_H_M_S;
        Date date= new Date(millSec);
        return sdf.format(date);

    }
}
