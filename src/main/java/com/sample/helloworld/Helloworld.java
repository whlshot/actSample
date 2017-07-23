package com.sample.helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
* RepositoryService:管理流程定义
* RuntimeService:执行管理，包括启动、推进、删除流程实例等操作
* TaskService:任务管理
* HistoryService:历史管理（执行完的数据的管理）
* IdentityService:组织机构管理
* FormService:一个可选服务，任务表单管理
* ManagerService
*/
public class Helloworld {

	@Test // 部署流程定义
	public void develpomentProcessDefinition() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 与流程定义和部署相关的Service 创建一个部署对象 从classpath的资源中加载，一次只能加载一个文件。 完成部署
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name("helloworld入门程序").addClasspathResource("diagrams/helloworld.bpmn").addClasspathResource("diagrams/helloworld.png").deploy();
		System.out.println("Id: " + deployment.getId() + " name: " + deployment.getName() + "" + deployment.getTenantId() + "" + deployment.getDeploymentTime() + "" + deployment.getCategory());
	}

	@Test // 启动流程实例
	public void startProcessInstance() {
		String processDefinitionKey = "helloworld";// 建议使用key启动，会启动最新版本。
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
/**
	ACT_RE_*: 'RE'表示repository。这个前缀的表包含了流程定义和流程静态资源（图片，规则等）
	ACT_RU_*: 'RU'表示runtime。运行时的表，包含流程实例，任务，变量，异步任务等运行中的数据。
				Activity只在流程实例执行过程中保存这些数据，在流程结束时就会删除这些记录。这样运行时表可以一直很小速度很快。
	ACT_ID_*: 'ID'表示identity。这些表包含身份信息，比如用户，组等。
	ACT_HT_*: 'HT'表示history。这些表包含历史数据，比如历史流程实例，变量，任务等
	ACT_GE_*: 'GE'表示通用数据，用于不同场景下，如存放资源文件。
	----------------------
	1)流程定义：
		act_re_development		部署信息表
		act_re_model			流程设计模型部署表
		act_re_procdef			流程定义数据表
	2)运行时数据表：
		act_ru_execution		运行时流程执行实例表
		act_ru_identitylink		运行时流程人员表，主要存储任务节点和参与者的相关信息
		act_ru_task				运行时任务节点表
		act_ru_variable			运行时流程变量数据表
	3)历史数据表：
		act_hi_actinst			历史节点表
		act_hi_attachment		历史附件表
		act_hi_comment			历史意见表
		act_hi_detail			历史详情表，提供历史变量的查询
		act_hi_procinst			历史流程实例表
		act_hi_taskinst			历史任务实例表
		act_hi_varinst			历史变量表
	4)组织机构表：
		act_id_group			用户组信息表
		act_id_info				用户扩展信息表
		act_id_membership		用户与用户组对应信息表
		act_id_user				用户信息表
			这四张表很常见，基本的组织机构管理，关于用户认证方面建议自己开发一套。组件自带的功能太简单，使用中有很多需求难以满足。
	5)通用数据表：
		act_ge_bytearray		二进制数据表
		act_ge_property			属性数据表存储整个流程引擎级别的数据，初始化表结构式，会默认插入三条记录
 */
