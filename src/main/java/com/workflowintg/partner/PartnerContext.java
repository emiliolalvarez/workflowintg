package com.workflowintg.partner;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.myworkflow.main.Configuration;
import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.WorkflowApplicationContext;

public class PartnerContext extends WorkflowApplicationContext {
	
	private String fileUrl;
	private String localFile;
	private PartnerSession session;

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
	
	public void setPartnerSession(PartnerSession session){
		this.session = session;
	}
	
	public PartnerSession getPartnerSession(){
		return session;
	}
	
	public void setLocalFile(String fileName){
		localFile = fileName;
	}
	
	public String getLocalFileName(){
		return localFile;
	}

}
