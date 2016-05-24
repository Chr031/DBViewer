package application.model.pojo.cinema;

import java.util.Date;

import application.model.descriptor.annotations.Link1N;

public class Session {

	private Date date;	
	
	@Link1N
	private Film film;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	@Override
	public String toString() {
		return "Session [date=" + date + ", film=" + film + "]";
	}
	
	
}
