package com.web.service.imp;

import com.web.service.ProducerMsgService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/***
 * 生产者发送订单信息
 */
@Service@Transactional
public class ProducerMsgServiceImpl implements ProducerMsgService {
//    @Autowired
//    @Qualifier("jmsTemplate")
//    private JmsTemplate jmsTemplate;
    public void sendMessage(final String message) {

//        jmsTemplate.send(new MessageCreator() {
//            public Message createMessage(Session session) throws JMSException {
//                return session.createTextMessage(message);
//            }
//        });
    }


}
