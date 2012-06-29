package com.workflowintg.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.log4j.Logger;

public class HttpPoolMonitor extends Thread {

	private Logger LOGGER = Logger.getLogger(HttpPoolMonitor.class);

	private boolean isRunning = true;
	
	public void stopService() {
		LOGGER.info("Stoping HttpPoolMonitor service");
		this.isRunning = false;
	}

	public void run() {
		this.setName("Pool Monitor Service");
		LOGGER.info("HttpPoolMonitor service started");
		while (isRunning) {
			synchronized (this) {
				try {
					wait(5000);
					LOGGER.info("Closing expired and idle connections");
					Map<String, PoolingClientConnectionManager> pools = HttpConnectionPool
							.getPools();
					for (Entry<String, PoolingClientConnectionManager> e : pools
							.entrySet()) {
						PoolingClientConnectionManager pool = e.getValue();
						pool.closeExpiredConnections();
						pool.closeIdleConnections(2, TimeUnit.MINUTES);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
