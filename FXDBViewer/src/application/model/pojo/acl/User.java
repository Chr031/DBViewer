package application.model.pojo.acl;

import java.util.Set;

import application.model.descriptor.annotations.LinkNN;

public class User {

	private String username;
	private String password;

	@LinkNN
	private Set<Group> groups;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return username;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

}
