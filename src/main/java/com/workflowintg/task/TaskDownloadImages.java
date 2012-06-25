package com.workflowintg.task;

import com.myworkflow.TaskResult;
import com.myworkflow.task.TaskAsync;
import com.myworkflow.task.TaskAsyncResult;
import com.workflowintg.ad.AdWorkflow;

public class TaskDownloadImages extends TaskAsync{

	private int totalImages = 10;
	private int processed = 0;
	private AdWorkflow w;
	
	public TaskDownloadImages(AdWorkflow w) {
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
		
		totalImages = w.getAd().getImages().size();
		for(String url:w.getAd().getImages()){
			w.getContext().queueAsyncTask("download_images",
					new DownloadImage(url, this));
		}
		
		while (processed<totalImages){
			try {
				this.wait();
			} 
			catch (InterruptedException e) {
				break;
			}
		}
		
		return new TaskResult("success", "TaskDownloadImages finished");
	}

}
