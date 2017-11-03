package org.apache.project.controleur;

import java.io.File;

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
		File file = fenetrePrincipale.ouvrirFichierXml("Fichier de demande de livraison", "*.xml", "Ouvrir une demande de livraison");
		if(file == null) {
			return;
		}
		controleur.setEtatCourant(controleur.etatPlanCharge);
		fenetrePrincipale.clearLivraison();
		controleur.clearDemandeDeLivraison();
		controleur.chargerDemandeDeLivraison(file);
	}

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale) {
		File file = fenetrePrincipale.ouvrirFichierXml("Fichier de plan", "*.xml", "Ouvrir un plan de ville");
		if(file == null)
			return;
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		controleur.clearPlanDeVille();
		controleur.clearDemandeDeLivraison();
		controleur.chargerPlanDeVille(file);
	}
	
	public void intersectionClicked (Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection intersection) {
		fenetrePrincipale.highlightIntersection(intersection);
	}
	
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		fenetrePrincipale.highlightLivraison(livraisonPrecedente);
	}
}
