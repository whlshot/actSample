package com.sample.parallelgateway;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

//并行网关
public class TestParallelGateWay {
	@Test // 部署流程定义
	public void develpomentProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 与流程定义和部署相关的Service 创建一个部署对象 从classpath的资源中加载，一次只能加载一个文件。 完成部署
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("并行网关").addClasspathResource("diagrams/paralleGateWay.bpmn").addClasspathResource("diagrams/paralleGateWay.png").deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test // 启动流程实例
	public void startProcessInstance() {
		String processDefinitionKey = "parallelgateway";// 建议使用key启动，会启动最新版本。
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 使用流程定义的key启动流程实例，key对应bpmn文件中<process>标签的id的属性值。
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
		System.out.println(processInstance.getId());// 流程实例id 2501
		System.out.println(processInstance.getProcessDefinitionId());// 流程定义id helloworld:1:4
	}

	@Test // 查看个人任务
	public void queryMyPersonTask() {
		String assignee = "王五";//张三任务完成之后，可以查看李四的任务
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 与正在执行的任务管理相关的Service  创建查询对象   指定个人任务查询，指定办理人  查询
		List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务办理人：" + task.getAssignee());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("流程实例ID： " + task.getProcessInstanceId());
				System.out.println("执行对象的ID：" + task.getExecutionId());
				System.out.println("流程定义ID：" + task.getProcessDefinitionId());
			}
		}
	}

	@Test // 完成个人任务
	public void completeMyPersonTask() {
		String taskId = "7502";// 参考任务ID:2504
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务，张三任务完成，不能再查看；可以查看李四的任务");
	}
}
