package com.revdebug.InvoicesCoreJava.controllers;

import com.revdebug.InvoicesCoreJava.dataAccess.ReconsileData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

	@PostMapping(value = "/buildInvoice", produces = "application/json", consumes = "application/json")
	public String buildInvoice(@RequestBody LinkedHashMap order) {
		// String orderString = httpEntity.getBody();
		double total = 0;
		StringBuilder invoice = new StringBuilder();
		java.util.ArrayList orderDetailsArray = (ArrayList) order.get("OrderDetails");
		for (int no = 0; no < orderDetailsArray.size(); no++) {
			LinkedHashMap orderDetails = (LinkedHashMap) orderDetailsArray.get(no);
			double discount = (double) orderDetails.get("Discount");
			if (discount >= 100) {
				continue;
			}

			if (no == 0) {
				invoice.append("Invoice number " + order.get("OrderId") + " for "
						+ ((LinkedHashMap) order.get("Customer")).get("CompanyName"));
				invoice.append(System.getProperty("line.separator"));
			}

			double price = (int) orderDetails.get("Quantity") * (double) orderDetails.get("UnitPrice");
			double discountedPrice = price - (price * (double) orderDetails.get("Discount") / 100);
			total = total + discountedPrice;
			LinkedHashMap product = (LinkedHashMap) orderDetails.get("Product");
			invoice.append(product.get("ProductName") + " Price: " + product.get("UnitPrice") + " Quantity: "
					+ orderDetails.get("Quantity") + " Discount: " + orderDetails.get("Discount")
					+ "% Discounted Total: $" + discountedPrice);
			invoice.append(System.getProperty("line.separator"));
			if (no + 1 == orderDetailsArray.size()) {
				invoice.append("TOTAL: $" + total);
			}

		}
		try {
			String invoiceString = invoice.toString();
			ReconsileData.ProcessInvoice(invoiceString);
			RestTemplate restTemplate = new RestTemplate();
			String resourceUrl = System.getenv("InvoicesSenderAddress") + "/Sender/Send?invoice=" + invoiceString;
			restTemplate.getForEntity(resourceUrl, String.class).getBody();
		}
		catch (Exception e) {
		}
		return "val";
	}

}