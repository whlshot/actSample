package com.sample.personaltask;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

/*
 * 个人任务分配三种方式：
 * 一：写死在xml文件中
 * 二：使用流程变量（在xml文件中写#{userId}或者${userId}）
 * 三：使用类（在xml文件中，不要指定办理人）添加TaskListener
 * */
public class TestPersonTask {
	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition() {
		String name = "个人任务分配";
		String bpmn = "diagrams/personTask.bpmn";
		String png = "diagrams/personTask.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition2() {
		String name = "个人任务分配2";
		String bpmn = "diagrams/personTask2.bpmn";
		String png = "diagrams/personTask2.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition3() {
		String name = "个人任务分配3";
		String bpmn = "diagrams/personTask3.bpmn";
		String png = "diagrams/personTask3.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "personalTaskID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
	}

	@Test
	public void startProcessInstance2() {
		String processDefinitionKey = "personalTask2ID";
		Map<String, Object> variables = new HashMap<>();
		variables.put("userId", "张三丰");
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, variables);
	}

	@Test
	public void startProcessInstance3() {
		String processDefinitionKey = "personalTask3ID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
	}

	@Test
	public void queryPersonTask() {
		String assignee = "灭绝师太";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Task task = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).singleResult();
		System.out.println(task.getId());
	}

	@Test
	public void completePersonTask() {
		String taskId = "22504";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().complete(taskId);
	}

	@Test //可以分配个人任务从一个人到另一个人（认领任务）
	public void setAssigneeTask() {
		String taskId = "22504";
		String userId = "张翠山";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().setAssignee(taskId, userId);
	}
}
