package application.model.pojo.acl;

import application.model.descriptor.annotations.Link1N;

public class Resource {

	private String name;
	
	@Link1N
	private ResourceType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name + " (" + type + ")";
	}
	
	
}
