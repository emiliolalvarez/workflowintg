package com.workflowintg.dispatcher.rest.resources;

//import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.myworkflow.main.Configuration;
//import com.workflowintg.dispatcher.JettyDispatcherServer;

@Path("/status")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class StatusResource {
	@Inject
	private Configuration c;
	private final static Logger LOGGER = Logger.getLogger(StatusResource.class);
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	public String currentStatus() {
		//JettyDispatcherServer.getRequestQueue().putMessage(""+new Date().getTime());
		LOGGER.debug("Returning current status");
		return "GOOD!!"+c.getString("database.driver");
	}
}
