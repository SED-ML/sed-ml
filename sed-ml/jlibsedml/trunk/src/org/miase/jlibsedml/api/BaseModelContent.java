package org.miase.jlibsedml.api;

class BaseModelContent implements IModelContent {
	private String content, name;
    
    BaseModelContent(String content, String name) {
		super();
		this.content = content;
		this.name = name;
	}

	
	public String getContents() {
		return content;
	}

	public String getName() {
		return name;
	}

}
