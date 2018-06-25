package com.web.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.tcp.DataParserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class WebJsion {
    private static Logger log = LogManager.getLogger(WebJsion.class.getName());




    public static JSONObject tojson(Object... objects) {
        JSONObject jsonObject = new JSONObject();
        for (Object obj : objects) {
            /* 得到类中的所有属性集合 */
            Field[] fs = obj.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true); // 设置些属性是可以访问的
                try {
                    Object val = f.get(obj);
                    // 得到此属性的值
                    if(val!=null){
                        jsonObject.put(f.getName(),val);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }




    /**
     * 把json string 转化成类对象
     *
     * @param str
     * @param t
     * @return
     */
    public static <T> T parseObject(String str, Class<T> t) {
        try {
            if (str != null && !"".equals(str.trim())) {
                T res = JSONArray.parseObject(str.trim(), t);
                return res;
            }
        } catch (Exception e) {

             log.info("数据转换出错:"+ e.getMessage());
        }
        return null;
    }

    /**
     * 把json string 转化成类对象
     *
     * @param str
     * @param t
     * @return
     */
    public static <T> List<T> parseArray(String str, Class<T> t) {
        try {
            if (str != null && !"".equals(str.trim())) {
                List<T> res = JSONArray.parseArray(str.trim(), t);
                return res;
            }
        } catch (Exception e) {
             log.info("数据转换出错:"+ e.getMessage());
        }
        return null;
    }

    /**
     * 把类对象转化成json string
     *
     * @param t
     * @return
     */
    public static <T> String toJson(T t) {
        try {
            return JSONObject.toJSONString(t);
        } catch (Exception e) {
             log.info("数据转换出错:"+ e.getMessage());

        }
        return "";
    }
}
