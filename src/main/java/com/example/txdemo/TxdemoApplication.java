package com.example.txdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TxdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxdemoApplication.class, args);
	}

}

