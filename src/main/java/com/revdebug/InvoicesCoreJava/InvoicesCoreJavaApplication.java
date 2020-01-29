package com.revdebug.InvoicesCoreJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(proxyBeanMethods = false)
public class InvoicesCoreJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicesCoreJavaApplication.class, args);
	}

}
