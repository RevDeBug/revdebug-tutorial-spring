package com.revdebug.InvoicesCoreJava.services;

import org.springframework.stereotype.Service;

@Service
public class MemoryLeakService {

	public void createMemoryLeak() {
		new Thread(() -> {
			new MemoryLeakSimulator().populateList();
		}).start();

	}

}
