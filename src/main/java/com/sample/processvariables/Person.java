package com.sample.processvariables;

import java.io.Serializable;

public class Person implements Serializable {
	
	private static final long serialVersionUID = -7278721590101095819L;
	
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
