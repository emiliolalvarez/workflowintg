package com.workflowintg.partner;

import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowObserver;
import com.sun.istack.logging.Logger;
import com.workflowintg.task.TaskDownloadFile;
import com.workflowintg.task.TaskParse;

public class PartnerSession implements Runnable,WorkflowObserver {
	
	private boolean active = true;
	
	private String partner;
	
	public PartnerSession(String partner) {
		this.partner = partner;
	}
	
	public String getPartnerName(){
		return partner;
	}
	
	public void run() {
		
		System.out.println("Starting partner session: "+partner);
		
		TransitionDefinition transition = new TransitionDefinition();
		transition.addTransition(new Transition("download", "success", "parse"));
		transition.addTransition(new Transition("download", "error", null));
		transition.addTransition(new Transition("parse", "success", null));
		transition.addTransition(new Transition("parse", "error", null));
		
		PartnerContext context = new PartnerContext(transition,"workflow.properties");
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
