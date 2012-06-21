package com.workflowintg.dispatcher;

import java.sql.ResultSet;

import org.eclipse.jetty.server.Server;

import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.TransitionDefinition;
import com.workflowintg.Configuration;
import com.workflowintg.context.WorkflowintgDefinitionContext;
import com.workflowintg.db.DbPool;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.dispatcher.rest.JettyLauncher;

public class JettyDispatcherServer{

	public static RequestQueue requestQueue;
	
	public static WorkflowintgDefinitionContext context;
	
	public static void main(String args[]){
		new JettyDispatcherServer().start();
	}
	
	public WorkflowintgDefinitionContext getWorkflowDefinition(){
		return context;
	}
	
	public void start(){
		try {
			Server svr = JettyLauncher.launchJetty();
			while (svr.isStarting()){
				Thread.sleep(100);
			}
//			requestQueue = new RequestQueue();
//			WorkflowintgDefinitionContext ctx = new WorkflowintgDefinitionContext("workflow.properties");
//			setUpWorkflowDefinition(ctx);
//			launchListeners(ctx);
			//ResultSet r = GuiceServletConfig.getDependencyInjector().getInstance(DbPool.class).getPool().getConnection().prepareStatement("Select 1").executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void launchListeners(WorkflowintgDefinitionContext ctx){
		int size = ctx.getWorkflowDefintionProperties().getIntProperty("executor.size.request-queue-listener");
		for(int i=0;i<size;i++){
			ctx.queueExecutorTask("request-queue-listener", new RequestQueueListener(ctx, requestQueue ));
		}
	}
}
