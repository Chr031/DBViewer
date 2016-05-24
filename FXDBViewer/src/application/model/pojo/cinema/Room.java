package application.model.pojo.cinema;

import java.util.List;

import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.LinkN1;

public class Room {

	@Link1N
	private Cinema cinema;

	private int roomNumber;

	private int numberOfSeats;

	@LinkN1
	private List<Session> sessions;

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return cinema + "-" + roomNumber + " (" + numberOfSeats + ")";
	}

}
