package application.model.pojo.logistic;

import java.util.Set;

public class SLA {

	private Set<Characteristic> contains;

	// @ reverse targetPerformance
	private Service service;

	public SLA() {
	}

	public Set<Characteristic> getContains() {
		return contains;
	}

	public void setContains(Set<Characteristic> contains) {
		this.contains = contains;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
