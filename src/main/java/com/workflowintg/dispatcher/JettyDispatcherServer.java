package com.workflowintg.dispatcher;

import org.eclipse.jetty.server.Server;

import com.myworkflow.main.Configuration;
import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.TransitionDefinition;
import com.workflowintg.context.AdContext;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.dispatcher.rest.JettyLauncher;

public class JettyDispatcherServer{

	private static RequestQueue requestQueue;
	
	private AdContext adContext;
	
	private Configuration config;
	
	public static RequestQueue getRequestQueue(){
		return requestQueue;
	}
	
	
	
	public void start(){
		try {
			Server svr = JettyLauncher.launchJetty();
			while (svr.isStarting()){
				Thread.sleep(100);
			}
			config = GuiceServletConfig.getDependencyInjector().getInstance(Configuration.class);
			requestQueue = GuiceServletConfig.getDependencyInjector().getInstance(RequestQueue.class);
			adContext = GuiceServletConfig.getDependencyInjector().getInstance(AdContext.class);
			launchListeners();
			//ResultSet r = GuiceServletConfig.getDependencyInjector().getInstance(DbPool.class).getPool().getConnection().prepareStatement("Select 1").executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void launchListeners(){
		int size = config.getInt("executor.size.request-queue-listener");
		for(int i=0;i<size;i++){
			adContext.queueExecutorTask("request-queue-listener", new RequestQueueListener(adContext, requestQueue ));
		}
	}
}
