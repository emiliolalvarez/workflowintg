package com.workflowintg.partner;

import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.TransitionDefinition;

public class PartnerTransitionDefinition extends TransitionDefinition {
	
	public PartnerTransitionDefinition() {
		addTransition(new Transition("download", "success", "parse"));
		addTransition(new Transition("download", "error", null));
		addTransition(new Transition("parse", "success", null));
		addTransition(new Transition("parse", "error", null));
	}
}
