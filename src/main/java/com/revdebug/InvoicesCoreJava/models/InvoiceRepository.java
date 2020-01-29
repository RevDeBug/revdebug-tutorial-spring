package com.revdebug.InvoicesCoreJava.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceRepository extends Repository<Invoice, Integer> {

	@Query(value = "SELECT inv FROM Invoices inv WHERE Number =:number")
	Invoice findByInvoiceId(@Param("number") Integer number);

	void save(Invoice invoice);

}
