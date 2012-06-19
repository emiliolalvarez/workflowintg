package com.workflowintg;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

import org.xlightweb.BodyDataSource;
import org.xlightweb.GetRequest;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.client.HttpClient;
import org.xlightweb.client.HttpClientConnection;

import com.workflowintg.task.ConnectionWrapper;
import com.workflowintg.task.ImageServerPool;

public class TestImageDownload {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestImageDownload().downloadTest();	
	}
	
	public void downloadTest(){
		
		try{
			for(int i=0;i<10;i++){
				new Thread(new Runnable() {
					
					public void run() {
						try{
							URL url = new URL("http://t3.gstatic.com/images?q=tbn:ANd9GcS5_wuKzW9N-MFmo-GxSze57iUOxFW7dNNYLzS1lFPzYhjcTluI");
							ConnectionWrapper wrapper = null;
									
							while(wrapper==null){
								wrapper = ImageServerPool.getConnection(url.getHost(),url.getPort()==-1?80:url.getPort());
								if(wrapper==null){
									Thread.sleep(100);
								}
							}
							HttpClientConnection conn = wrapper.getConnection(); 
							//IHttpRequest req = new GetRequest(url.getPath()+"?"+url.getQuery());
							IHttpRequest req = new GetRequest("http://t0.gstatic.com/images?q=tbn:ANd9GcRm7WjYS5w_H62NPBupHyI7_trVLtEUjls6GBYbYstlmHHwLyxmJg");
							HttpClient c = new HttpClient();
							c.setMaxRedirects(5);
							c.setMaxRetries(3);
							c.setMaxIdle(5);
							IHttpResponse resp = c.call(req);
							List<String> l = c.getActiveConnectionInfos();
							for(String s:l){
								System.out.println(s);
							}
							
							
							System.out.println("Sending request");
							//IHttpResponse resp = conn.call(req);
							System.out.println("Status: "+resp.getStatus());
							BodyDataSource bds = resp.getBody();
							FileOutputStream fos = new FileOutputStream("/tmp/test/"+url.getFile()+(Thread.currentThread().getName()));
							fos.write(bds.readBytes());
							fos.close();
							Thread.sleep(4000);
							wrapper.setIsInUse(false);
						}
						catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}).start(); 
			
			}
			ImageServerPool.close(); 
			
			
			//conn.call(new GetRequest("http://t3.gstatic.com/images?q=tbn:ANd9GcS5_wuKzW9N-MFmo-GxSze57iUOxFW7dNNYLzS1lFPzYhjcTluI"));
			//IHttpRequest request = new GetRequest("http://t3.gstatic.com/images?q=tbn:ANd9GcS5_wuKzW9N-MFmo-GxSze57iUOxFW7dNNYLzS1lFPzYhjcTluI");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
