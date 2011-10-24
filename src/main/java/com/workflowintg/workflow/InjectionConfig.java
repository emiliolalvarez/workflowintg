package com.workflowintg.workflow;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.workflow.workflow.Workflow;

public class InjectionConfig implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(Workflow.class).to(WorkflowIntg.class);		
	}

}
