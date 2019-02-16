package com.example.txdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

	@Autowired
	ConnectionFactory connectionFactory;
	
	private JmsTemplate jmsTemplate;
	
	@PostConstruct
	public void init() {
		this.jmsTemplate = new JmsTemplate(connectionFactory);
	}
	
	public void sendMessage(String queueName, String message) {
		System.out.println(String.format("Sending to queue %s: %s", queueName, message));
		jmsTemplate.send(queueName, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
			
		});
	}
	
	public int getMessageCount(String queueName) {
	    return jmsTemplate.browse(queueName, new BrowserCallback<Integer>() {
	        @Override
	        public Integer doInJms(Session s, QueueBrowser qb) throws JMSException {
	            return Collections.list(qb.getEnumeration()).size();
	        }
	    });
	}
	
	public List<String> readMessages(String queueName) {
	    return jmsTemplate.browse(queueName, new BrowserCallback<List<String>>() {
	        @Override
	        public List<String> doInJms(Session s, QueueBrowser qb) throws JMSException {
	        	List<String> result = new ArrayList<String>();
	        	Enumeration enumeration = qb.getEnumeration();
	        	while (enumeration.hasMoreElements()) {
	        		ActiveMQTextMessage message = (ActiveMQTextMessage) enumeration.nextElement();
	        		result.add(message.getText());
	        	}
	        	return result;
	        }
	    });
	}
	
}
