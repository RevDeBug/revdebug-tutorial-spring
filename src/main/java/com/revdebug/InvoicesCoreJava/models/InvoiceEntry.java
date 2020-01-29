package com.revdebug.InvoicesCoreJava.models;

import javax.persistence.*;

@Entity(name = "InvoiceEntries")
@Table(name = "\"InvoiceEntries\"")
public class InvoiceEntry {

	@Id
	@Column(name = "\"InvoiceEntryId\"")
	public int InvoiceEntryId;

	@Column(name = "\"Quantity\"")
	public int Quantity;

	@ManyToOne
	@JoinColumn(name = "\"ProductId\"")
	public Product ProductId;

	@ManyToOne
	@JoinColumn(name = "\"InvoiceId\"")
	public Invoice InvoiceId;

	@Transient
	public double Gross;

	public double getGross() {
		double gross = ProductId.UnitPrice + (ProductId.UnitPrice * ProductId.Tax / 100);
		return gross;
	}

	@Transient
	public double TotalBill;

	public double getTotalBill() {
		return getGross() * Quantity;
	}

}
