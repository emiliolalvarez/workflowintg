package com.workflowintg.context;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.myworkflow.main.Configuration;
import com.myworkflow.workflow.TransitionDefinition;
import com.myworkflow.workflow.WorkflowApplicationContext;

public class AdContext extends WorkflowApplicationContext {
	
	@Inject
	public AdContext(@Named("Ad") TransitionDefinition transition, Configuration configuration) {
		super(transition, configuration);
	}

}
