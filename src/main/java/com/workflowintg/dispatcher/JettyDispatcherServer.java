package com.workflowintg.dispatcher;

import java.sql.ResultSet;

import org.eclipse.jetty.server.Server;

import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.WorkflowDefinition;
import com.workflowintg.Configuration;
import com.workflowintg.context.WorkflowintgDefinitionContext;
import com.workflowintg.db.DbPool;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.dispatcher.rest.JettyLauncher;

public class JettyDispatcherServer{

	public static RequestQueue requestQueue;
	
	public static WorkflowDefinition workflowDefinition;
	
	public static void main(String args[]){
		new JettyDispatcherServer().start();
	}
	
	public WorkflowDefinition getWorkflowDefinition(){
		return workflowDefinition;
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
	
	public void setUpWorkflowDefinition(WorkflowintgDefinitionContext ctx){
		workflowDefinition = new WorkflowDefinition(ctx);
 	    workflowDefinition.addTransition(new Transition("download_file", "success", "parse"));
		workflowDefinition.addTransition(new Transition("download_file", "error", "error"));
		workflowDefinition.addTransition(new Transition("parse", "success", "validate"));
		workflowDefinition.addTransition(new Transition("parse", "error", "error"));
		workflowDefinition.addTransition(new Transition("validate", "success", "download_images"));
		workflowDefinition.addTransition(new Transition("download_images","success","submit"));
		workflowDefinition.addTransition(new Transition("submit","success",null));
		workflowDefinition.addTransition(new Transition("error",null,null));
	}
	
	public void launchListeners(WorkflowintgDefinitionContext ctx){
		int size = ctx.getWorkflowDefintionProperties().getIntProperty("executor.size.request-queue-listener");
		for(int i=0;i<size;i++){
			ctx.queueExecutorTask("request-queue-listener", new RequestQueueListener(workflowDefinition, requestQueue ));
		}
	}
}
