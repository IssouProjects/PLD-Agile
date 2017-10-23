package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;

public class EtatTourneeCalculee extends EtatDefaut{
	
	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		fenetrePrincipale.clearTournee();
		controleur.clearPlanDeVille();
		controleur.clearDemandeDeLivraison();
		controleur.ouvrirPlanDeVille();
	}
	
	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale){
		controleur.setEtatCourant(controleur.etatPlanCharge);
		fenetrePrincipale.clearLivraison();;
		fenetrePrincipale.clearTournee();
		controleur.clearDemandeDeLivraison();
		controleur.ouvrirDemandeDeLivraison();
	}

}
