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
 * 生产者发送登录信息
 */
@Service("loginProducerMsgServiceImpl")
@Transactional
public class LoginProducerMsgServiceImpl implements IProducerMsgService {
    @Autowired
    @Qualifier("loginJmsTemplate")
    private JmsTemplate loginJmsTemplate;

    public void sendMessage(final String message) {
        System.out.println("---------------生产者发了登录一个消息：" + message);
        loginJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }


}
