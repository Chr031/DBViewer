package application.model.pojo.logistic;

import java.util.Set;

public abstract class Capability {

	private String name;
	private String value;
	private String description;

	private Set<Certificate> clearance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Certificate> getClearance() {
		return clearance;
	}

	public void setClearance(Set<Certificate> clearance) {
		this.clearance = clearance;
	}

}
