package com.workflowintg.context;

import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.WorkflowApplicationContext;

public class WorkflowintgDefinitionContext extends WorkflowApplicationContext {
	public WorkflowintgDefinitionContext(TransitionDefinition transition,String propertiesFileName){
		super(transition,propertiesFileName);
	}
}
