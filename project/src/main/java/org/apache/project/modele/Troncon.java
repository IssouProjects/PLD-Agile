package org.apache.project.modele;

public class Troncon {
	
	private int longueur;
	private String nomRue;
	private int idIntersectionDepart;
	private int idIntersectionArrivee;
	
	public Troncon(int longueur, int idInterDepart, int idInterArrivee, String nomRue) {
		this.longueur=longueur;
		this.nomRue =  nomRue;
		this.idIntersectionDepart = idInterDepart;
		this.idIntersectionArrivee = idInterArrivee;
	}

	public int getLongueur() {
		return longueur;
	}

	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}

	public String getNomRue() {
		return nomRue;
	}

	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}

	public int getIdIntersectionDepart() {
		return idIntersectionDepart;
	}

	public void setIdIntersectionDepart(int idIntersectionDepart) {
		this.idIntersectionDepart = idIntersectionDepart;
	}

	public int getIdIntersectionArrivee() {
		return idIntersectionArrivee;
	}

	public void setIdIntersectionArrivee(int idIntersectionArrivee) {
		this.idIntersectionArrivee = idIntersectionArrivee;
	}
}
