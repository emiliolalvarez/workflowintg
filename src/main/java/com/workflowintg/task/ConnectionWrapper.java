package com.workflowintg.task;

import org.xlightweb.client.HttpClientConnection;

public class ConnectionWrapper{
	private boolean isInUse = false;
	
	private HttpClientConnection conn = null;
	
	public ConnectionWrapper(HttpClientConnection conn){
		this.conn = conn;
	}
	
	public void setIsInUse(boolean state){
		isInUse = state;
	}
	
	public boolean isInUse(){
		return isInUse;
	}
	
	public HttpClientConnection getConnection(){
		return conn;
	}
	
}