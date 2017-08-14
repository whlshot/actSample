package com.sample.receivetask;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 
 接收任务活动（receiveTask,）
接收任务是一个简单的任务，它会等待对应的消息的到达。当流程达到接收任务，流程状态会保存到数据库中。
在任务创建后，意味着流程会进入等待状态，直到引擎接收了一个特定的消息，这会触发流程穿过接收任务去继续执行。
 */
public class TestReceiveTask {

	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition() {
		String name = "接收活动任务";
		String bpmn = "diagrams/receiveTask.bpmn";
		String png = "diagrams/receiveTask.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test //启动流程实例 runtimeService
	public void startProcessInstance() {
		String processDefinitionKey = "receiveTaskProcessID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
		Execution execution1 = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(processInstance.getId()).activityId("receivetask1").singleResult();
		/*使用流程变量设置当日销售额，用来传递业务参数*/
		String variableName = "汇总当日销售额";
		Integer value = 20000;
		processEngine.getRuntimeService().setVariable(execution1.getId(), variableName, value);
		/*向后执行一步。如果流程处于等待状态，使得流程继续执行*/
		processEngine.getRuntimeService().signal(execution1.getId());
		/*从流程变量中获取变量值*/
		Execution execution2 = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(processInstance.getId()).activityId("receivetask2").singleResult();
		Integer valueVariable = (Integer) processEngine.getRuntimeService().getVariable(execution2.getId(), variableName);
		System.out.println("给老板发送短信，当日销售额是：" + valueVariable);
		/*向后执行一步。如果流程处于等待状态，使得流程继续执行*/
		processEngine.getRuntimeService().signal(execution2.getId());
	}
}
