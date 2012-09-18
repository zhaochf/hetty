package com.hetty.object;

import java.io.Serializable;

public abstract class Service implements Serializable{

	private static final long serialVersionUID = 2351769180636491630L;
	
	
	protected Class<?> typeClass;
	
	public void setTypeClass(Class<?> type) {
		this.typeClass = type;
	}

	protected String id;
	
	protected String name;
	
	
	
	public Service(){
		
	}
	
	public Service(String id,String name){
		this.id=id;
		this.name = name;
	}
	
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

	public Class<?> getTypeClass() {
		return typeClass;
	}

	
	
}
