package org.apache.project.modele;

import java.sql.Time;

public class Livraison {

	private Intersection lieuDeLivraison;
	private int duree;
	private PlageHoraire plageHoraire;
	private Time heureArrivee;

	public Livraison(Intersection lieuDeLivraison, int dureeLivraison, PlageHoraire plageHoraire) {
		this.lieuDeLivraison = lieuDeLivraison;
		this.duree = dureeLivraison;
		this.plageHoraire = plageHoraire;
		this.heureArrivee = null;
	}

	public Livraison(Intersection lieuDeLivraison, int dureeLivraison) {
		this.lieuDeLivraison = lieuDeLivraison;
		this.duree = dureeLivraison;
		this.plageHoraire = null;
		this.heureArrivee = null;
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

	@Override
	public String toString() {
		String livraison_s = "";
		if (heureArrivee != null) {
			livraison_s += "Heure d'arrivée: " + PlageHoraire.formatTime(heureArrivee) + "\n";
		}
		if (plageHoraire != null) {
			livraison_s += "Plage horaire: " + PlageHoraire.formatTime(plageHoraire.getDebut()) + " - "
					+ PlageHoraire.formatTime(plageHoraire.getFin());
			if (heureArrivee != null) {
				long avance = plageHoraire.getDebut().getTime() - heureArrivee.getTime();
				if (avance > 0) {
					livraison_s += "\n" + "Avance: " + (int) Math.ceil(avance / 60000) + " min";
				}
			}
		} else {
			livraison_s += "Pas de plage horaire";
		}
		livraison_s += "\n";
		double duree_min = duree / 60;
		livraison_s += "Duree sur place: " + (int) Math.ceil(duree_min) + " minutes";
		return livraison_s;
	}

}
