package com.revdebug.InvoicesCoreJava.models;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Accounts")
@Table(name = "\"Accounts\"")
public class Account {

	@Id
	@Column(name = "\"AccountId\"")
	public int AccountId;

	@Column(name = "\"FirstName\"")
	public String FirstName;

	@Column(name = "\"LastName\"")
	public String LastName;

	@Column(name = "\"Balance\"")
	public int Balance;

	@Column(name = "\"Invoices\"")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "AccountId")
	private Set<Invoice> Invoices;

}
