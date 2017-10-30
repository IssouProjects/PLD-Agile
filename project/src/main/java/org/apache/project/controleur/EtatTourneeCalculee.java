package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;

public class EtatTourneeCalculee extends EtatDefaut{
	
	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		fenetrePrincipale.clearLivraison();
		fenetrePrincipale.clearTournee();
		controleur.clearPlanDeVille();
		controleur.clearTournee();
		controleur.clearDemandeDeLivraison();
		controleur.ouvrirPlanDeVille();
	}
	
	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale){
		controleur.setEtatCourant(controleur.etatPlanCharge);
		fenetrePrincipale.clearLivraison();
		fenetrePrincipale.clearTournee();
		controleur.clearDemandeDeLivraison();
		controleur.clearTournee();
		controleur.ouvrirDemandeDeLivraison();
	}
	
	@Override
	public void ajouterLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatAjoutLivraison1);
		fenetrePrincipale.afficherInfo("Veuillez cliquer sur une intersection de la carte");
	}
	
  @Override
	public void intersectionClicked (Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection intersection) {
		fenetrePrincipale.highlightIntersection(intersection);
	}
	
  @Override
	public void livraisonClicked(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		fenetrePrincipale.highlightLivraison(livraisonPrecedente);
  }

	@Override 
	public void supprimerLivraison( Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatSupprLivraison1);
		fenetrePrincipale.afficherInfo("Veuillez cliquer sur une livraison de la carte");
	}

}
