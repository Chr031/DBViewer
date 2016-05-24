package application.model.pojo.cinema;

import java.util.Date;
import java.util.List;

import application.model.descriptor.annotations.LinkNN;
import application.model.descriptor.annotations.TextArea;

public class Film {

	private String title;

	@TextArea
	private String summary;

	private Date realisationDate;

	@LinkNN
	private List<Person> actors;

	@LinkNN
	private List<Person> realisators;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getRealisationDate() {
		return realisationDate;
	}

	public void setRealisationDate(Date realisationDate) {
		this.realisationDate = realisationDate;
	}

	public List<Person> getActors() {
		return actors;
	}

	public void setActors(List<Person> actors) {
		this.actors = actors;
	}

	public List<Person> getRealisators() {
		return realisators;
	}

	public void setRealisators(List<Person> realisators) {
		this.realisators = realisators;
	}

	@Override
	public String toString() {
		return  title + " (" + (realisationDate!=null?realisationDate.getYear()+1900:"?") + ")";
	}
	
	

}
