package com.workflowintg.task;

import com.workflow.task.TaskAsync;
import com.workflow.task.TaskResult;
import com.workflow.workflow.Workflow;
import com.workflowintg.workflow.WorkflowIntg;

public class TaskDownloadImages extends TaskAsync{

	private int totalImages = 10;
	private int processed = 0;
	private Workflow w;
	
	public TaskDownloadImages(Workflow w) {
		super(w);
		this.w = w;
	}

	@Override
	public void notifyAsyncTaskFinalization() {
		this.incrementProcessed();
		
	}
	
	private synchronized void incrementProcessed(){
		this.processed++;
		this.notify();
	}

	@Override
	public synchronized  TaskResult runTask() {
		for(int i = 0;i<totalImages;i++){
			w.getWorkflowDefinition().getWorkflowDefinitionContext()
			.queueAsyncTask("images_"+((WorkflowIntg)w).getPartner(),
					new DownloadImage("www.mydomain.com/image_"+i, this));
			
		}
		
		while (processed<totalImages){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return new TaskResult("success", "TaskDownloadImages finished");
	}

}
