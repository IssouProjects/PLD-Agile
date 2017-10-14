package org.apache.project.modele;

import java.sql.Time;
import java.util.List;

public class DemandeDeLivraison {

	private Intersection adresseEntrepot;
	private Time heureDepart;
	private List<Livraison> livraisons;
	
	public DemandeDeLivraison(Intersection entrepot, Time heureDepart) {
		this.adresseEntrepot=entrepot;
		this.heureDepart=heureDepart;
	}
	
	public Intersection getAdresseEntrepot() {
		return adresseEntrepot;
	}
	
	public void setAdresseEntrepot(Intersection adresseEntrepot) {
		this.adresseEntrepot = adresseEntrepot;
	}

	public Time getHeureDepart() {
		return heureDepart;
	}

	public void setHeureDepart(Time heureDepart) {
		this.heureDepart = heureDepart;
	}
	
	public void ajouterLivraison(Livraison uneLivraison) {
		livraisons.add(uneLivraison);
	}
	
}
