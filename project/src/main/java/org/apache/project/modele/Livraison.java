package org.apache.project.modele;

import java.sql.Time;

public class Livraison {
	
	private Intersection lieuDeLivraison;
	private int duree;
	private Time debutPlageHoraire;
	private Time finPlageHoraire;
	
	public Livraison(Intersection lieuDeLivraison,int dureeLivraison, Time debutPlageHoraire, Time finPlageHoraire) {
		this.lieuDeLivraison=lieuDeLivraison;
		this.duree=dureeLivraison;
		this.debutPlageHoraire=debutPlageHoraire;
		this.finPlageHoraire=finPlageHoraire;
	}
	
	public Livraison(Intersection lieuDeLivraison,int dureeLivraison) {
		this.lieuDeLivraison=lieuDeLivraison;
		this.duree=dureeLivraison;
		this.debutPlageHoraire=null;
		this.finPlageHoraire=null;
	}
	
	public Intersection getLieuDeLivraison() {
		return lieuDeLivraison;
	}
	
	public void setLieuDeLivraison(Intersection lieuDeLivraison) {
		this.lieuDeLivraison = lieuDeLivraison;
	}
	
	public int getDuree() {
		return duree;
	}
	
	public void setDuree(int duree) {
		this.duree = duree;
	}
	
	public Time getDebutPlageHoraire() {
		return debutPlageHoraire;
	}
	
	public void setDebutPlageHoraire(Time debutPlageHoraire) {
		this.debutPlageHoraire = debutPlageHoraire;
	}
	
	public Time getFinPlageHoraire() {
		return finPlageHoraire;
	}
	
	public void setFinPlageHoraire(Time finPlageHoraire) {
		this.finPlageHoraire = finPlageHoraire;
	}

}
