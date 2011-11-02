package com.workflowintg;

import com.workflow.transition.Transition;
import com.workflow.workflow.WorkflowDefinition;
import com.workflow.workflow.WorkflowDefinitionContext;
import com.workflowintg.workflow.WorkflowIntg;

public class TestWorflowInstance {
	
	public static void main(String[] args){
		
		WorkflowDefinition wd = new WorkflowDefinition(new WorkflowDefinitionContext());
		wd.addTransition(new Transition("dummy", null, null));
		WorkflowIntg w = wd.getWorkflowInstance(WorkflowIntg.class);
		
		System.out.println("Class: "+w.getClass().getName());
		System.out.println("Workflow: "+w.getName());
		
	}
}