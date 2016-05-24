package application.model.pojo.logistic;

import java.util.Set;

public class Service {

	private String id;
	private String name;
	private String serviceType;

	private String description;

	private Set<Characteristic> actualPerformance;
	private Set<Capability> externalizes;

	public Service() {
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Characteristic> getActualPerformance() {
		return actualPerformance;
	}

	public void setActualPerformance(Set<Characteristic> actualPerformance) {
		this.actualPerformance = actualPerformance;
	}

	public Set<Capability> getExternalizes() {
		return externalizes;
	}

	public void setExternalizes(Set<Capability> externalizes) {
		this.externalizes = externalizes;
	}

}
