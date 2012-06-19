package com.workflowintg.workflow;

import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowDefinition;

public class WorkflowIntg extends Workflow {

	private String partner;
	
	private PartnerContext context;
	
	
	public WorkflowIntg(WorkflowDefinition workflowDefinition, String name) {
		super(workflowDefinition, name);
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
