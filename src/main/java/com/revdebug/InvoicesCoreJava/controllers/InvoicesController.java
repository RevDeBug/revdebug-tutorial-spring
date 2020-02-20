package com.revdebug.InvoicesCoreJava.controllers;

import com.revdebug.InvoicesCoreJava.dataAccess.ReconsileData;
import com.revdebug.InvoicesCoreJava.models.Invoice;
import com.revdebug.InvoicesCoreJava.models.InvoiceEntry;
import com.revdebug.InvoicesCoreJava.models.InvoiceEntryRepository;
import com.revdebug.InvoicesCoreJava.models.InvoiceRepository;
import javassist.bytecode.stackmap.BasicBlock;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.*;

import javax.persistence.EntityManager;

@Controller
public class InvoicesController {

	private final InvoiceRepository invoiceRepository;

	private final InvoiceEntryRepository invoiceEntryRepository;

	public InvoicesController(InvoiceRepository invoiceRepository, InvoiceEntryRepository invoiceEntryRepository) {
		this.invoiceRepository = invoiceRepository;
		this.invoiceEntryRepository = invoiceEntryRepository;
	}

	@GetMapping("/invoices")
	public String invoices(Map<String, Object> model) {
		List<Object> InvoiceEntries = invoiceEntryRepository.selectAll();

		model.put("Invoices", InvoiceEntries);
		return "invoices";
	}

	@GetMapping("/invoices/{invoiceNumber}/details")
	public String details(@PathVariable("invoiceNumber") int invoiceNumber, Model model) {
		Invoice invoice = invoiceRepository.findByInvoiceId(invoiceNumber);
		model.addAttribute(invoice);
		return "details";
	}

	@GetMapping("/invoices/{invoiceNumber}/reconsile")
	public String reconsile(@PathVariable("invoiceNumber") int invoiceNumber, Model model) {
		Invoice invoice = invoiceRepository.findByInvoiceId(invoiceNumber);
		invoice.Reconciled = CalculateReconcilation(invoice.InvoiceId);
		invoiceRepository.save(invoice);

		model.addAttribute(invoice);
		return "details";
	}

	private double CalculateReconcilation(String id) {
		double reconsiled = 0;

		ReconsileData reconsileData = new ReconsileData("InvoiceData.json");
		JSONArray invoiceEntries = reconsileData.getInvoiceEntriesByInvoiceId(id);

		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = System.getenv("InvoicesCoreAddress") + "/Products/all";
		String response = restTemplate.getForEntity(resourceUrl, String.class).getBody();

		JSONParser parser = new JSONParser();
		JSONArray productsArray = null;
		try {
			productsArray = (JSONArray) parser.parse(response);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		for (Object item : invoiceEntries) {
			if (item instanceof JSONObject) {
				int quantity = Integer.parseInt(((JSONObject) item).get("Quantity").toString());
				int productId = Integer.parseInt(((JSONObject) item).get("ProducId").toString());

				JSONObject product = null;
				for (Object prod : productsArray) {
					if (prod instanceof JSONObject) {
						int prodId = Integer.parseInt(((JSONObject) prod).get("ProductId").toString());
						if (prodId == productId) {
							product = (JSONObject) prod;
						}
					}
				}

				String productName;
				double unitPrice;
				int taxRate;
				double tax;

				if (product != null) {
					productName = product.get("Label").toString();
					String unitPriceString = product.get("UnitPrice").toString();
					unitPrice = Double.parseDouble(unitPriceString);
					taxRate = Integer.parseInt(product.get("Tax").toString());
					tax = unitPrice * taxRate / 100;

					reconsiled += (unitPrice + tax) * quantity;

				}
				else {
					return reconsiled;
				}

			}
		}

		return reconsiled;
	}

}
