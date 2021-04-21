package com.revdebug.InvoicesCoreJava.services;

import com.revdebug.InvoicesCoreJava.dataAccess.ReconsileData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReconciliationCalculatorService {

    public double calculate(String id) {
        double reconciled = 0;

        ReconsileData reconsileData = new ReconsileData("InvoiceData.json");
        JSONArray invoiceEntries = reconsileData.getInvoiceEntriesByInvoiceId(id);

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = System.getenv("InvoicesCoreAddress") + "/Products/all";
        String response = restTemplate.getForEntity(resourceUrl, String.class).getBody();

        JSONParser parser = new JSONParser();
        JSONArray productsArray = null;
        try {
            productsArray = (JSONArray) parser.parse(response);
        } catch (ParseException e) {
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

                    reconciled += (unitPrice + tax) * quantity;

                } else {
                    return reconciled;
                }

            }
        }

        return reconciled;
    }
}
