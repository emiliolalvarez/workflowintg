package com.workflowintg.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.log4j.Logger;

public class HttpConnectionPool {

	private final static int MAX_CONCURRENT_DOMAINS = 3;
	private final static int MAX_CONNECTIONS_PER_DOMAIN = 10;
	private static Map<String, PoolingClientConnectionManager> pool = new HashMap<String, PoolingClientConnectionManager>();
	private static List<String> list = new LinkedList<String>();
	private static Logger LOGGER = Logger.getLogger(HttpConnectionPool.class);

	public static Map<String, PoolingClientConnectionManager> getPools() {
		return pool;
	}

	public synchronized static String getinfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP CONNECTION POOL STATUS\n");
		sb.append("--------------------------------------------\n");
		for (String k : pool.keySet()) {
			PoolingClientConnectionManager q = pool.get(k);
			PoolStats stats = q.getTotalStats();
			stats.getAvailable();
			sb.append(k + ":\n");
			sb.append("----------------------------"
					+ "---------------------------------------" + "\n");
			sb.append("Available:" + stats.getAvailable() + "\n");
			sb.append("Leased:" + stats.getLeased() + "\n");
			sb.append("Pending:" + stats.getPending() + "\n");
			sb.append("Max:" + stats.getMax() + "\n");
			sb.append("\n\n");
		}
		return sb.toString();
	}

	public static synchronized PoolingClientConnectionManager getConnectionPool(
			String domain, int port) throws IOException {

		port = port < 0 ? 80 : port;
		String key = buildPoolKey(domain, port);

		if (!pool.containsKey(key)) {
			if (pool.size() == MAX_CONCURRENT_DOMAINS) {
				pool.remove(list.remove(pool.size() - 1));
			}

			SchemeRegistry schemeRegistry = new SchemeRegistry();

			switch (port) {
			case 443:
				schemeRegistry.register(new Scheme("https", port,
						SSLSocketFactory.getSocketFactory()));
				break;
			default:
				schemeRegistry.register(new Scheme("http", port,
						PlainSocketFactory.getSocketFactory()));
				break;
			}

			PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
					schemeRegistry);
			cm.setMaxTotal(MAX_CONNECTIONS_PER_DOMAIN);
			cm.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_DOMAIN);
			HttpHost host = new HttpHost(domain, port);
			cm.setMaxPerRoute(new HttpRoute(host), MAX_CONNECTIONS_PER_DOMAIN);
			LOGGER.info("CONNECTION POOL CREATED FOR " + domain + ":" + port);
			pool.put(key, cm);
			list.add(0, key);
		}
		PoolingClientConnectionManager q = pool.get(key);
		return q;
	}

	private static String buildPoolKey(String domain, int port) {
		port = port < 0 ? 80 : port;
		String key = domain + ":" + port;
		return key;
	}

}
