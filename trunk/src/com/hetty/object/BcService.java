package com.hetty.object;

import java.io.Serializable;

public abstract class BcService implements Serializable{

	private static final long serialVersionUID = 2351769180636491630L;
	
	public final static int TYPE_LOCAL=0;
	public final static int TYPE_REMOTE=1;
	
	protected Class<?> typeClass;
	
	public void setTypeClass(Class<?> type) {
		this.typeClass = type;
	}

	protected String id;
	
	protected String name;
	
	
	
	public BcService(){
		
	}
	
	public BcService(String id,String name){
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

	public abstract int getType();

	public Class<?> getTypeClass() {
		return typeClass;
	}

	
	
}
