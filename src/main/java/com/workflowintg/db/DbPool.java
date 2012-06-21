package com.workflowintg.db;

import java.beans.PropertyVetoException;

import com.google.inject.Inject;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myworkflow.main.Configuration;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;

public class DbPool {

	private static DbPool instance;

	private ComboPooledDataSource pool;
	@Inject
	private Configuration c;

	public static DbPool getInstance() throws PropertyVetoException {
		
		if (instance == null) {
			instance = new DbPool();
			instance.setUpPool();
		}
		
		return instance;
	}
	
	public ComboPooledDataSource getPool(){
		return pool;
	}

	private void setUpPool() throws PropertyVetoException {

		pool = new ComboPooledDataSource();
		pool.setDriverClass("com.mysql.jdbc.Driver"); // loads the jdbc driver
		pool.setJdbcUrl("jdbc:mysql://db-dev.olx.com.ar/FEEDS_DEV");
		pool.setUser("FEEDS_DEV");
		pool.setPassword("+BCp3nc4M#");
		
		int increment = c.getInt("pool.AcquireIncrement");
		pool.setAcquireIncrement(increment);

		int retry = c.getInt("pool.AcquireRetryAttempts");
		pool.setAcquireRetryAttempts(retry);

		int minSize = c.getInt("pool.MinPoolSize");
		pool.setMinPoolSize(minSize);

		int maxSize = c.getInt("pool.MaxPoolSize");
		pool.setMaxPoolSize(maxSize);

		int maxIdle = c.getInt("pool.MaxIdleItem");
		pool.setMaxIdleTime(maxIdle);

		int checkoutTimeout = c.getInt("pool.CheckoutTimeout");
		pool.setCheckoutTimeout(checkoutTimeout);

		int idleTestPeriod = c.getInt("pool.IdleTestPeriod");
		pool.setIdleConnectionTestPeriod(idleTestPeriod);

		String testQuery = c.getString("pool.TestQuery");
		pool.setPreferredTestQuery(testQuery);
		
		System.out.println(c.getString("database.driver"));
		
	}
}
