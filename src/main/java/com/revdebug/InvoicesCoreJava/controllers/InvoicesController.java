package com.revdebug.InvoicesCoreJava.controllers;

import com.google.common.collect.Lists;
import com.revdebug.InvoicesCoreJava.dataAccess.ReconsileData;
import com.revdebug.InvoicesCoreJava.models.Invoice;
import com.revdebug.InvoicesCoreJava.models.InvoiceEntryRepository;
import com.revdebug.InvoicesCoreJava.models.InvoiceRepository;
import com.revdebug.InvoicesCoreJava.models.Product;
import com.revdebug.InvoicesCoreJava.models.ProductRepository;
import com.revdebug.InvoicesCoreJava.services.ReconciliationCalculatorService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class InvoicesController {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceEntryRepository invoiceEntryRepository;

    private final ReconciliationCalculatorService reconciliationCalculatorService;

    private final ProductRepository productRepository;

    public InvoicesController(InvoiceRepository invoiceRepository, InvoiceEntryRepository invoiceEntryRepository,
                              ReconciliationCalculatorService reconciliationCalculatorService, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceEntryRepository = invoiceEntryRepository;
        this.reconciliationCalculatorService = reconciliationCalculatorService;
        this.productRepository = productRepository;
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

    @GetMapping("/invoices/{invoiceNumber}/reconcile")
    public String reconcile(@PathVariable("invoiceNumber") int invoiceNumber, Model model) {
        Invoice invoice = invoiceRepository.findByInvoiceId(invoiceNumber);
        invoice.Reconciled = reconciliationCalculatorService.calculate(invoice.InvoiceId);
        invoiceRepository.save(invoice);

        model.addAttribute(invoice);
        return "details";
    }

    private double CalculateReconcilation(String id) {
        double reconsiled = 0;

        List<Product> allProducts = Lists.newArrayList(productRepository.findAll());

        ReconsileData reconsileData = new ReconsileData("InvoiceData.json");
        JSONArray invoiceEntries = reconsileData.getInvoiceEntriesByInvoiceId(id);

        JSONParser parser = new JSONParser();
        JSONArray productsArray = null;

        for (Object item : invoiceEntries) {
            if (item instanceof JSONObject) {
                int quantity = Integer.parseInt(((JSONObject) item).get("Quantity").toString());
                int productId = Integer.parseInt(((JSONObject) item).get("ProducId").toString());

                Product product = allProducts.stream().filter(p -> p.ProductId==productId).findFirst().orElse(null);
                String productName;
                double unitPrice;
                int taxRate;
                double tax;
                productName = product.Label;
                String unitPriceString = String.valueOf(product.UnitPrice);
                unitPrice = Double.parseDouble(unitPriceString);
                taxRate = (int)product.Tax;
                tax = unitPrice * taxRate / 100;
                reconsiled += (unitPrice + tax) * quantity;
            }
        }

        return reconsiled;
    }

}
