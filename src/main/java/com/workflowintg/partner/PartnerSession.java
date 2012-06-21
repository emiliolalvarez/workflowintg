package com.workflowintg.partner;

import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowObserver;
import com.sun.istack.logging.Logger;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.task.TaskDownloadFile;
import com.workflowintg.task.TaskParse;

public class PartnerSession implements Runnable,WorkflowObserver {
	
	private String partner;
	
	public PartnerSession(String partner) {
		this.partner = partner;
	}
	
	public String getPartnerName(){
		return partner;
	}
	
	public void run() {
		
		System.out.println("Starting partner session: "+partner);
		PartnerContext context = GuiceServletConfig.getDependencyInjector().getInstance(PartnerContext.class);
		
		context.setFileUrl("http://www.gestionaleimmobiliare.it/export/xml/trovit_com/prossima-casa_it.xml");
		context.subscribe(this);
		
		Workflow w = context.getWorkflowInstance(Workflow.class);
		w.addTask("download", new TaskDownloadFile(w));
		w.addTask("parse", new TaskParse(w));
		
		context.queueExecutorTask(partner,w);
		
	}

	public void notifyFinishedEvent(Workflow w) {
		Logger.getLogger(PartnerSession.class).info("WORKFLOW OBJECT FINISHED!");
	}
	

}
