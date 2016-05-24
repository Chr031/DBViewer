package application.model.pojo.hotel;

import application.model.descriptor.annotations.Link1N;
import application.model.descriptor.annotations.TextArea;

public class Hotel {

	private String nom;

	private String adresse;

	@TextArea
	private String description;

	@Link1N
	private Ville ville;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Ville getVille() {
		return ville;
	}

	public void setVille(Ville ville) {
		this.ville = ville;
	}

	public String toString() {
		return nom;
	}

}
