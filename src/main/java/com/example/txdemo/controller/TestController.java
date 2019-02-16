package com.example.txdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.txdemo.DatabaseWriter;
import com.example.txdemo.MessagePublisher;

@Controller
public class TestController {

	private long counter = 0;
	
	@Autowired
	private MessagePublisher messagePublisher;

	@Autowired
	private DatabaseWriter databaseWriter;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("queuedepth", messagePublisher.getMessageCount("queue1"));
		model.addAttribute("messages", messagePublisher.readMessages("queue1"));
		
		model.addAttribute("numrecords", databaseWriter.numberofRecords());
		model.addAttribute("records", databaseWriter.readRecords());
		
		return "index";
	}
	
	@GetMapping("/onlyLocal")
	public String onlyLocal(Model model) {
		return "redirect:/";
	}

	@Transactional
	@GetMapping("/toMq")
	public String writeToMQ() {
		messagePublisher.sendMessage("queue1", "str " + System.currentTimeMillis());
		return "redirect:/";
	}

	@Transactional
	@GetMapping("/toDb")
	public String writeToDB() {
		databaseWriter.writeToDatabase("str " + System.currentTimeMillis());
		return "redirect:/";
	}

	@Transactional
	@GetMapping("shouldWorkEveryTime")
	public String testShouldWorkEveryTime() {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("Current timestamp: " + currentTimeMillis);
		
		messagePublisher.sendMessage("queue1", "str " + currentTimeMillis);
		databaseWriter.writeToDatabase("str " + currentTimeMillis);
		
		return "redirect:/";
	}

	@Transactional
	@GetMapping("shouldFailEveryTime")
	public String testShouldFailEveryTime() {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("Current timestamp: " + currentTimeMillis);
		
		messagePublisher.sendMessage("queue1", "str " + currentTimeMillis);
		databaseWriter.writeToDatabase("str " + currentTimeMillis);
		
		throw new RuntimeException("Shit happened!");
	}

	@Transactional
	@GetMapping("shouldFailEverySecondTime")
	public String testShouldFailEverySecondTime() {
		counter++;
		
		System.out.println("Current counter: " + counter);
		
		messagePublisher.sendMessage("queue1", "counter " + counter);
		databaseWriter.writeToDatabase("counter " + counter);
		
		if (counter % 2 == 0) {
			throw new RuntimeException("Shit happened!");
		}
		
		return "redirect:/";
	}
	
}
