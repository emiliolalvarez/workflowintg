package com.workflowintg.task;

import com.workflow.task.Task;
import com.workflow.task.TaskResult;
import com.workflow.workflow.Workflow;

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
