package com.workflowintg.dispatcher;

import org.apache.log4j.Logger;

import com.myworkflow.TaskOnError;
import com.myworkflow.workflow.WorkflowApplicationContext;
import com.workflowintg.ad.AdWorkflow;
import com.workflowintg.partner.PartnerAd;
import com.workflowintg.task.TaskDownloadImages;
import com.workflowintg.task.TaskSubmit;
import com.workflowintg.task.TaskValidate;

public class RequestQueueListener implements Runnable {
	
	private RequestQueue queue;
	private WorkflowApplicationContext context;
	private boolean process = true;
	private final Logger LOGGER = Logger.getLogger(RequestQueueListener.class);
	
	public RequestQueueListener(WorkflowApplicationContext context,RequestQueue queue){
		this.queue = queue;
		this.context = context;
		
	}
	
	public void run(){
		LOGGER.info("Request Queue Listener launched!");
		while(process){
			try{
				PartnerAd message = queue.getMessage();
			    if(message != null){
					AdWorkflow w = context.getWorkflowInstance(AdWorkflow.class);
					w.setAd(message);
					w.setPartner(message.getPartner());
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
		
		LOGGER.info("Request Queue Listener stopped!");
	}
	
	public void stopService(){
		this.process=false;
		this.queue.putMessage(null);
	}
	
}
