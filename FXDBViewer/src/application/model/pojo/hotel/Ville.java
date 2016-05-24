package application.model.pojo.hotel;

import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.TextArea;

public class Ville {

	private String nom;

	@TextArea
	private String info;

	@Link1N
	private Pays pays;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Pays getPays() {
		return pays;
	}

	public void setPays(Pays pays) {
		this.pays = pays;
	}

	public String toString() {
		return nom;
	}
}
