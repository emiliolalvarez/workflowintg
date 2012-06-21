package com.workflowintg.partner;

import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.WorkflowApplicationContext;

public class PartnerContext extends WorkflowApplicationContext {
	
	String fileUrl;
	String localFile;

	public PartnerContext(TransitionDefinition transition,String propertiesFileName) {
		super(transition,propertiesFileName);
	}
	
	public void setFileUrl(String fileUrl){
		this.fileUrl = fileUrl;
	}
	
	public String getFileUrl(){
		return fileUrl;
	}
	
	public void setLocalFile(String fileName){
		localFile = fileName;
	}
	
	public String getLocalFileName(){
		return localFile;
	}

}
