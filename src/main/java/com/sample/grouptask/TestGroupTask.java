package com.sample.grouptask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
组任务分配的三种方式
一：写死在xml文件中    candidate user：小A,小B,小C,小D
二：使用流程变量    candidate user：${userIds}
三：使用类：在xml中不做任何配置
 */
public class TestGroupTask {
	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition() {
		String name = "组任务分配";
		String bpmn = "diagrams/groupTask.bpmn";
		String png = "diagrams/groupTask.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition2() {
		String name = "组任务分配2";
		String bpmn = "diagrams/groupTask2.bpmn";
		String png = "diagrams/groupTask2.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition3() {
		String name = "组任务分配3";
		String bpmn = "diagrams/groupTask3.bpmn";
		String png = "diagrams/groupTask3.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "groupTaskID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
	}

	@Test
	public void startProcessInstance2() {
		String processDefinitionKey = "groupTask2ID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Map<String, Object> vaiables = new HashMap<>();
		vaiables.put("userIds", "黄蓉,郭靖,杨康,穆念慈");
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, vaiables);
	}

	@Test
	public void startProcessInstance3() {
		String processDefinitionKey = "groupTask3ID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
	}

	@Test
	public void queryPersonTask() {
		String assignee = "小B";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Task task = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).singleResult();
		System.out.println(task.getId());
	}

	@Test
	public void queryGroupTask() {
		String candidateUser = "小B";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Task task = processEngine.getTaskService().createTaskQuery().taskCandidateUser(candidateUser).singleResult();//组任务的办理人查询
		System.out.println(task.getId());
	}

	@Test //查询正在执行的任务办理人表
	public void queryRuntimeGroupPersonTask() {
		String taskId = "30004";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<IdentityLink> list = processEngine.getTaskService().getIdentityLinksForTask(taskId);
		for (IdentityLink identityLink : list) {
			System.out.println(identityLink.getTaskId() + "  ..  " + identityLink.getType() + "  ..  " + identityLink.getProcessDefinitionId() + "  ..  " + identityLink.getUserId());
		}
	}

	@Test //查询历史任务的办理人表
	public void queryHistoryGroupPersonTask() {
		String processInstanceId = "30001";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<HistoricIdentityLink> list = processEngine.getHistoryService().getHistoricIdentityLinksForProcessInstance(processInstanceId);
		for (HistoricIdentityLink historicIdentityLink : list) {
			System.out.println(historicIdentityLink.getTaskId() + "   " + historicIdentityLink.getType() + "  " + historicIdentityLink.getProcessInstanceId() + "   " + historicIdentityLink.getUserId());
		}
	}

	@Test //拾取任务，将组任务分配各个人任务(任务结束，从候选人中找出真正参与的人)  claim(索赔,认领,签收)
	public void claim() {
		String taskId = "10004";
		String userId = "杨过";//分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().claim(taskId, userId);
	}

	@Test //将个人任务回退到组任务(前提，之前一定是一个组任务)
	public void setAssignee() {
		String taskId = "30004";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().setAssignee(taskId, null);//设置为null即可，然后可以在重新认领签收
	}

	//向组任务中添加成员
	@Test
	public void addGroupPerson() {
		String taskId = "30004";
		String userId = "小E";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().addCandidateUser(taskId, userId);
	}

	//向组任务中删除成员
	@Test
	public void removeGroupPerson() {
		String taskId = "30004";
		String userId = "小C";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().deleteCandidateUser(taskId, userId);//act_ru_identitylink表中，依然有小C,但是他从候选者变成了参与者。
	}

	@Test
	public void completePersonTask() {
		String taskId = "10004";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().complete(taskId);
	}
}
/*
 * assignee为空：要么没有填写，要么是一个组任务
 * act_ru_identitylink表
 * 		type	participant：参与者（记录的是流程实例id）
 * 				candidate：候选者（记录的是任务id,任务还没有办理，所以称之为候选者）
 * 
 */
