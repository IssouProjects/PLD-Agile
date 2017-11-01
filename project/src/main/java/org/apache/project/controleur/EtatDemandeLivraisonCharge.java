package org.apache.project.controleur;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatDemandeLivraisonCharge extends EtatDefaut {
	@Override
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison,
			Tournee tournee, FenetrePrincipale fenetrePrincipale) {
		tournee.setEntrepot(demandeDeLivraison.getEntrepot());
		tournee.calculerTournee(planDeVille, demandeDeLivraison);
		tournee.ajouterListeLivraison(demandeDeLivraison.getEntrepot());
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}

	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatPlanCharge);
		fenetrePrincipale.clearLivraison();
		controleur.clearDemandeDeLivraison();
		controleur.ouvrirDemandeDeLivraison();
	}

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		controleur.clearPlanDeVille();
		controleur.clearDemandeDeLivraison();
		controleur.ouvrirPlanDeVille();
	}
	
	public void intersectionClicked (Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection intersection) {
		fenetrePrincipale.highlightIntersection(intersection);
	}
	
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		fenetrePrincipale.highlightLivraison(livraisonPrecedente);
	}
}
