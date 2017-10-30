package org.apache.project.vue;

import org.apache.project.controleur.Controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class EcouteurDeBouton implements EventHandler<ActionEvent> {
	
	Controleur controleur;
	
	LivraisonPopup popup = null;

	public EcouteurDeBouton(Controleur c) {
		super();
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
			case FenetrePrincipale.ANNULER_ID:
				controleur.annuler();
				break;
			case LivraisonPopup.VALIDATE_ID:
				if(popup != null) {
					controleur.calculerCheminsNouvelleLivraison(popup.getNewDuree(), popup.getNewHeureDeb(), popup.getNewHeureFin());	
					popup.selfDestruct();
					popup = null;
				}
				break;
			case LivraisonPopup.CANCEL_ID:
				if(popup != null) {
					controleur.annuler();	
					popup.selfDestruct();
					popup = null;
				}
				break;
		
			
			default:
				System.out.println("Unmapped Button");
			}
			System.out.println(sender.getText());
		}
	}
	
	void setPopup(LivraisonPopup popup) {
		this.popup = popup;
	}

}
