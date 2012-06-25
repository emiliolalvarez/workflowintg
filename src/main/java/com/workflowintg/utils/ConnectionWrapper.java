package com.workflowintg.utils;

import org.xlightweb.client.HttpClient;

public class ConnectionWrapper{
	
	private boolean isInUse = false;
	
	private HttpClient conn = null;
	
	public ConnectionWrapper(HttpClient conn){
		this.conn = conn;
	}
	
	public void setIsInUse(boolean state){
		isInUse = state;
	}
	
	public boolean isInUse(){
		return isInUse;
	}
	
	public HttpClient getConnection(){
		return conn;
	}
	
}