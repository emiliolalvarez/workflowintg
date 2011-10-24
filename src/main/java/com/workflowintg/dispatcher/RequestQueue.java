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
	
	public String getMessage(){
		
		try {
			return queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
