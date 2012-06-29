package com.workflowintg.dispatcher.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.workflowintg.utils.HttpConnectionPool;

@Path("/http_pool_status")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class HttpPoolStatus {
	
	private final static Logger LOGGER = Logger.getLogger(StopResource.class);
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	public String httpPoolStatus() {
		
		
		LOGGER.info("Http connection pool status requested");
		return HttpConnectionPool.getinfo();
	}
	
}
