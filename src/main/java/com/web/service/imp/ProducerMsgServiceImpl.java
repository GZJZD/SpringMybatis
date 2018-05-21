package com.web.service.imp;

import com.web.service.IProducerMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/***
 * 生产者发送信息
 */
@Service
public class ProducerMsgServiceImpl implements IProducerMsgService {
    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate jmsTemplate;
    public void sendMessage(final String message) {
        System.out.println("---------------生产者发了一个消息：" + message);
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }


}
