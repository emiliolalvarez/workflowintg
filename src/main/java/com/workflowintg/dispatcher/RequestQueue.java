package com.workflowintg.dispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {
	
	private BlockingQueue<String> queue;
	
	public RequestQueue(){
		queue = new LinkedBlockingQueue<String>();
	}
	
	public void putMessage(String message){
		queue.add(message);
	}
	
	public String getMessage() throws InterruptedException  {
		return queue.take();
	}

}
