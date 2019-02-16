//package com.example.txdemo.resource;
//
//import javax.transaction.Transactional;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.example.txdemo.DatabaseWriter;
//import com.example.txdemo.MessagePublisher;
//
//@Component
//@Path("/api/test")
//public class TestResource {
//
//	private long counter = 0;
//	
//	@Autowired
//	private MessagePublisher messagePublisher;
//
//	@Autowired
//	private DatabaseWriter databaseWriter;
//	
//	@GET
//	@Produces("text/plain")
//	@Path("onlyLocal")
//	public String onlyLocal() {
//		return "OK";
//	}
//	
//	@GET
//	@Produces("text/plain")
//	@Path("toMq")
//	@Transactional
//	public String writeToMQ() {
//		messagePublisher.sendMessage("queue1", "str " + System.currentTimeMillis());
//		return "OK";
//	}
//	
//	@GET
//	@Produces("text/plain")
//	@Path("toDb")
//	@Transactional
//	public String writeToDB() {
//		databaseWriter.writeToDatabase("str " + System.currentTimeMillis());
//		return "OK";
//	}
//	
//	@GET
//	@Produces("text/plain")
//	@Path("shouldWorkEveryTime")
//	@Transactional
//	public String testShouldWorkEveryTime() {
//		long currentTimeMillis = System.currentTimeMillis();
//		System.out.println("Current timestamp: " + currentTimeMillis);
//		
//		messagePublisher.sendMessage("queue1", "str " + currentTimeMillis);
//		databaseWriter.writeToDatabase("str " + currentTimeMillis);
//		
//		return "SUCCESS";
//	}
//	
//	@GET
//	@Produces("text/plain")
//	@Path("shouldFailEveryTime")
//	@Transactional
//	public String testShouldFailEveryTime() {
//		long currentTimeMillis = System.currentTimeMillis();
//		System.out.println("Current timestamp: " + currentTimeMillis);
//		
//		messagePublisher.sendMessage("queue1", "str " + currentTimeMillis);
//		databaseWriter.writeToDatabase("str " + currentTimeMillis);
//		
//		throw new RuntimeException("Shit happened!");
//	}
//	
//	@GET
//	@Produces("text/plain")
//	@Path("shouldFailEverySecondTime")
//	@Transactional
//	public String testShouldFailEverySecondTime() {
//		counter++;
//		
//		System.out.println("Current counter: " + counter);
//		
//		messagePublisher.sendMessage("queue1", "counter " + counter);
//		databaseWriter.writeToDatabase("counter " + counter);
//		
//		if (counter % 2 == 0) {
//			throw new RuntimeException("Shit happened!");
//		}
//		
//		return String.valueOf(counter);
//	}
//	
//}
