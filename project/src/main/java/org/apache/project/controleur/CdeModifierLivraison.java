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
		this.anciennePlage = livraison.getPlageHoraire();
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
		livraison.setPlageHoraire(anciennePlage);
	}

}
