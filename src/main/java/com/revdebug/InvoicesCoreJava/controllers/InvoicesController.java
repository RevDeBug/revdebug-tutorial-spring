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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

		for (Object item : invoiceEntries) {
			if (item instanceof JSONObject) {
				int quantity = Integer.parseInt(((JSONObject) item).get("Quantity").toString());
				int productId = Integer.parseInt(((JSONObject) item).get("ProducId").toString());
				JSONObject product = reconsileData.getProductById(productId);

				String productName;
				double unitPrice;
				int taxRate;
				double tax;

				if (product != null) {
					productName = product.get("Label").toString();
					unitPrice = Double.parseDouble(product.get("UnitPrice").toString());
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
