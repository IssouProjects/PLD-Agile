package org.apache.project.modele;

import java.sql.Time;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable {
	
	private Intersection adresseEntrepot;
	private Time heureDepart;
	private List<Livraison> livraisonsOrdonnees;
	private List<Chemin> chemins;
	private int dureeTourneeSecondes;

	public Tournee(Intersection adresseEntrepot, Time heureDepart) {
		this.adresseEntrepot=adresseEntrepot;
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
		livraisonsOrdonnees.add(uneLivraison);
	}
	
	public void ajouterChemin(Chemin chemin) {
		chemins.add(chemin);
	}
	
	public List<Chemin> getChemins(){
		return chemins;
	}
	
	public List<Livraison> getLivraisonsOrdonnees(){
		return livraisonsOrdonnees;
	}
	
	public int getDureeTourneeSecondes()
	{
		return dureeTourneeSecondes;
	}
}
