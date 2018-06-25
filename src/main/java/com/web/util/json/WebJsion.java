package com.web.util.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.tcp.DataParserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

public class WebJsion {
    private static Logger log = LogManager.getLogger(WebJsion.class.getName());


    public static JSONObject tojson(Object... objects) {
        JSONObject jsonObject = new JSONObject();
        for (Object o :objects){
            Field[] fields =  o.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                try {
                    Object val = field.get(o);
                    if(val != null){
                        jsonObject.put(field.getName(),val);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            jsonObject.put(o.getClass().getName(),o);

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
