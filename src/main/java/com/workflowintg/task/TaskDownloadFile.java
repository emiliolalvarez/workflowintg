package com.workflowintg.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xlightweb.GetRequest;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.client.HttpClient;

import com.myworkflow.TaskResult;
import com.myworkflow.task.Task;
import com.myworkflow.workflow.Workflow;
import com.workflowintg.partner.PartnerContext;

public class TaskDownloadFile extends Task {

	private Logger LOGGER = Logger.getLogger(TaskDownloadFile.class);	

	public TaskDownloadFile(Workflow w) {
		super(w);
	}

	@Override
	public TaskResult runTask(){
		String fileName = "";
		fileName = "/tmp/parter-test.xml";
		try{
			PartnerContext context =(PartnerContext) this.getWorkflow().getContext();
			IHttpRequest req = new GetRequest(context.getFileUrl());
			HttpClient c = new HttpClient();
			
			c.setMaxRedirects(5);
			c.setMaxRetries(3);
			
			IHttpResponse resp = c.call(req);
			context.setLocalFile(fileName);
			File f = new File(fileName);
			 
			if(!f.exists() && resp.hasBody()){
				FileOutputStream fos = new FileOutputStream(new File(fileName));
				int size = resp.getContentLength();
				LOGGER.info("Downloading file: "+fileName+" size: "+size+" bytes");
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
			LOGGER.error("Could not download: "+fileName);
			return new TaskResult("error", "TaskDownloadFile failed");
		}
		LOGGER.info("File: "+fileName+" downloaded");
		return new TaskResult("success", "TaskDownloadFile finished");
	}
}
