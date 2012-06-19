package com.workflowintg.dispatcher.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.workflowintg.dispatcher.JettyDispatcherServer;

@Path("/stop")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class StopResource {
	
	private final static Logger LOGGER = Logger.getLogger(StopResource.class);
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	public String stop() {
		
		JettyDispatcherServer.workflowDefinition.getWorkflowDefinitionContext().finish();
		LOGGER.info("Stopping service");
		return "Service Stopped!";
	}
}
