package com.revdebug.InvoicesCoreJava.dataAccess;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.*;
import org.w3c.dom.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReconsileData {

	JSONObject obj;

	public ReconsileData(String fileName) {
		JSONParser jsonParser = new JSONParser();

		InputStream in = null;
		try {
			in = new ClassPathResource(fileName).getInputStream();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			obj = (JSONObject) jsonParser.parse(reader);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public JSONArray getInvoiceEntriesByInvoiceId(String id) {
		JSONArray invoiceEntries = (JSONArray) obj.get("InvoiceEntries");
		JSONArray filteredInvoiceEntries = new JSONArray();

		for (Object item : invoiceEntries) {
			if (item instanceof JSONObject) {
				String invId = (String) ((JSONObject) item).get("InvoiceId");
				if (id.contains(invId)) {
					filteredInvoiceEntries.add(item);
				}
			}
		}

		return filteredInvoiceEntries;
	}

	public JSONObject getProductById(int id) {
		JSONArray products = (JSONArray) obj.get("Products");
		JSONObject product = null;

		for (Object item : products) {
			if (item instanceof JSONObject) {
				int productId = Integer.parseInt((String) ((JSONObject) item).get("ProducId"));
				if (id == productId) {
					return (JSONObject) item;
				}
			}
		}

		return product;
	}

}
