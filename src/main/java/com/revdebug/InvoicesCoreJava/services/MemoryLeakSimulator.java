package com.revdebug.InvoicesCoreJava.services;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakSimulator {

	public List<Double> list = new ArrayList<>();

	public void populateList() {

		long t = System.currentTimeMillis();
		int thirtySeconds = 30000;
		long end = t + thirtySeconds;
		new Thread(() -> {
			while (System.currentTimeMillis() < end) {
				for (int i = 0; i < 10000000; i++) {
					list.add(Math.random());
				}
			}
			list.clear();
		}).start();

	}

}