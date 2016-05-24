package application.model.pojo.acl;

import application.model.descriptor.annotations.Link1N;

public class UserRole {

	@Link1N
	private User user;
	
	@Link1N
	private Role role;
	
	@Link1N
	private Resource resource;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return user + " has " + role + " on " + resource ;
	}
	
}
