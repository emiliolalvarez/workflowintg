package com.workflowintg.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xlightweb.client.HttpClientConnection;

public class ImageServerPool {
	
	
	static Map<String, ArrayList<ConnectionWrapper> > pool = new HashMap<String, ArrayList<ConnectionWrapper>>();
	
	static final int MAX_CONNECTIONS_PER_DOMAIN = 3;
	
	static final int MAX_CACHE_SIZE = 50;
	
	static public synchronized ConnectionWrapper getConnection(String domain,int port){
		
		
		if(!pool.containsKey(domain)){
			checkCacheSize();
			ArrayList<ConnectionWrapper>  list = new ArrayList<ConnectionWrapper>(MAX_CONNECTIONS_PER_DOMAIN);
			pool.put(domain,list);
			for(int i=0;i<MAX_CONNECTIONS_PER_DOMAIN;i++){
				try {
					HttpClientConnection conn = new HttpClientConnection(domain,port);
					list.add(new ConnectionWrapper(conn));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return getAvailableConnection(domain);
		
		
	}
	
	static private void checkCacheSize(){
		
		if(pool.size()==MAX_CACHE_SIZE){
			Set<String> keys = pool.keySet();
			int c = 0;
			int idx = (int)Math.round(Math.random() * MAX_CACHE_SIZE);
			for(String k : keys){
				if(c==idx){
					pool.remove(k);
				}
				c++;
			}
		}
		
	}
	
	static private ConnectionWrapper getAvailableConnection(String domain){
		ArrayList<ConnectionWrapper> list = pool.get(domain);
		if(list!=null){
			for(ConnectionWrapper cw : list){
				if(cw.getConnection().isOpen() && !cw.isInUse()){
					cw.setIsInUse(true);
					return cw;
				}
			}
		}
		return null;
	}
	
	static public void close(){
		
		Set<String> keys = pool.keySet();
		for(String k : keys){
			ArrayList<ConnectionWrapper> list = pool.get(k);
			for(ConnectionWrapper c : list){
				try {
					c.getConnection().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pool.remove(k);
		}
		
	}
	
}
