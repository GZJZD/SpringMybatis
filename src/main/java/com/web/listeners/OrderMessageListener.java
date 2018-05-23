package com.web.listeners;


import com.web.service.IFollowOrderTradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
/**
 * 监听下单的信息
 *@Author: May
 *@param
 *@Date: 11:08 2018/5/21
 */
public class OrderMessageListener implements MessageListener {
    @Autowired
    private IFollowOrderTradeRecordService followOrderTradeRecordService;
    public void onMessage(Message message) {
        //这里我们知道生产者发送的就是一个纯文本消息，所以这里可以直接进行强制转换
        TextMessage textMsg = (TextMessage) message;
        System.out.println("接收到一个纯文本消息。");
        try {
            System.out.println("消息内容是：" + textMsg.getText());
            followOrderTradeRecordService.updateRecordByComeBackTradeMsg(textMsg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}