package com.workflowintg.ad;

import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowApplicationContext;
import com.workflowintg.partner.PartnerAd;

public class AdWorkflow extends Workflow {

	private String partner;
	
	private PartnerAd ad;
	
	public void setAd(PartnerAd ad){
		this.ad = ad;
	}
	
	public PartnerAd getAd(){
		return ad;
	}
	
	public AdWorkflow(WorkflowApplicationContext context, String name) {
		super(context, name);
	}
	
	public void setPartner(String partner){
		this.partner = partner;
	}
	
	public String getPartner(){
		return partner;
	}

}
