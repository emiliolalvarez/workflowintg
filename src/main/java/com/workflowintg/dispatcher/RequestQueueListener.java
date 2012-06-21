package com.workflowintg.dispatcher;

import com.myworkflow.TaskOnError;
import com.myworkflow.workflow.WorkflowApplicationContext;
import com.workflowintg.task.TaskDownloadFile;
import com.workflowintg.task.TaskDownloadImages;
import com.workflowintg.task.TaskParse;
import com.workflowintg.task.TaskSubmit;
import com.workflowintg.task.TaskValidate;
import com.workflowintg.workflow.WorkflowIntg;

public class RequestQueueListener implements Runnable {
	
	private RequestQueue queue;
	private WorkflowApplicationContext context;
	private boolean process = true;
	
	public RequestQueueListener(WorkflowApplicationContext context,RequestQueue queue){
		this.queue = queue;
		this.context = context;
		
	}
	
	public void run(){
		System.out.println("Request Queue Listener launched!");
		while(process){
			try{
			    String message = queue.getMessage();
			    if(message != null){
			    	System.out.println("Processing: "+message);
					WorkflowIntg w = context.getWorkflowInstance(WorkflowIntg.class);
					w.setPartner("Partner_1");
					w.addTask("download_file", new TaskDownloadFile(w));
					w.addTask("parse", new TaskParse(w));
					w.addTask("validate", new TaskValidate(w));
					w.addTask("download_images",new TaskDownloadImages(w));
					w.addTask("submit", new TaskSubmit(w));
					w.addTask("error", new TaskOnError(w));
					context.queueExecutorTask("workflow-executor", w);
					
			    }
			}
			catch(Exception ex){
				process = false;
			}
		}
		System.out.println("Request Queue Listener stopped!");
	}
	
	public void stopService(){
		this.process=false;
		this.queue.putMessage("");
	}
	
}
