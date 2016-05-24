package application.model.pojo.acl;

import application.model.descriptor.annotations.TextArea;

public class Group {

	private String name;
	
	@TextArea
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return  name ;
	}
	
	
	
}
