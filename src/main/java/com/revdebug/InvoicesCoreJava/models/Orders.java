package com.revdebug.InvoicesCoreJava.models;

import java.util.Date;
import java.util.HashSet;

public class Orders {

	public Orders() {
		OrderDetails = new HashSet<OrderDetails>();
	}

	public short OrderId;

	public String CustomerId;

	public short EmployeeId;

	public Date OrderDate;

	public Date RequiredDate;

	public Date ShippedDate;

	public short ShipVia;

	public float Freight;

	public String ShipName;

	public String ShipAddress;

	public String ShipCity;

	public String ShipRegion;

	public String ShipPostalCode;

	public String ShipCountry;

	public Customers Customer;

	public HashSet<OrderDetails> OrderDetails;

}
