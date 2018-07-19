package wang.xing.user.test;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.orderuser.OrderUserListVo;
import com.web.pojo.vo.orderuser.OrderUserVo;
import com.web.service.OrderUserService;
import com.web.service.TestService;
import com.web.util.sms.AliSms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-mvc.xml","classpath:spring-mybatis.xml"})
public class DemoTest {

    @Autowired
    @Qualifier("testService")
    private TestService testService;
    @Autowired
    private OrderUserService orderUserService;




    public void TestPage(){
       List<OrderUser> orderUserList = orderUserService.findAll();
        System.out.println(orderUserList.size());
    }

    //测试新开仓

    public void addOrderUser(){
         DataSource dataSource = new DataSource();
         dataSource.setLogin("123");
         dataSource.setTicket("110");
         dataSource.setNewTicket("110");
         dataSource.setPrice(200.00);
         dataSource.setVarietyCode("黄金");
         dataSource.setCmd(1);//多空
         dataSource.setHandNumber(11111.1111);
         dataSource.setPrice(100.00);//价位
         dataSource.setCreateTime("2018-11-22");
         dataSource.setOpenClose(0);//平开仓
         dataSource.setProfit(2000.000);//平仓盈亏
         dataSource.setPlatformName("五山路口");
         dataSource.setAgencyName("王星代理人");
         String Message =  orderUserService.addOrderUser(dataSource);
         System.out.println(Message);

    }

    //测试平仓

    public void testOrder(){
        DataSource dataSource = new DataSource();
        dataSource.setLogin("hello wang xing ");
        dataSource.setTicket("110");
        dataSource.setNewTicket("122");
        dataSource.setPrice(200.00);
        dataSource.setVarietyCode("黄金");
        dataSource.setCmd(1);//多空  11111.1111-5555.1111
        dataSource.setHandNumber(5555.1111);
        dataSource.setPrice(100.00);//价位
        dataSource.setCreateTime("2018-11-22");
        dataSource.setOpenClose(1);//平开仓
        dataSource.setProfit(2000.000);//平仓盈亏
        dataSource.setPrice(2000.11);
        dataSource.setPlatformName("五山路口");
        dataSource.setAgencyName("王星代理人");
        String Message =  orderUserService.addOrderUser(dataSource);
        System.out.println(Message);

    }

    //测试收到tcp平仓数据，本地是没有开仓
    public void testOrderIfNull(){
        DataSource dataSource = new DataSource();
        dataSource.setLogin("hello wang xing ");
        dataSource.setTicket("1111");
        dataSource.setNewTicket("120");
        dataSource.setPrice(200.00);
        dataSource.setVarietyCode("黄金");
        dataSource.setCmd(1);//多空
        dataSource.setHandNumber(5555.1111);
        dataSource.setPrice(100.00);//价位
        dataSource.setCreateTime("2018-11-22");
        dataSource.setOpenClose(1);//平开仓
        dataSource.setProfit(2000.000);//平仓盈亏
        dataSource.setPlatformName("五山路口");
        dataSource.setAgencyName("王星代理人");
        String Message =  orderUserService.addOrderUser(dataSource);
        System.out.println(Message);
    }

    //查询多个用户  开始时间 结束时间，产品
    public void getOrderUserList(){
        List<String> strList = new ArrayList<>();
        strList.add("122 ");
        strList.add("123");
        String startTime = "2018-5-29 00:00:00";
        String productCode = "黄金";
        List<OrderUser> orderUserList = orderUserService.findByUserIdList(strList,startTime,"",productCode,1);
        System.out.println(orderUserList.size());
        for(OrderUser orderUser : orderUserList){
            System.out.println(orderUser.getUserCode());
        }
    }

    public void TestFindByTicket(){
        String ticket = "110";
        OrderUser orderUser =  orderUserService.findByTicket(ticket);
        System.out.println(orderUser.getAgencyName());

    }

    /**
     * 测试 接口是否调通
     */

    public void TestCount(){
        String endTime="";
        String startTime="";
        String productCode = "XAUUSD.e";
        String platformName="";
        String agencyName="";
        String contract = "";
        String userCode = "";
        OrderUserVo orderUserVo = new OrderUserVo();
        orderUserVo.setUserCode(userCode);
        orderUserVo.setContract(contract);
        orderUserVo.setPlatformName(platformName);
        orderUserVo.setAgencyName(agencyName);
        orderUserVo.setProductCode(productCode);
        orderUserVo.setEndTime(endTime);
        orderUserVo.setStartTime(startTime);
        OrderUserListVo orderUserList =    orderUserService.countOrderUser(orderUserVo);

        for(OrderUserVo o :orderUserList.getListVo()){
            System.out.println(o.getTotalGainAndLoss());//累计盈亏list
            System.out.println(o.getPosition_gain_and_loss());//盈亏效率
            System.out.println(o.getOffset_gain_and_loss());//平仓盈亏
        }

    }
    /**
     * 测试日期切割 & 根据日期去重 & 统计天数
     */

   public void TestgetDays(){
        List<OrderUser> orderUserList = orderUserService.findAll();
        LinkedHashSet<String> set = new LinkedHashSet<String>(orderUserList.size());
       for(OrderUser orderUser : orderUserList){
           set.add(orderUser.getCreateDate().substring(0,10).trim());
           System.out.println("CreateTime:"+orderUser.getCreateDate().substring(0,10).trim());
       }

        System.out.println("做单天数:"+set.size());
   }

  public void TestSms (){
        int code = (int)(Math.random()*8999)+1000; //生成随机数
      AliSms aliSms = new AliSms();
      try {
          SendSmsResponse sendSmsResponse = AliSms.SendSms("18664879184",code);
          System.out.println("短信接口返回的数据----------------");
          System.out.println("Code=" + sendSmsResponse.getCode());
          System.out.println("Message=" + sendSmsResponse.getMessage());
          System.out.println("RequestId=" + sendSmsResponse.getRequestId());
          System.out.println("BizId=" + sendSmsResponse.getBizId());
      } catch (ClientException e) {
          e.printStackTrace();
          System.out.println("发送失败");
      }
      System.out.println("发送成功");




  }

  public void testDate(){
       Long  millSec= 1530864644000L;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      Date date= new Date(millSec);

      System.out.println(sdf.format(date));
  }
    @Test
    public void testDouble (){
        double  d=0.01;
        if(d <0){
            System.out.println("负数");
        }else{
            System.out.println("不是负数");
        }
   }


}
