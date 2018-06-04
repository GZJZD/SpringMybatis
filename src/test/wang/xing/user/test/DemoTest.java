package wang.xing.user.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.web.pojo.DataSource;
import com.web.pojo.OrderUser;
import com.web.pojo.vo.OrderUserVo;
import com.web.service.OrderUserService;
import com.web.service.TestService;
import com.web.util.query.QueryObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.Order;
import java.util.ArrayList;
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
        List<OrderUser> orderUserList = orderUserService.findByUserIdList(strList,startTime,"",productCode);
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

    @Test
    public void TestCount(){
        String endTime="";
        String startTime="";
        String productCode = "黄金";
        OrderUserVo orderUserVo = new OrderUserVo();
        orderUserVo.setProductCode(productCode);
        orderUserVo.setEndTime(endTime);
        orderUserVo.setStartTime(startTime);
     List<OrderUserVo> orderUserList=    orderUserService.countOrderUser(orderUserVo);
        System.out.println(orderUserList.size());
    }
}
