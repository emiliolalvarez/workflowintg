package com.workflowintg.workflow;

public class PartnerContext {
	
	private String partner;
	
	private String fileName;
	
	public void setPartner(String partnerName){
		this.partner = partnerName;
	}
	
	public String getPartner(){
		return this.partner;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}

}