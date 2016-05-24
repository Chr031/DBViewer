package application.model.pojo.hotel;

import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.TextArea;

public class Chambre {

	private int numero;

	@TextArea
	private String commentaire;

	@Link1N
	private Hotel hotel;

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public String toString() {
		return hotel + " Ch " + numero;
	}

}
