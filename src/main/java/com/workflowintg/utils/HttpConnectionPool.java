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
import org.xlightweb.client.MyHttpClient;

import com.sun.istack.logging.Logger;

public class HttpConnectionPool {
	
	private final static int MAX_CONCURRENT_DOMAINS = 3;
	private final static int MAX_CONNECTIONS_PER_DOMAIN = 2;
	private static Map<String,BlockingQueue<MyHttpClient>> pool = new HashMap<String, BlockingQueue<MyHttpClient>>();
	private static List<String> list = new LinkedList<String>();
	private static Logger LOGGER = Logger.getLogger(HttpConnectionPool.class);

	public synchronized static String getinfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP CONNECTION POOL STATUS\n");
		sb.append("--------------------------------------------\n");
		for(String k:pool.keySet()){
			BlockingQueue<MyHttpClient> q = pool.get(k);
			int totalActive=0,totalCreated=0,totalDestroyed=0;
			sb.append(k+":\n");
			sb.append("-------------------------------------------------------------------"+"\n");
			sb.append("Clients queued:"+q.size()+"\n");
			for(HttpClient c:q){
				totalActive+=c.getNumActive();
				totalCreated+=c.getNumCreated();
				totalDestroyed=c.getNumDestroyed();
			}
			sb.append("Active: "+totalActive+"\n");
			sb.append("Created: "+totalCreated+"\n");
			sb.append("Destroyed: "+totalDestroyed+"\n");
			sb.append("\n\n");
		}
		return sb.toString();
	}
	
	public static synchronized BlockingQueue<MyHttpClient> getConnectionPool(String domain,int port) throws IOException{
		String key = buildPoolKey(domain, port);
		if(!pool.containsKey(key)){
			if(pool.size()==MAX_CONCURRENT_DOMAINS){
				pool.remove(list.remove(pool.size()-1));
			}
			BlockingQueue<MyHttpClient> connections = new LinkedBlockingQueue<MyHttpClient>();
			for(int i=0;i<MAX_CONNECTIONS_PER_DOMAIN;i++){
				MyHttpClient client = new MyHttpClient();
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
		BlockingQueue<MyHttpClient> q = pool.get(key);
		return q;
	}
	
	
	public static synchronized void releaseConnection(String domain, int port,MyHttpClient c){
		String key = buildPoolKey(domain, port);
		pool.get(key).add(c);
		LOGGER.info("CONNECTION RELEASED ON POOL ["+key+"]");
	}
	
	private static String buildPoolKey(String domain,int port){
		port = port<0?80:port;
		String key = domain+":"+port;
		return key;
	}
	
}
