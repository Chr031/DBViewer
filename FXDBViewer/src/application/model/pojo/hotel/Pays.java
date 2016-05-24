package application.model.pojo.hotel;

import application.model.descriptor.annotations.TextArea;

public class Pays {

	public String nom;

	@TextArea
	public String info;

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

	public String toString() {
		return nom;
	}
}
