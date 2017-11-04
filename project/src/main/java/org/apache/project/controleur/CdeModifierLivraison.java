package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;

public class CdeModifierLivraison implements Commande {

	private Livraison livraison;
	private PlageHoraire nouvellePlage;
	private PlageHoraire anciennePlage;
	
	public CdeModifierLivraison(Livraison livraison, Time heureDebut, Time heureFin) {
		this.livraison = livraison;
		this.nouvellePlage = new PlageHoraire(heureDebut, heureFin);
		if(livraison.getPlageHoraire() == null) {
			this.anciennePlage = null;
		}
		else {
			this.anciennePlage = new PlageHoraire(livraison.getPlageHoraire().getDebut(), livraison.getPlageHoraire().getFin());
		}
	}
	
	@Override
	public void doCommande() {
		if(livraison.getPlageHoraire() == null) { 
			livraison.setPlageHoraire(nouvellePlage);
		}
		else {
			livraison.getPlageHoraire().setDebut(nouvellePlage.getDebut());
			livraison.getPlageHoraire().setFin(nouvellePlage.getFin());
		}
	}

	@Override
	public void undoCommande() {
		if(anciennePlage == null) {
			livraison.setPlageHoraire(anciennePlage);
		}
		else {
			livraison.getPlageHoraire().setDebut(anciennePlage.getDebut());
			livraison.getPlageHoraire().setFin(anciennePlage.getFin());
		}
	}

}
