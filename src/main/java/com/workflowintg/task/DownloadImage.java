package com.workflowintg.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.myworkflow.task.TaskAsync;
import com.myworkflow.task.TaskAsyncResult;
import com.myworkflow.task.TaskCallable;
import com.workflowintg.ad.AdWorkflow;
import com.workflowintg.utils.HttpConnectionPool;

public class DownloadImage extends TaskCallable {

	private TaskAsync t;
	private String url;
	private Logger LOGGER = Logger.getLogger(DownloadImage.class);

	public DownloadImage(String url, TaskAsync t) {
		super(t);
		this.t = t;
		this.url = url;
	}

	@Override
	public TaskAsyncResult call() {
		String fileName = null;
		URI uri = null;
		try {
			uri = new URI(url);
			fileName = "/tmp/" + ((AdWorkflow) t.getWorkflow()).getPartner()
					+ "-" + uri.getPath().replace("/", "");
			LOGGER.info("Image [" + url + "] downloaded");
			HttpGet httpget = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient(
					HttpConnectionPool.getConnectionPool(uri.getHost(),
							uri.getPort()));
			httpClient.getParams().setIntParameter(ClientPNames.MAX_REDIRECTS, 7);
			try {
				HttpResponse response = httpClient.execute(httpget,
						new BasicHttpContext());
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// do something useful with the entity
					File f = new File(fileName);
					f.createNewFile();
					FileOutputStream fos = new FileOutputStream(f);
					InputStream is = entity.getContent();
					byte[] buffer = new byte[128];
					int len = 0;
					while ((len = is.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					is.close();
					LOGGER.info("Image [" + url + "] downloaded");
				}
				// ensure the connection gets released to the manager
				EntityUtils.consume(entity);
			    
			} catch (Exception ex) {
				httpget.abort();
			}
		} catch (Exception e) {
			LOGGER.error("Image [" + url + "] failed " + "\n" + fileName);
			e.printStackTrace();
		}
		return new TaskAsyncResult("success", t, this);
	}

	public synchronized void notifyFreeConnection() {
		notify();
	}

}
