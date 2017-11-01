package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatModifierLivraison1 extends EtatDefaut {

	Livraison livraison;
	
	@Override
	public void validerModificationLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee, Time heureDeb, Time heureFin) {
		
		PlageHoraire plageHoraire;
		if(livraison.getPlageHoraire()==null) {
			plageHoraire = new PlageHoraire(heureDeb, heureFin);
			livraison.setPlageHoraire(plageHoraire);
		}else {
			livraison.getPlageHoraire().setDebut(heureDeb);
			livraison.getPlageHoraire().setFin(heureFin);
		}
		
		tournee.calculerDureeTotale();
		// TODO: Notify
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("done");
	}
	
	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé, vous êtes libre");
	}
	
	protected void actionEntreeEtatModifierLivraison1(Livraison livraison) {
		this.livraison = livraison;
	}
}
