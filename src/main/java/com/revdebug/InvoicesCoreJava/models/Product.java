package com.revdebug.InvoicesCoreJava.models;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Products")
@Table(name = "\"Products\"")
public class Product {

	@Id
	@Column(name = "\"ProductId\"")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int ProductId;

	@Column(name = "\"Label\"")
	public String Label;

	@Column(name = "\"Description\"")
	public String Description;

	@Column(name = "\"UnitPrice\"")
	public double UnitPrice;

	@Column(name = "\"Tax\"")
	public double Tax;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ProductId")
	@Column(name = "\"Entries\"")
	private Set<InvoiceEntry> Entries;

}
