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
		int a = 6 / 0;
		return "val";
	}

}