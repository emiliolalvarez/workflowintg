package org.xlightweb.client;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xlightweb.FutureResponseHandler;
import org.xlightweb.IFutureResponse;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpResponse;
import org.xlightweb.InvokeOn;
import org.xlightweb.client.HttpClient;


public class MyHttpClient extends HttpClient{
	
	private static final Logger LOG = Logger.getLogger(MyHttpClient.class.getName());
	private static int MAX_TIMEOUT_FOR_REQUEST = 5000; //Default Value (in milliseconds)
	
	public MyHttpClient() {
		super();
	}
	
	@Override
	public IHttpResponse call(IHttpRequest request) throws IOException, SocketTimeoutException {
		//return super.call(request);
		
	    if (getCallReturnOnMessage()) {
	        try {
	            request.setAttribute(org.xlightweb.client.RetryHandler.RETRY_KEY, false);
	            FutureMessageResponseHandler responseHandler = new FutureMessageResponseHandler(request);
	            send(request, responseHandler);
	            return responseHandler.getResponse(MAX_TIMEOUT_FOR_REQUEST, TimeUnit.MILLISECONDS);
	        } catch (Exception ie) {
	            throw new RuntimeException(ie);
	        }

	    } else {
    		try {
    			IFutureResponse futureResponse = send(request);
    			return futureResponse.getResponse(MAX_TIMEOUT_FOR_REQUEST, TimeUnit.MILLISECONDS);
    		} catch (InterruptedException ie) {
    			throw new RuntimeException(ie);
    		}
	    }

	}
	// TODO: When change the xlightweb API, change this code as well.
	@InvokeOn(InvokeOn.MESSAGE_RECEIVED)
	private final class FutureMessageResponseHandler extends FutureResponseHandler {
	    
	    private final IHttpRequest request;
	    private final int currentRetryNum;
	    
	    public FutureMessageResponseHandler(IHttpRequest request) {
	        this.request = request;
	        
	        Integer numReq = (Integer) request.getAttribute("org.xlightweb.client.FutureMessageResponseHandler.currentRetryNum");
	        if (numReq == null) {
	            numReq = 0;
	        }
	        
	        currentRetryNum = numReq + 1;
	        request.setAttribute("org.xlightweb.client.FutureMessageResponseHandler.currentRetryNum", currentRetryNum);	        
        }
	    
	    @Override
	    public void onException(IOException ioe) throws IOException {
	        if (isRetryableMethod() && RetryHandler.isRetryable(ioe)) {
	            if (currentRetryNum < getMaxRetries()) {
	                if (LOG.isLoggable(Level.FINE)) {
	                    LOG.fine("try to retrying request (retry num " +  currentRetryNum + "). I/O exception  caught when processing request " + ioe.toString());
	                }
	                retry();
	                return;
	                
	            } else {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("max retries " + getMaxRetries() + ". I/O exception  caught when processing request " + ioe.toString());
                    }
                }
	        } 
	        
	        super.onException(ioe);
	    }
	    
	    @Override
	    public void onException(SocketTimeoutException stoe) {
	        if (isRetryableMethod()) {
	            if (currentRetryNum < getMaxRetries()) {
	                if (LOG.isLoggable(Level.FINE)) {
	                    LOG.fine("try to retrying request (retry num " +  currentRetryNum + "). I/O exception  caught when processing request " + stoe.toString());
	                }
	                retry();
	                return;
	                
	            } else {
	                if (LOG.isLoggable(Level.FINE)) {
	                    LOG.fine("max retries " + getMaxRetries() + ". I/O exception  caught when processing request " + stoe.toString());
	                }
	            }
	        }
	        
	        super.onException(stoe);
	    }
	    
	    
	    private void retry() {
	        
	        Runnable task = new Runnable() {  
	            
	            public void run() {
        	        try {
        	            IHttpResponse response = call(request);
        	            onResponse(response);
        	        } catch (IOException ioe) {
        	            try {
        	                FutureMessageResponseHandler.super.onException(ioe);
        	            } catch (IOException e) {
        	                if (LOG.isLoggable(Level.FINE)) {
        	                    LOG.fine("error occured by calling onException " + e.toString());
        	                }
        	                
        	            }
        	        }
	            }
	        };
	        
	    }
	    
	    
	    private boolean isRetryableMethod() {
	        return (request.getMethod().equalsIgnoreCase("GET"));
	    }
	}
	
	public static final int getMaxTimeoutForRequest() {
		return MyHttpClient.MAX_TIMEOUT_FOR_REQUEST;
	}
	
	public static final void setMaxTimeoutForRequest(int maxTimeOutForRequest) {
		MyHttpClient.MAX_TIMEOUT_FOR_REQUEST = maxTimeOutForRequest;
	}

}
