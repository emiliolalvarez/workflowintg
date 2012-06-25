package com.workflowintg;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.xlightweb.client.HttpClient;
import org.xlightweb.client.HttpClientConnection;
import org.xlightweb.client.IHttpClientEndpoint;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.NonBlockingConnection;

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
	
	public void testConnection(){
		//http://media.gestionaleimmobiliare.it/foto/annunci/070911/31/800x600/537.jpg
		try {
			String url = "http://media.gestionaleimmobiliare.it/foto/annunci/070911/31/800x600/537.jpg";
			String host = "media.gestionaleimmobiliare.it";
			INonBlockingConnection tcpCon = new NonBlockingConnection(host, 80);
			IHttpClientEndpoint httpClientConnection = new HttpClientConnection(tcpCon);
			HttpClientConnection c = new HttpClientConnection(tcpCon);
			HttpClient cli = new HttpClient();
			cli.setMaxRedirects(3);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
