package org.apache.project.controleur;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
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
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		fenetrePrincipale.highlightLivraison(livraisonPrecedente);
  }

	@Override 
	public void supprimerLivraison( Controleur controleur, Tournee tournee,  PlanDeVille planDeVille,  FenetrePrincipale fenetrePrincipale) {
		
		Livraison livraisonASupprimer = fenetrePrincipale.getSelectedLivraison();
		
		if(livraisonASupprimer == null) {
			controleur.setEtatCourant(controleur.etatTourneeCalculee);
			return;
		}
		
		// TODO: AFFICHER UNE POPUP : ETES VOUS SUR
		
		int indexLivSuppr = tournee.getLivraisonIndex(livraisonASupprimer);
		
		if(tournee.getLivraisonsOrdonnees().size() == 3) {
			fenetrePrincipale.afficherPopupError("Vous ne pouvez pas suprimer la seule livraison");
			controleur.setEtatCourant(controleur.etatTourneeCalculee);
			return;
		}
		
		Livraison livraisonPre = tournee.getLivraison(indexLivSuppr - 1);
		Livraison livraisonSuiv = tournee.getLivraison(indexLivSuppr + 1);
		Chemin chemin = tournee.calculerNouveauChemin(planDeVille, livraisonPre.getLieuDeLivraison(), livraisonSuiv.getLieuDeLivraison());
		
		tournee.supprimerLivraison(indexLivSuppr);
		tournee.supprimerChemin(indexLivSuppr-1);
		tournee.supprimerChemin(indexLivSuppr-1);
	
		tournee.ajouterChemin(chemin, indexLivSuppr-1);
		
		tournee.calculerDureeTotale();
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
	}
	
	@Override
	public void modifierLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		Livraison livraisonSelectionnee = fenetrePrincipale.getSelectedLivraison();
		fenetrePrincipale.afficherFenetreModifierLivraison(livraisonSelectionnee);
		controleur.setEtatCourant(controleur.etatAjoutLivraison1);
	}

}
