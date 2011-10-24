package com.workflowintg.dispatcher;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.workflow.task.TaskOnError;
import com.workflow.workflow.Workflow;
import com.workflow.workflow.WorkflowDefinition;
import com.workflowintg.task.TaskDownloadImages;
import com.workflowintg.task.TaskParse;
import com.workflowintg.task.TaskSubmit;
import com.workflowintg.task.TaskValidate;
import com.workflowintg.workflow.WorkflowIntg;

public class RequestQueueListener extends Thread {
	
	private RequestQueue queue;
	private WorkflowDefinition wd;
	private Executor executor;
	private boolean process = true;
	
	public RequestQueueListener(WorkflowDefinition wd,RequestQueue queue){
		this.queue = queue;
		this.wd = wd;
		executor = Executors.newFixedThreadPool(20);
	}
	
	public void run(){
		while(process){
			String message = queue.getMessage();
			System.out.println("Processing: "+message);
			Workflow w = wd.getWorkflowInstance();
			w = new WorkflowIntg(wd,w.getName());
			((WorkflowIntg)w).setPartner("Partner_1");
			w.addTask("parse", new TaskParse(w));
			w.addTask("validate", new TaskValidate(w));
			w.addTask("download_images",new TaskDownloadImages(w));
			w.addTask("submit", new TaskSubmit(w));
			w.addTask("error", new TaskOnError(w));
			executor.execute(w);
		}
	}
	
}
