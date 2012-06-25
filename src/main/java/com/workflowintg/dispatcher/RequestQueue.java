package com.workflowintg.dispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.workflowintg.partner.PartnerAd;

public class RequestQueue {
	
	private BlockingQueue<PartnerAd> queue;
	
	public RequestQueue(){
		queue = new LinkedBlockingQueue<PartnerAd>();
	}
	
	public void putMessage(PartnerAd message){
		queue.add(message);
	}
	
	public PartnerAd getMessage() throws InterruptedException  {
		return queue.take();
	}

}
