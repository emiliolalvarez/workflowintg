package com.workflowintg;

import org.apache.log4j.Logger;

import com.myworkflow.main.Configuration;
import com.workflowintg.dispatcher.JettyDispatcherServer;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.partner.PartnerSession;

public class Main extends Thread {
	
	private final static Logger LOGGER = Logger.getLogger(Main.class);
	
	private Configuration config;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main().start();
	}
	
	public void run(){
		LOGGER.info("Initializing...");
		//Launch Jetty rest server
		new JettyDispatcherServer().start();
		
		config = GuiceServletConfig.getDependencyInjector().getInstance(Configuration.class);
		
		LOGGER.info(config.getString("database.driver"));
	
		//Launch test partner session;
		PartnerSession ps = new PartnerSession("mypartner");
		Thread t = new Thread(ps);
		t.setName(ps.getPartnerName());
		t.start();
	
	}
	
	public void setUpDependencyConfiguration(){
		
	}
	
	

}
