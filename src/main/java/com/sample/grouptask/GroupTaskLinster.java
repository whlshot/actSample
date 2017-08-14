package com.sample.grouptask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class GroupTaskLinster implements TaskListener {

	private static final long serialVersionUID = 4060653398098914059L;

	@Override
	public void notify(DelegateTask delegateTask) {

		//组任务：
		delegateTask.addCandidateUser("小龙女");
		delegateTask.addCandidateUser("杨过");
	}

}
