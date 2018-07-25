package com.web.dao;


import com.web.pojo.FollowOrderClient;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowOrderClientDao {
  /*
   *
   *   通过跟单id，删除对应的对象
   * @author may
   * @date 2018/6/4 16:56
   * @param
   * @return
   */
    int deleteByFollowOrderId(Long followOrderId);

    int insert(FollowOrderClient record);


    int updateByPrimaryKey(FollowOrderClient record);
    /**通过客户的编号找到关联的跟单id
     *@Author: May
     *@Date: 10:39 2018/5/21
     * @param userCode
     * @param platformCode
     */
    List<Long> findListFollowOrderIDsByUserCode(@Param("userCode") String userCode, @Param("platformCode") String platformCode);

    List<FollowOrderClient> getListByFollowOrderId(@Param("followOrderId") Long followOrderId, @Param("NODeleteOrAllStatus") Integer NODeleteOrAllStatus);


    FollowOrderClient getByUserCodeAndPlatformCode(@Param("userCode") String userCode, @Param("platformCode") String platformCode, @Param("followOrderId") Long followOrderId);


    FollowOrderClient getFollowOrderClient(Long followOrderClientId);


   List<FollowOrderClient> getALLByUserCodeAndPlatformCode(@Param("userCode") String userCode, @Param("platformCode") String platformCode, @Param("followOrderId") Long followOrderId);
}