package com.workflowintg.dispatcher.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;


public abstract class JettyLauncher {
	
	public static Server launchJetty() throws Exception {
		
		Server server = new Server(9001);

		// Create a servlet context and add the jersey servlet.
		ServletContextHandler sch = new ServletContextHandler(server, "/rest");
		
		// Add our Guice listener that includes our bindings
		sch.addEventListener(new GuiceServletConfig());

		// Then add GuiceFilter and configure the server to
		// reroute all requests through this filter.
		sch.addFilter(GuiceFilter.class, "/*", null);
		
		// Must add DefaultServlet for embedded Jetty.
		// Failing to do this will cause 404 errors.
		// This is not needed if web.xml is used instead.
		sch.addServlet(DefaultServlet.class, "/");
		
		// Start the server
		server.start();
		
		return server;
	}
}
