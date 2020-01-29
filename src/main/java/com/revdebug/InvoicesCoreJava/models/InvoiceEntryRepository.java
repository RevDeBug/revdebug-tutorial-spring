package com.revdebug.InvoicesCoreJava.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceEntryRepository extends Repository<InvoiceEntry, String> {

	@Query(value = "SELECT inv FROM Invoices inv, Accounts cnt, Products prod, InvoiceEntries entr WHERE entr.InvoiceId = inv.InvoiceId AND entr.ProductId = prod.ProductId AND inv.AccountId = cnt.AccountId GROUP BY inv.InvoiceId")
	List<Object> selectAll();

}
