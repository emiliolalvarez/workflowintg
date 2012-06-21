package com.workflowintg.workflow;

import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowApplicationContext;
import com.workflowintg.partner.PartnerContext;

public class WorkflowIntg extends Workflow {

	private String partner;
	
	private PartnerContext context;
	
	
	public WorkflowIntg(WorkflowApplicationContext context, String name) {
		super(context, name);
	}
	
	public void setPartner(String partner){
		this.partner = partner;
	}
	
	public String getPartner(){
		return partner;
	}
	
	public void setPartnerContext(PartnerContext context){
		this.context = context;
	}
	
	public PartnerContext getPartnerContext(){
		return this.context;
	}

}
