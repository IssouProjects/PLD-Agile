package org.apache.project.modele;

import java.sql.Time;

public class Livraison {
	
	private Intersection lieuDeLivraison;
	private int duree;
	private PlageHoraire plageHoraire;
	
	public Livraison(Intersection lieuDeLivraison,int dureeLivraison, PlageHoraire plageHoraire) {
		this.lieuDeLivraison=lieuDeLivraison;
		this.duree=dureeLivraison;
		this.plageHoraire=plageHoraire;
	}
	
	public Livraison(Intersection lieuDeLivraison,int dureeLivraison) {
		this.lieuDeLivraison=lieuDeLivraison;
		this.duree=dureeLivraison;
		this.plageHoraire=null;
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

}
