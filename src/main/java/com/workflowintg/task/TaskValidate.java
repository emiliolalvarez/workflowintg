package com.workflowintg.task;

import com.myworkflow.TaskResult;
import com.myworkflow.task.Task;
import com.myworkflow.workflow.Workflow;

public class TaskValidate extends Task {

	public TaskValidate(Workflow w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TaskResult runTask() {
		// TODO Auto-generated method stub
		return new TaskResult("success","TaskValidate finished");
	}

}
