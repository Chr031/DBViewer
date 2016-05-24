package application.model.pojo.tasks;

import java.util.Date;
import java.util.Set;

import application.model.descriptor.annotations.Link0N;
import application.model.descriptor.annotations.LinkN0;
import application.model.descriptor.annotations.TextArea;

public class Task {

	private int id;

	private String name;

	@TextArea
	private String description;

	private boolean done;

	private Date expectedDate;

	@LinkN0(reverseName="mainTask")
	private Set<Task> subTasks;

	@Link0N
	private User assignee;

	public Set<Task> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(Set<Task> subTasks) {
		this.subTasks = subTasks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	@Override
	public String toString() {
		return id + " " + name + " (" + assignee + " | " + (isDone() ? "Done" : "Todo") + ")";
	}

	public Date getExpectedDate() {
		return expectedDate;
	}

	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
