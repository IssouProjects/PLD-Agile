package org.apache.project.controleur;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatSupprLivraison2 extends EtatDefaut{

	Livraison livraisonSupprimee;
	
	@Override
	public void calculerCheminSupprLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee,
			FenetrePrincipale fenetrePrincipale) {
		int indexLivSuppr = tournee.getLivraisonIndex(livraisonSupprimee);
		
		if(indexLivSuppr == 0) {
			fenetrePrincipale.afficherPopupError("Vous ne pouvez pas selectionner l'entrepot");
			controleur.setEtatCourant(controleur.etatSupprLivraison1);
		}
		
		Livraison livraisonPre = tournee.getLivraison(indexLivSuppr - 1);
		Livraison livraisonSuiv = tournee.getLivraison(indexLivSuppr + 1);
		Chemin chemin = tournee.calculerNouveauChemin(planDeVille, livraisonPre.getLieuDeLivraison(), livraisonSuiv.getLieuDeLivraison());
		
		tournee.suppriemerLivraison(indexLivSuppr);
		tournee.supprimerChemin(indexLivSuppr);
	
		tournee.ajouterChemin(chemin, indexLivSuppr);
		
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.notify();
	}

	
	protected void actionEntreeEtatSupprLivraison2(Livraison livraisonSupprimee) {
		this.livraisonSupprimee = livraisonSupprimee;
	}
}
