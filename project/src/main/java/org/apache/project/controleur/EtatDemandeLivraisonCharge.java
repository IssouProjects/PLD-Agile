package org.apache.project.controleur;

import java.io.File;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

import com.sun.javafx.util.TempState;

public class EtatDemandeLivraisonCharge extends EtatDefaut {
	@Override
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison,
			Tournee tournee, FenetrePrincipale fenetrePrincipale, int tempsLimite) {
		
		controleur.clearTournee();
		
		tournee.setEntrepot(demandeDeLivraison.getEntrepot());
		
		boolean tempsLimiteAtteint = tournee.calculerTournee(planDeVille, demandeDeLivraison, tempsLimite);
		tournee.ajouterListeLivraison(demandeDeLivraison.getEntrepot());
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.getListDisplay().enableMoveLivraison();
		fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");
		
		if(tempsLimiteAtteint) {
			fenetrePrincipale.setVisibleRecalculerButton(true);
			fenetrePrincipale.afficherPopupInfo("Le calcul de tournée s'est terminé avec un timeout. Vous pouvez recalculer la tournée en modifiant la durée");
		}else {
			fenetrePrincipale.setVisibleRecalculerButton(false);
		}
			
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}
	
	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale) {
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.DDL_FILE_DESCRIPTION,
				FenetrePrincipale.DDL_FILE_EXTENSION, FenetrePrincipale.DDL_FILEDIALOG_DESCRIPTION);
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
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.PDV_FILE_DESCRIPTION, 
				FenetrePrincipale.PDV_FILE_EXTENSION, FenetrePrincipale.PDV_FILEDIALOG_DESCRIPTION);
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
	
	@Override
	public void annulerRecalcul(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee) {
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}
}
