package application.model.pojo.timesheet;

import application.model.descriptor.annotations.TextArea;


public class Category {

	private String name;
	
	@TextArea
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String toString() {return name;}
}
