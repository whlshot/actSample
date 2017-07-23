package com.sample.helloworld;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TestProcessDefinition {

	@Test //部署流程定义 classpath
	public void testDevelopmentDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("流程定义").addClasspathResource("diagrams/helloworld.bpmn").addClasspathResource("diagrams/helloworld.png").deploy();
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}

	@Test //部署流程定义 zip
	public void testDevelopmentDefinitionZip() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("流程定义").addZipInputStream(zipInputStream).deploy();
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}

	@Test //查询流程定义
	public void testQueryProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService().createProcessDefinitionQuery() //创建一个流程定义的查询
				//				.deploymentId(deploymentId) //使用部署对象ID查询
				//				.processDefinitionId(processDefinitionId)//使用流程定义ID查询
				//				.processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询
				//				.list();//返回一个集合列表，封装流程定义
				//				.singleResult();//返回唯一结果集
				//				.count();//结果集数量
				//				.listPage(firstResult, maxResults);//分页查询
				.count();
	}

	@Test //删除流程定义
	public void testDeleteProcessDefinition() {
		String deploymentId = "1";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService().deleteDeployment(deploymentId);//不带级联删除（如果流程启动就会抛出异常），还有一种级联删除
	}

	@Test //查看流程图
	public void viewPic() throws Exception {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		String deploymentId = "15001";
		String resourceName = null;
		//获取图片资源名称
		List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
		if (list != null && list.size() > 0) {
			for (String name : list) {
				if (name.indexOf("png") >= 0) {
					resourceName = name;
				}
			}
		}
		InputStream inputStream = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
		//将图片生成后放到指定目录下
		File file = new File("d:/" + resourceName);
		FileUtils.copyInputStreamToFile(inputStream, file);
	}

	@Test //查询最新版本的流程定义
	public void testLastVersionProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();
		Map<String, ProcessDefinition> map = new LinkedHashMap<>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition processDefinition : list) {
				map.put(processDefinition.getKey(), processDefinition);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<>(map.values());
		for (ProcessDefinition processDefinition : pdList) {
			System.out.println(processDefinition);
		}
	}
}
