package wang.xing.user.test;

import com.web.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-mvc.xml","classpath:spring-mybatis.xml"})
public class DemoTest {

    @Autowired
    @Qualifier("testService")
    private TestService testService;
    @Test
    public  void demo(){
        System.out.println("************************************");
        int  i = 1;


    }

}
