package com.workflowintg.task;

import com.myworkflow.TaskResult;
import com.myworkflow.task.TaskAsync;
import com.myworkflow.task.TaskAsyncResult;
import com.workflowintg.workflow.WorkflowIntg;

public class TaskDownloadImages extends TaskAsync{

	private int totalImages = 10;
	private int processed = 0;
	private WorkflowIntg w;
	
	public TaskDownloadImages(WorkflowIntg w) {
		super(w);
		this.w = w;
	}

	@Override
	public void notifyAsyncTaskFinalization(TaskAsyncResult r) {
		this.incrementProcessed();
	}
	
	private synchronized void incrementProcessed(){
		this.processed++;
		this.notify();
	}

	@Override
	public synchronized  TaskResult runTask() {
//		for(int i = 0;i<totalImages;i++){
//			w.getWorkflowDefinition().getWorkflowDefinitionContext()
//			.queueAsyncTask("download-images",
//					new DownloadImage("www.mydomain.com/image_"+i, this));
//		}
//		
//		while (processed<totalImages){
//			try {
//				this.wait();
//			} 
//			catch (InterruptedException e) {
//				break;
//			}
//		}
		
		return new TaskResult("success", "TaskDownloadImages finished");
	}

}
