package com.workflowintg.task;

import com.workflow.task.TaskAsync;
import com.workflow.task.TaskCallable;

public class DownloadImage extends TaskCallable{
	
	private TaskAsync t;
	private String url;
	
	public DownloadImage(String url,TaskAsync t) {
		super(t);
		this.t = t;
		this.url = url;
		
	}

	@Override
	public TaskAsync call() {
		long sleep = Math.round(Math.random() * 2000);
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Downloaded "+url);
		return t;
	}

}
