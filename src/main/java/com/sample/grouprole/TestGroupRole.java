package com.sample.grouprole;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

public class TestGroupRole {
	@Test //部署流程定义  repositoryService
	public void deployProcessDefinition() {
		String name = "用户角色组";
		String bpmn = "diagrams/groupRole.bpmn";
		String png = "diagrams/groupRole.png";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(name).addClasspathResource(bpmn).addClasspathResource(png).deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
		//添加用户角色组
		IdentityService identityService = processEngine.getIdentityService();
		//创建角色
		identityService.saveGroup(new GroupEntity("总经理"));
		identityService.saveGroup(new GroupEntity("部门经理"));
		//创建用户
		identityService.saveUser(new UserEntity("张三"));
		identityService.saveUser(new UserEntity("李四"));
		identityService.saveUser(new UserEntity("王五"));
		//建立用户和角色的关联关系
		identityService.createMembership("张三", "部门经理");
		identityService.createMembership("李四", "部门经理");
		identityService.createMembership("王五", "总经理");
		System.out.println("添加组织机构成功");
	}

	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "groupRoleTaskID";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
	}
}
