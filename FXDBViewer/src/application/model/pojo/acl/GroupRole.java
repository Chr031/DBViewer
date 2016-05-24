package application.model.pojo.acl;

import application.model.descriptor.annotations.Link1N;

public class GroupRole {

	@Link1N
	private Group group;
	
	@Link1N
	private Role role;
	
	@Link1N
	private Resource resource;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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
		return "GroupRole [group=" + group + ", role=" + role + ", resource=" + resource + "]";
	}
	
	
}
