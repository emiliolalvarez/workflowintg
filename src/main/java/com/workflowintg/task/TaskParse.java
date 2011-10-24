package com.workflowintg.task;

import com.workflow.task.Task;
import com.workflow.task.TaskResult;
import com.workflow.workflow.Workflow;

public class TaskParse extends Task {

	public TaskParse(Workflow w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TaskResult runTask() {
		// TODO Auto-generated method stub
		return new TaskResult("success", "TaskParse finished");
	}

}
