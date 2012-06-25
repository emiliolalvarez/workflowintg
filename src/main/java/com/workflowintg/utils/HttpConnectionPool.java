package com.workflowintg.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.xlightweb.client.HttpClient;
import org.xlightweb.client.HttpClient.FollowsRedirectMode;

import com.sun.istack.logging.Logger;

public class HttpConnectionPool {
	private final static int MAX_CONCURRENT_DOMAINS = 15;
	private final static int MAX_CONNECTIONS_PER_DOMAIN = 10;
	private static Map<String,BlockingQueue<HttpClient>> pool = new HashMap<String, BlockingQueue<HttpClient>>();
	private static List<String> list = new LinkedList<String>();
	private static Logger LOGGER = Logger.getLogger(HttpConnectionPool.class);
	
	public static BlockingQueue<HttpClient> getConnectionPool(String domain,int port,Object obj) throws IOException{
		String key = domain+port;
		if(!pool.containsKey(key)){
			if(pool.size()==MAX_CONCURRENT_DOMAINS){
				pool.remove(list.remove(pool.size()-1));
			}
			BlockingQueue<HttpClient> connections = new LinkedBlockingQueue<HttpClient>();
			for(int i=0;i<MAX_CONNECTIONS_PER_DOMAIN;i++){
				HttpClient client = new HttpClient();
				client.setFollowsRedirectMode(FollowsRedirectMode.ALL);
				client.setMaxRedirects(5);
				client.setMaxRetries(3);
				client.setMaxActivePerServer(1);
				//client.setMaxActive(1);
				//client.setMaxIdle(0);
				client.setConnectTimeoutMillis(2000);
				connections.add(client);
			}
			pool.put(key, connections);
			list.add(0,key);
		}
		BlockingQueue<HttpClient> q = pool.get(key);
		return q;
	}
	
	public static void releaseConnection(String domain, int port,HttpClient c){
		String key = domain+port;
		pool.get(key).add(c);
		LOGGER.info("CONNECTION RELEASED ON POOL ["+key+"]");
	}
	

	
	
}
