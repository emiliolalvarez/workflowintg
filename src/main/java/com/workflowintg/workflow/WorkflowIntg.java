package com.workflowintg.workflow;

import com.workflow.workflow.Workflow;
import com.workflow.workflow.WorkflowDefinition;

public class WorkflowIntg extends Workflow {

	private String partner;
	
	public WorkflowIntg(WorkflowDefinition workflowDefinition, String name) {
		super(workflowDefinition, name);
	}
	
	public void setPartner(String partner){
		this.partner = partner;
	}
	
	public String getPartner(){
		return partner;
	}

}
