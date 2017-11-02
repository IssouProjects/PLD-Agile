package org.apache.project.vue;

import org.apache.project.controleur.Controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class EcouteurDeBouton implements EventHandler<ActionEvent> {
	
	private Controleur controleur;
	private FenetrePrincipale fenetrePrincipale;

	public EcouteurDeBouton(Controleur c, FenetrePrincipale fp) {
		super();
		fenetrePrincipale = fp;
		controleur = c;
	}
	
	@Override
	public void handle(ActionEvent event) {
		Button sender = (Button)event.getSource();
		if(sender.getUserData() instanceof String) {
			switch((String)sender.getUserData()) {
			case FenetrePrincipale.LOAD_MAP_ID:
				controleur.ouvrirPlanDeVille();
				break;
			case FenetrePrincipale.LOAD_LIVRAISON_ID:
				controleur.ouvrirDemandeDeLivraison();
				break;
			case FenetrePrincipale.CALCULATE_TOURNEE_ID:
				controleur.calculerTournee();
				break;
			case FenetrePrincipale.ADD_LIVRAISON_ID:
				controleur.ajouterLivraison();
				break;
			case FenetrePrincipale.SUPPR_LIVRAISON_ID:
				controleur.supprimerLivraison();
				break;
			case FenetrePrincipale.EDIT_LIVRAISON_ID:
				controleur.editerLivraison();
				break;
			case FenetrePrincipale.ANNULER_ID:
				controleur.annuler();
				break;
			case LivraisonPopup.VALIDATE_ID:
				LivraisonPopup popup = fenetrePrincipale.getFenetreAjouterLivraison();
				if(popup != null) {
					if(popup.checkTimeOk()) {
						controleur.calculerCheminsNouvelleLivraison(popup.getNewDuree(), popup.getNewHeureDeb(), popup.getNewHeureFin());	
						fenetrePrincipale.masquerFenetreAjouterLivraison();
					}
				}
				break;
			case LivraisonPopup.CANCEL_ID:
				LivraisonPopup popup2 = fenetrePrincipale.getFenetreAjouterLivraison();
				if(popup2 != null) {
					if(popup2.checkTimeOk()) {
						controleur.annuler();	
						fenetrePrincipale.masquerFenetreAjouterLivraison();
					}
				}
				break;
			default:
				System.out.println("Unmapped Button");
			}
			System.out.println((String)sender.getUserData());
		}
	}
}
