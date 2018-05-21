package com.web.service.imp;

import com.web.service.IProducerMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/***
 * 生产者发送订单信息
 */
@Service@Transactional
public class OrderProducerMsgServiceImpl implements IProducerMsgService {
    @Autowired
    @Qualifier("orderJmsTemplate")
    private JmsTemplate orderJmsTemplate;
    public void sendMessage(final String message) {
        System.out.println("---------------发了一个订单消息：" + message);
        orderJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }


}
