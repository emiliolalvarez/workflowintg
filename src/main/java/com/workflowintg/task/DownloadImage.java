package com.workflowintg.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.log4j.Logger;
import org.xlightweb.GetRequest;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.client.MyHttpClient;

import com.myworkflow.task.TaskAsync;
import com.myworkflow.task.TaskAsyncResult;
import com.myworkflow.task.TaskCallable;
import com.workflowintg.ad.AdWorkflow;
import com.workflowintg.utils.HttpConnectionPool;

public class DownloadImage extends TaskCallable{
	
	private TaskAsync t;
	private String url;
	private Logger LOGGER = Logger.getLogger(DownloadImage.class);
	
	public DownloadImage(String url,TaskAsync t) {
		super(t);
		this.t = t;
		this.url = url;
	}

	@Override
	public TaskAsyncResult call() {
		String fileName = null;
		URI uri = null;
		MyHttpClient c = null;
		try {
			uri = new URI(url);
			fileName = "/tmp/"+((AdWorkflow)t.getWorkflow()).getPartner()+"-"+uri.getPath().replace("/", "");
			
		    LOGGER.info("Image ["+url+"] downloaded");
			
			IHttpRequest req = new GetRequest(url);
			c = HttpConnectionPool.getConnectionPool(uri.getHost(), uri.getPort()).take();
			IHttpResponse resp = c.call(req);
			File f = new File(fileName);
			if(resp.hasBody()){
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				InputStream is = resp.getBody().toInputStream();
				byte[] buffer = new byte[128];
		        int len = 0;
		        while ((len = is.read(buffer)) > 0) {
		           fos.write(buffer, 0, len);
		        }
		        fos.close();
		        is.close();
		        LOGGER.info("Image ["+url+"] downloaded");
			}
			
		} 
		catch(Exception e){
			LOGGER.error("Image ["+url+"] failed "+"\n"+fileName);
			e.printStackTrace();
		}
		
		HttpConnectionPool.releaseConnection(uri.getHost(),uri.getPort(), c);
		
		
		return new TaskAsyncResult("success",t,this);
	}
	
	public synchronized void notifyFreeConnection(){
		notify();
	}

}
