package com.web.listeners;



import com.web.pojo.Account;
import com.web.pojo.vo.LoginMsgResult;
import com.web.pojo.vo.OrderMsgResult;
import com.web.service.IFollowOrderService;
import com.web.service.IFollowOrderTradeRecordService;
import com.web.util.StatusUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.regex.Pattern;

/**
 * 监听登录信息
 *@Author: May
 *@param
 *@Date: 11:07 2018/5/21
 */
public class ConsumerMessageListener implements MessageListener {
    @Autowired
    private IFollowOrderService followOrderService;
    @Autowired
    private IFollowOrderTradeRecordService followOrderTradeRecordService;
    public void onMessage(Message message) {
        //这里我们知道生产者发送的就是一个纯文本消息，所以这里可以直接进行强制转换
        TextMessage textMsg = (TextMessage) message;
        System.out.println("接收到一个纯文本消息。");
        try {
            String text = textMsg.getText();
            //将JSON转换成成JAVABean对象
            if(Pattern.compile("orderOpen").matcher(text).find()||Pattern.compile("orderClose").matcher(text).find()){
                JSONObject msgResult = JSONObject.fromObject(text);
                OrderMsgResult orderMsgResult = (OrderMsgResult) JSONObject.toBean(msgResult, OrderMsgResult.class);
                followOrderTradeRecordService.updateRecordByComeBackTradeMsg(orderMsgResult);
            }else if(Pattern.compile("userLogout").matcher(text).find()||Pattern.compile("userLogin").matcher(text).find()){
                JSONObject msgResult = JSONObject.fromObject(text);
                LoginMsgResult loginMsgResult = (LoginMsgResult) JSONObject.toBean(msgResult, LoginMsgResult.class);
                if(loginMsgResult.getTypeId().equals("userLogin")){
                    //设计启动
                    followOrderService.updateFollowOrderStatus(Long.valueOf(loginMsgResult.getRequestId()),
                            StatusUtil.FOLLOW_ORDER_START.getIndex());
                }
            }

            System.out.println("消息内容是：" + textMsg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}