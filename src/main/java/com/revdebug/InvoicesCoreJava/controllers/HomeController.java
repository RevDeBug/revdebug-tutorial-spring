package com.revdebug.InvoicesCoreJava.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONArray;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/getval")
	public String getval() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String resourceUrl = "http://localhost:8083/processval";
			restTemplate.getForEntity(resourceUrl, String.class).getBody();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "val";
	}

	@GetMapping("/processval")
	public String processval() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String resourceUrl = "http://localhost:8084/calculateval";
			restTemplate.getForEntity(resourceUrl, String.class).getBody();
		}
		catch (Exception e) {
		}
		return "val";
	}

	@GetMapping("/calculateval")
	public String calculateval() {
		int multiplier = 10;
		int val = 0;
		for (int i = 10; i >= 0;) {
			val = val + multiplier / i;
			i = i - 1;
		}
		return String.valueOf(val);
	}

}