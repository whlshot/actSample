package com.sample.helloworld;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class TestProcessInstance {

	@Test //部署流程定义
	public void testDeploymentProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 与流程定义和部署相关的Service 创建一个部署对象 从classpath的资源中加载，一次只能加载一个文件。 完成部署
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("helloworld入门程序").addClasspathResource("diagrams/helloworld.bpmn").addClasspathResource("diagrams/helloworld.png").deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test //启动流程实例
	public void startProcessInstance() {
		String processDefinitionKey = "helloworld";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
	}

	@Test //查询流程状态（判断流程正在执行，还是已经结束）
	public void testProcessEnd() {
		String processInstanceId = "17501";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			System.out.println("流程已经结束了");
		} else {
			System.out.println("流程没有结束");
		}
	}

	@Test //查询历史流程任务
	public void testHistoryTask() {
		String assignee = "张三";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(assignee).list();
	}

	@Test //查询历史的流程实例
	public void testHistoryProcessInstance() {
		String processInstanceId = "";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).list();
	}
}
