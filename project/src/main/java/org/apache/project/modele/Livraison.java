package org.apache.project.modele;

import java.sql.Time;

/**
 * La classe <tt>Livraison</tt> représente une livraison. Ses principaux
 * attributs sont le lieu où elle a lieu, sa durée (temps de déchargement du
 * camion) et la plage horaire que le livreur doit respecter.
 */
public class Livraison {

	private Intersection lieuDeLivraison;
	private int duree;
	private PlageHoraire plageHoraire;
	private Time heureArrivee;
	private Boolean estSelectionnee;

	/**
	 * Crée une livraison avec un lieu de livraison, une durée de déchargement et
	 * une plage horaire connus.
	 * 
	 * @param lieuDeLivraison
	 *            intersection où se déroule la livraison.
	 * @param dureeLivraison
	 *            temps de déchargement des produits livrés du camion du livreur.
	 * @param plageHoraire
	 *            plage horaire où se déroule la livraison.
	 */
	public Livraison(Intersection lieuDeLivraison, int dureeLivraison, PlageHoraire plageHoraire) {
		this.lieuDeLivraison = lieuDeLivraison;
		this.duree = dureeLivraison;
		this.plageHoraire = plageHoraire;
		this.heureArrivee = null;
		this.setEstSelectionnee(false);
	}

	/**
	 * Crée une livraison avec un lieu de livraison et une durée de déchargement
	 * connus, mais sans plage horaire.
	 * 
	 * @param lieuDeLivraison
	 *            intersection où se déroule la livraison.
	 * @param dureeLivraison
	 *            temps de déchargement des produits livrés du camion du livreur.
	 */
	public Livraison(Intersection lieuDeLivraison, int dureeLivraison) {
		this.lieuDeLivraison = lieuDeLivraison;
		this.duree = dureeLivraison;
		this.plageHoraire = null;
		this.heureArrivee = null;
		this.setEstSelectionnee(false);
	}

	/**
	 * Crée une livraison avec un lieu de livraison mais sans plage horaire et une
	 * durée de déchargement nulle.
	 * 
	 * @param lieuDeLivraison
	 *            intersection où se déroule la livraison.
	 */
	public Livraison(Intersection lieuDeLivraison) {
		this.lieuDeLivraison = lieuDeLivraison;
		this.duree = 0;
		this.plageHoraire = null;
		this.setEstSelectionnee(false);
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

	public PlageHoraire getPlageHoraire() {
		return plageHoraire;
	}

	public void setPlageHoraire(PlageHoraire plageHoraire) {
		this.plageHoraire = plageHoraire;
	}

	public Time getHeureArrivee() {
		return heureArrivee;
	}

	public void setHeureArrivee(Time heureArrivee) {
		this.heureArrivee = heureArrivee;
	}

	public Boolean getEstSelectionnee() {
		return estSelectionnee;
	}

	public void setEstSelectionnee(Boolean estSelectionnee) {
		this.estSelectionnee = estSelectionnee;
	}

	@Override
	public String toString() {
		String livraison_s = "";
		if (heureArrivee != null) {
			livraison_s += "Heure d'arrivée: " + PlageHoraire.timeToString(heureArrivee) + "\n";
		}
		if (plageHoraire != null) {
			livraison_s += "Plage horaire: " + PlageHoraire.timeToString(plageHoraire.getDebut()) + " - "
					+ PlageHoraire.timeToString(plageHoraire.getFin());
			if (heureArrivee != null) {
				long avance = plageHoraire.getDebut().getTime() - heureArrivee.getTime();
				if (avance > 0) {
					livraison_s += "\n" + "Avance: " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(avance);
				}
			}
		} else {
			livraison_s += "Pas de plage horaire";
		}
		livraison_s += "\n";
		livraison_s += "Duree sur place: " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(duree * 1000);
		return livraison_s;
	}

}
