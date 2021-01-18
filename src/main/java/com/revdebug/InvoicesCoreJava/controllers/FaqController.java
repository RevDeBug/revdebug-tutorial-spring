package com.revdebug.InvoicesCoreJava.controllers;

import com.revdebug.InvoicesCoreJava.services.MemoryLeakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaqController {

	@Autowired
	MemoryLeakService memoryLeakService;

	@GetMapping("/faq")
	public String faq() {
		memoryLeakService.createMemoryLeak();
		return "faq";
	}

}