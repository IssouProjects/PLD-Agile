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
		
		if(tournee.getLivraisonsOrdonnees().size() == 2) {
			fenetrePrincipale.afficherPopupError("Vous ne pouvez pas suprimer la seule livraison");
			controleur.setEtatCourant(controleur.etatTourneeCalculee);
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
		fenetrePrincipale.afficherInfo("Vous êtes libre");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		
	}

	
	protected void actionEntreeEtatSupprLivraison2(Livraison livraisonSupprimee) {
		this.livraisonSupprimee = livraisonSupprimee;
	}
}
