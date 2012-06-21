package com.workflowintg.context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.myworkflow.main.Configuration;
import com.myworkflow.transition.Transition;
import com.myworkflow.workflow.TransitionDefinition;

public class AdContextProvider implements Provider<AdContext>{
    @Inject
	private Configuration configuration;
    @Inject
    private TransitionDefinition transition; 
	
	public AdContext get() {
		System.out.println("AdContextProvider!!!!!!!!!");
		transition.addTransition(new Transition("validate", "success", "submit"));
		transition.addTransition(new Transition("validate", "error", null));
		transition.addTransition(new Transition("submit","success",null));
		transition.addTransition(new Transition("submit","error",null));
		AdContext context = new AdContext(transition, configuration);
		return context;
	}
}