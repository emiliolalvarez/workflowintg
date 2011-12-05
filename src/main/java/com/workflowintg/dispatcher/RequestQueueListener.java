package com.workflowintg.dispatcher;

import com.workflow.task.TaskOnError;
import com.workflow.workflow.WorkflowDefinition;
import com.workflowintg.task.TaskDownloadImages;
import com.workflowintg.task.TaskParse;
import com.workflowintg.task.TaskSubmit;
import com.workflowintg.task.TaskValidate;
import com.workflowintg.workflow.WorkflowIntg;

public class RequestQueueListener extends Thread {
	
	private RequestQueue queue;
	private WorkflowDefinition wd;
	private boolean process = true;
	
	public RequestQueueListener(WorkflowDefinition wd,RequestQueue queue){
		this.setName("request-listener-thread");
		this.queue = queue;
		this.wd = wd;
	}
	
	public void run(){
		while(process){
			String message = queue.getMessage();
			if(process){
				System.out.println("Processing: "+message);
				WorkflowIntg w = wd.getWorkflowInstance(WorkflowIntg.class);
				w = new WorkflowIntg(wd,w.getName());
				w.setPartner("Partner_1");
				w.addTask("parse", new TaskParse(w));
				w.addTask("validate", new TaskValidate(w));
				w.addTask("download_images",new TaskDownloadImages(w));
				w.addTask("submit", new TaskSubmit(w));
				w.addTask("error", new TaskOnError(w));
				wd.getWorkflowDefinitionContext().getExecutor("request-listener-executor").execute(w);
			}
		}
	}
	
	public void stopService(){
		this.process=false;
		this.queue.putMessage("");
	}
	
}
