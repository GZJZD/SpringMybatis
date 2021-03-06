package com.web.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by may on 2018/5/9.
 */
public abstract class DoubleUtil {
    public static final Double Double_val = 0.0;  // double 默认值
    private static final int DEF_DIV_SCALE = 10;

    /**
     * * 两个Double数相加 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.add(b2).doubleValue());
    }

    /**
     * * 两个Double数相减 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.subtract(b2).doubleValue());
    }

    /**
     * * 两个Double数相乘 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.multiply(b2).doubleValue());
    }

    /**
     * double 和 int 相乘计算
     */
    public static Double mul(Double v1, Integer v2){
        BigDecimal b1 = new BigDecimal(Integer.toString(v2));
        BigDecimal b2 = new BigDecimal(Double.toString(v1));
        return new Double( b1.multiply(b2).doubleValue());
    }

    /**
     * * 两个Double数相除 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
    }


    /**
     * * 两个Double数相除，并保留scale位小数 *
     *
     * @param v1 *
     * @param v2 *
     * @param scale *
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * Double 类型除以 正数类型
     *     @param v1 *
     *      @param v2 *
     *      scale  保留多少位数
     *  向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
     *  @return Double
     */
     public static Double div(Double v1 ,Integer v2, int scale){
         if (scale < 0) {
             throw new IllegalArgumentException(
                     "The scale must be a positive integer or zero");
         }
         BigDecimal b2 = new BigDecimal(Integer.toString(v2));
         BigDecimal b1 = new BigDecimal(Double.toString(v1));
         return b1.divide(b2,scale,BigDecimal.ROUND_HALF_DOWN ).doubleValue();
     }

    /**
     * Integer 类型除以 Double类型
     *     @param v1 *
     *      @param v2 *
     *      scale  保留多少位数
     *  向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
     *  @return Double
     */
    public static Double div(Integer v1 ,Double v2, int scale){
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b2 = new BigDecimal(Integer.toString(v1));
        BigDecimal b1 = new BigDecimal(Double.toString(v2));
        return b2.divide(b1,scale,BigDecimal.ROUND_HALF_DOWN ).doubleValue();
    }

    /**
     *
     */

}
