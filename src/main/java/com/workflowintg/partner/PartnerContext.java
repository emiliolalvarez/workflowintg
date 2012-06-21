package com.workflowintg.partner;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.myworkflow.main.Configuration;
import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.WorkflowApplicationContext;

public class PartnerContext extends WorkflowApplicationContext {
	
	String fileUrl;
	String localFile;

	@Inject
	public PartnerContext(@Named("Partner")TransitionDefinition transition,Configuration configuration) {
		super(transition,configuration);
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
