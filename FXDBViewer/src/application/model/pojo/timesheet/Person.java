package application.model.pojo.timesheet;

import java.util.Set;

import application.model.descriptor.annotations.LinkN1;

public class Person {

	private String firstName;
	private String lastName;

	@LinkN1
	private Set<DayReport> reports;

	public Set<DayReport> getReports() {
		return reports;
	}

	public void setReports(Set<DayReport> reports) {
		this.reports = reports;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String toString() {
		return firstName + " " + lastName;
	}
}
