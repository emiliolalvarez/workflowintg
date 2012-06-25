package com.workflowintg.context;

import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.TransitionDefinition;

public class AdTransitionDefinition extends TransitionDefinition {
	
	public AdTransitionDefinition() {
		addTransition(new Transition("validate", "success", "download_images"));
		addTransition(new Transition("validate", "error", null));
		addTransition(new Transition("download_images", "success", "submit"));
		addTransition(new Transition("download_images", "error", null));
		addTransition(new Transition("submit","success",null));
		addTransition(new Transition("submit","error",null));
	}
	
}
