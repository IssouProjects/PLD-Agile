package org.apache.project.modele;


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

	public PlageHoraire getPlageHoraire() {
		return plageHoraire;
	}

	public void setPlageHoraire(PlageHoraire plageHoraire) {
		this.plageHoraire = plageHoraire;
	}
	
	@Override
	public String toString() {
		String livraison_s = "X: " + lieuDeLivraison.getCoordX() + "  Y: " + lieuDeLivraison.getCoordY();
		livraison_s += "\n";
		if(plageHoraire != null) {
			livraison_s += "Plage horaire: " + plageHoraire.getDebut().toString() + " - " + plageHoraire.getFin().toString();
		}else {
			livraison_s += "Pas de plage horaire";
		}
		livraison_s += "\n";
		double duree_min = duree/60;
		livraison_s += "Durée sur place: " + (int)Math.ceil(duree_min) + " minutes";			
		return livraison_s;
	}

}
