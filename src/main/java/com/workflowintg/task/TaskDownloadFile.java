package com.workflowintg.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.xlightweb.GetRequest;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.client.HttpClient;

import com.myworkflow.TaskResult;
import com.myworkflow.task.Task;
import com.myworkflow.workflow.Workflow;
import com.workflowintg.partner.PartnerContext;

public class TaskDownloadFile extends Task {


	public TaskDownloadFile(Workflow w) {
		super(w);
	}

	@Override
	public TaskResult runTask(){
		try{
			
			PartnerContext context =(PartnerContext) this.getWorkflow().getContext();
			IHttpRequest req = new GetRequest(context.getFileUrl());
			HttpClient c = new HttpClient();
			
			c.setMaxRedirects(5);
			c.setMaxRetries(3);
			c.setMaxIdle(5);
			IHttpResponse resp = c.call(req);
			String fileName = "/tmp/parter-test.xml";
			context.setLocalFile(fileName);
			File f = new File(fileName);
			 
			if(!f.exists() && resp.hasBody()){
				FileOutputStream fos = new FileOutputStream(new File(fileName));
				int size = resp.getContentLength();
				System.out.println("File: "+fileName + " size:"+size);
				InputStream is = resp.getBody().toInputStream();
				byte[] buffer = new byte[10];
		        int len = 0;
		        while ((len = is.read(buffer)) > 0) {
		           fos.write(buffer, 0, len);
		        }
		        fos.close();
		        is.close();
			}
		}
		catch (Exception e) {
			return new TaskResult("error", "TaskDownloadFile failed");
		}
		// TODO Auto-generated method stub
		return new TaskResult("success", "TaskDownloadFile finished");
	}
}
