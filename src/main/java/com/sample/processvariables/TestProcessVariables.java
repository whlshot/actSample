package com.sample.processvariables;

import java.util.Date;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
当一个javabean（实现序列化）放置到流程变量中，要求改Javabean的属性不能再发生变化（增加或者删除某个属性）
如果发生变化，再次从流程变量中获取时，就会抛出异常。

解决办法：生成一个序列化id: serialVersionUID（随机生成的那种），然后就可以修改了
 */
public class TestProcessVariables {

	@Test //部署流程定义
	public void testDeploymentProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("流程变量").addClasspathResource("diagrams/processVariables.bpmn").addClasspathResource("diagrams/processVariables.png").deploy();
		System.out.println(deployment);
	}

	@Test //启动流程实例
	public void testStartProcessInstance() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		String processDefinitionKey = "processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
		System.out.println(processInstance.getId());
	}

	@Test //设置流程变量
	public void testSetVariables() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();
		taskService.setVariable("22504", "请假天数", 3);//设置流程变量
		taskService.setVariable("22504", "请假日期", new Date());
		taskService.setVariable("22504", "请假原因", "回家探亲");
		//Person person=new Person();
		//taskService.setVariable("22504", "请假原因", person);//person需要实现序列化，person数据会存放到act_ge_bytearray表中。

	}

	@Test
	public void testGetVariables() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = processEngine.getTaskService();
		Integer days = (Integer) taskService.getVariable("22504", "请假天数");
		Date date = (Date) taskService.getVariable("22504", "请假日期");
		String resean = (String) taskService.getVariable("22504", "请假原因");
		System.out.println(days + "  " + date + "  " + resean);

	}

	@Test //模拟设置和获取流程变量的场景
	public void testSetAndGetVariables() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		TaskService taskService = processEngine.getTaskService();

		//----设置流程变量的值

		//runtimeService.setVariable(executionId, variableName, value);//表示使用执行对象的ID，和流程变量的名称，设置流程变量的值
		//runtimeService.setVariables(executionId, variables);//可以设置多个

		//taskService.setVariable(taskId, variableName, value);
		//taskService.setVariables(taskId, variables);

		//runtimeService.startProcessInstanceByKey(processDefinitionKey, variables)//启动流程实例的时候可以设置流程变量

		//taskService.complete(taskId, variables);//完成任务的时候可以设置流程变量

		//----获取流程变量的值

		//runtimeService.getVariable(executionId, variableName);
		//runtimeService.getVariables(executionId);
		//runtimeService.getVariables(executionId, variableNames);//获取指定流程变量名称的流程变量值
		//runtimeService.getVariable(executionId, variableName, variableClass);

		//taskService.getVariable(taskId, variableName);
		//taskService.getVariable(taskId, variableName, variableClass);
		//taskService.getVariables(taskId);
		//taskService.getVariables(taskId, variableNames);
	}

	@Test
	public void testHistoryVariables() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getHistoryService().createHistoricVariableInstanceQuery().variableName("请假天数").list();
	}
}
