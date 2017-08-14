package com.sample.personaltask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class UserTaskLinster implements TaskListener {

	private static final long serialVersionUID = -2251546726956706706L;

	/**
	 * 用来指定任务的办理人
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		//指定个人任务的办理人，也可以指定组任务的办理人
		//个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
		String assignee = "灭绝师太";//假设从数据库中查询完毕
		delegateTask.setAssignee(assignee);
	}

}
