package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatTempsLimiteAtteint extends EtatDefaut{

	@Override
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison,
			Tournee tournee, FenetrePrincipale fenetrePrincipale, int tempsLimite) {
		
		controleur.clearTournee();
		
		tournee.setEntrepot(demandeDeLivraison.getEntrepot());
		
		boolean tempsLimiteAtteint = tournee.calculerTournee(planDeVille, demandeDeLivraison, tempsLimite);
		tournee.ajouterListeLivraison(demandeDeLivraison.getEntrepot());
		
		if(tempsLimiteAtteint) {
			fenetrePrincipale.afficherFenetreTimeout();
			controleur.setEtatCourant(controleur.etatTempsLimiteAtteint);
		}else {
			fenetrePrincipale.afficherTournee(tournee);
			fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");
			controleur.setEtatCourant(controleur.etatTourneeCalculee);
		}
	}
	
	@Override
	public void annulerRecalcul(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee) {
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}
}
