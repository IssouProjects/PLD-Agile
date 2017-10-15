package org.apache.project.modele;

public class Troncon {
	
	private Long longueur;
	private String nomRue;
	private Intersection IntersectionDepart;
	private Intersection IntersectionArrivee;
	
	public Troncon(Long longueur, Intersection InterDepart, Intersection InterArrivee, String nomRue) {
		this.longueur=longueur;
		this.nomRue =  nomRue;
		this.IntersectionDepart = InterDepart;
		this.IntersectionArrivee = InterArrivee;
	}

	public Long getLongueur() {
		return longueur;
	}

	public void setLongueur(Long longueur) {
		this.longueur = longueur;
	}

	public String getNomRue() {
		return nomRue;
	}

	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}

	public Intersection getIdIntersectionDepart() {
		return IntersectionDepart;
	}

	public void setIntersectionDepart(Intersection IntersectionDepart) {
		this.IntersectionDepart = IntersectionDepart;
	}

	public Intersection getIntersectionArrivee() {
		return IntersectionArrivee;
	}

	public void setIntersectionArrivee(Intersection IntersectionArrivee) {
		this.IntersectionArrivee = IntersectionArrivee;
	}
}
