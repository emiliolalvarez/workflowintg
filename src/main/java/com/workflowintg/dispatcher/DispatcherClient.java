package com.workflowintg.dispatcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowDefinition;
import com.myworkflow.workflow.WorkflowObserver;

public class DispatcherClient extends Thread implements WorkflowObserver{
	
	private RequestQueue queue = new RequestQueue();
	private Socket socket = null;
	private WorkflowDefinition wd;
	private RequestQueueListener listener;
	
	public DispatcherClient(WorkflowDefinition workflowDefinition, Socket client){
		socket = client;
		wd = workflowDefinition;
		listener = new RequestQueueListener(wd,queue);
	}
	
	public void run(){
		try{
			
			this.setName("disptacher-client-thread");
			
			wd.subscribe(this);
			
			new Thread(listener).start();
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    socket.getInputStream()));
	
		    String inputLine;
		    
		    while ((inputLine = in.readLine()) != null) {
		    	out.println("Recieved: "+inputLine);
		    	queue.putMessage(inputLine);
		    }
		    //TODO Join workflow context threads 
	    	listener.stopService();
		    wd.getWorkflowDefinitionContext().finish();
		    out.close();
		    in.close();
		    socket.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public void notifyFinishedEvent(Workflow w) {
		
		System.out.println("++++++++++++++++++++++++++++++");
		System.out.println(w.getName()+" finished");
		System.out.println("++++++++++++++++++++++++++++++");
		
	}
	
}
