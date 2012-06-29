package com.workflowintg.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.log4j.Logger;
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
		    DefaultHttpClient client = new DefaultHttpClient();
		    client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
		    HttpGet httpget = new HttpGet(url);
		    HttpResponse response = client.execute(httpget);
		    File f = new File(fileName);
		    f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			InputStream is =  response.getEntity().getContent();
			byte[] buffer = new byte[128];
	        int len = 0;
	        while ((len = is.read(buffer)) > 0) {
	           fos.write(buffer, 0, len);
	        }
	        fos.close();
	        is.close();
	        LOGGER.info("Image ["+url+"] downloaded");
			//IHttpRequest req = new GetRequest(url);
			//c = HttpConnectionPool.getConnectionPool(uri.getHost(), uri.getPort()).take();
//			IHttpResponse resp = c.call(req);
//			File f = new File(fileName);
//			if(resp.hasBody()){
//				f.createNewFile();
//				FileOutputStream fos = new FileOutputStream(f);
//				InputStream is = resp.getBody().toInputStream();
//				byte[] buffer = new byte[128];
//		        int len = 0;
//		        while ((len = is.read(buffer)) > 0) {
//		           fos.write(buffer, 0, len);
//		        }
//		        fos.close();
//		        is.close();
//		        LOGGER.info("Image ["+url+"] downloaded");
//			}
			//HttpConnectionPool.releaseConnection(uri.getHost(),uri.getPort(), c);
		} 
		catch(Exception e){
			LOGGER.error("Image ["+url+"] failed "+"\n"+fileName);
			e.printStackTrace();
		}
		
		
		
		
		return new TaskAsyncResult("success",t,this);
	}
	
	public synchronized void notifyFreeConnection(){
		notify();
	}

}
