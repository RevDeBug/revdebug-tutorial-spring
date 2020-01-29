package com.revdebug.InvoicesCoreJava.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "Invoices")
@Table(name = "\"Invoices\"")
public class Invoice {

	@Id
	@Column(name = "\"InvoiceId\"")
	public String InvoiceId;

	@Column(name = "\"Number\"")
	public int Number;

	@Column(name = "\"IssueDate\"")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "0:dd/MM/yyyy")
	public Date IssueDate;

	@Column(name = "\"DueDate\"")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "0:dd/MM/yyyy")
	public Date DueDate;

	@Column(name = "\"Reconciled\"")
	public double Reconciled;

	@Column(name = "\"Accepted\"")
	public boolean Accepted;

	@OneToOne
	@JoinColumn(name = "\"AccountId\"")
	public Account AccountId;

	@Column(name = "\"InvoiceEntries\"")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "InvoiceId")
	public Set<InvoiceEntry> InvoiceEntries;

	@Transient
	public double TotalBill;

	public double getTotalBill() {

		double sum = 0;

		for (InvoiceEntry item : InvoiceEntries) {
			sum += item.getTotalBill();
		}

		return sum;
	}

}
