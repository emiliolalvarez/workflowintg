package com.workflowintg.dispatcher;

import java.io.IOException;
import java.net.ServerSocket;

import com.workflow.transition.Transition;
import com.workflow.workflow.WorkflowDefinition;
import com.workflowintg.context.WorkflowintgDefinitionContext;

public class DispatcherServer {
	
	private ServerSocket server = null;
	private int port = 9001;
	private boolean listening = true;
	private WorkflowDefinition workflowDefinition;
	
	public static void main (String[] args){
		new DispatcherServer().start();
	}
	
	public void start(){
		
		setUpWorkflowDefinition();
		
		try {
			server = new ServerSocket(port);
			while( listening ){
				new DispatcherClient(workflowDefinition,server.accept()).start();
			}
			server.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void setUpWorkflowDefinition(){
		//Injector injector = Guice.createInjector(new InjectionConfig()); 
		//workflowDefinition.setInjector(injector);
		workflowDefinition = new WorkflowDefinition(new WorkflowintgDefinitionContext());
		workflowDefinition.addTransition(new Transition("parse", "success", "validate"));
		workflowDefinition.addTransition(new Transition("validate", "success", "download_images"));
		workflowDefinition.addTransition(new Transition("download_images","success","submit"));
		workflowDefinition.addTransition(new Transition("submit","success",null));
		workflowDefinition.addTransition(new Transition("error",null,null));
		
	}

}
