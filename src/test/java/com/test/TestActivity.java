package com.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActivity {

	@Test
	public void test() {// 使用代码创建工作流需要的23张表
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		// 连接数据库配置
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/act?characterEncoding=utf8&useSSL=true");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("123456");

		/**
		 * 设置数据库创建表策略：
		 * public static final String DB_SCHEMA_UPDATE_FALSE = "false";如果不存在表就抛出异常
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";每次都先删除表在创建新的表
		 * public static final String DB_SCHEMA_UPDATE_TRUE = "true";如果不存在表就创建表，如果存在就直接使用
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine);
	}

	@Test
	public void testXml() {// 使用配置文件创建工作流需要的23张表
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println(processEngine);
	}
}
