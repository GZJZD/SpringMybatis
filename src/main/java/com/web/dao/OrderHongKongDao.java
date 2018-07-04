package com.web.dao;

import com.web.datebase.entity.Agent;
import com.web.datebase.entity.Prices;
import com.web.datebase.entity.Users75;
import com.web.datebase.entity.Users76;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface OrderHongKongDao {


    /**
     * 获取商品当前市场价格
     * @param variety 商品代码
     * @return
     */
    @Select("select * from prices where symbol=#{arg1};")
    public  Prices getMarketPrice(String variety);


    /**
     *查询用户姓名
     * @param Table 表名 不同平台用户 表不一样
     * @param userId 用户id
     * WITHDRAWAL 止盈
     * BALANCE 止损
     * @return
     */
    @Select("select LOGIN,REGDATE,AGENT,NAME,DEPOSIT,WITHDRAWAL,BALANCE from users75 where LOGIN=#{arg1};")
    public Users75 getUser75 ( String userId);



    /**
     *查询用户姓名
     * @param Table 表名 不同平台用户 表不一样
     * @param userId 用户id
     * WITHDRAWAL 止盈
     * BALANCE 止损
     * @return
     */
    @Select("select LOGIN,REGDATE,AGENT,NAME,DEPOSIT,WITHDRAWAL,BALANCE from users76 where LOGIN=#{arg1};")
    public Users76 getUser76 ( String userId);



    /**
     *查询用户姓名
     * @param agentId 代理人id
     * WITHDRAWAL 止盈
     * BALANCE 止损
     * @return
     */
    @Select("select agent , agentname from agent where agent =#{arg0}")
    public Agent getAgent (String agentId);
}
