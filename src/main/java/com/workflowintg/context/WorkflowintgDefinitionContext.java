package com.workflowintg.context;

import com.myworkflow.main.Configuration;
import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.WorkflowApplicationContext;

public class WorkflowintgDefinitionContext extends WorkflowApplicationContext {
	public WorkflowintgDefinitionContext(TransitionDefinition transition,Configuration configuration){
		super(transition,configuration);
	}
}
