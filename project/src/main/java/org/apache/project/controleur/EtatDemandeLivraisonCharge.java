package org.apache.project.controleur;

import java.io.File;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.modele.Troncon;
import org.apache.project.vue.FenetrePrincipale;

import javafx.application.Platform;
import javafx.concurrent.Task;


public class EtatDemandeLivraisonCharge extends EtatDefaut {
	
	private Thread thread;
	
	@Override

	public void calculerTournee(final Controleur controleur,final PlanDeVille planDeVille,final DemandeDeLivraison demandeDeLivraison,
			final Tournee tournee,final FenetrePrincipale fenetrePrincipale, final int tempsLimite) {
		
		controleur.clearTournee();

		tournee.setEntrepot(demandeDeLivraison.getEntrepot());
		
		
		Task <Void> t = new Task <Void> () {

	        @Override
	        protected Void call() throws Exception {
	        	tournee.calculerTournee(planDeVille, demandeDeLivraison, tempsLimite);
	        	
	        	Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						afterCalculation(controleur, fenetrePrincipale, demandeDeLivraison, tournee);
						
					}
				});	        	
	        	return null;
	        }
	      };
		
		thread = new Thread(t);
		thread.start();
		
		fenetrePrincipale.afficherLoading();
	}
	
	private void afterCalculation(Controleur controleur, FenetrePrincipale fenetrePrincipale, DemandeDeLivraison demandeDeLivraison, Tournee tournee) {

		fenetrePrincipale.removeLoading();
					
		//TODO getter
		boolean tempsLimiteAtteint = false;
		tournee.ajouterListeLivraison(demandeDeLivraison.getEntrepot());
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.getListDisplay().enableMoveLivraison();
		fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");

		if (tempsLimiteAtteint) {
			fenetrePrincipale.setVisibleRecalculerButton(true);
			fenetrePrincipale.afficherPopupInfo(
					"Le calcul de tournée s'est terminé avec un timeout. Vous pouvez recalculer la tournée en modifiant la durée");
		} else {
			fenetrePrincipale.setVisibleRecalculerButton(false);
		}

		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		//TODO DELETE THIS
		fenetrePrincipale.afficherPopupInfo(tournee.exporterRoute());
		
	}

	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale, ListeDeCommandes commandes) {
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.DDL_FILE_DESCRIPTION,
				FenetrePrincipale.DDL_FILE_EXTENSION, FenetrePrincipale.DDL_FILEDIALOG_DESCRIPTION);
		if (file == null) {
			return;
		}
		controleur.setEtatCourant(controleur.etatPlanCharge);
		fenetrePrincipale.clearLivraison();
		controleur.clearDemandeDeLivraison();
		controleur.chargerDemandeDeLivraison(file);
	}

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale, ListeDeCommandes commandes) {
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.PDV_FILE_DESCRIPTION, 
				FenetrePrincipale.PDV_FILE_EXTENSION, FenetrePrincipale.PDV_FILEDIALOG_DESCRIPTION);
		if (file == null)
			return;
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		controleur.clearPlanDeVille();
		controleur.clearDemandeDeLivraison();
		controleur.chargerPlanDeVille(file);
	}

	@Override
	public void tronconClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan,
			Troncon troncon, ListeDeCommandes commandes) {
		fenetrePrincipale.highlightTroncon(troncon);
	}

	@Override
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan,
			Tournee tournee, Livraison livraisonPrecedente, ListeDeCommandes commandes) {
		fenetrePrincipale.highlightLivraison(livraisonPrecedente);
	}

	@Override
	public void annulerRecalcul(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee) {
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre de toute action");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}
}
