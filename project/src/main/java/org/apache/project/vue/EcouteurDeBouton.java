package org.apache.project.vue;

import org.apache.project.controleur.Controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class EcouteurDeBouton implements EventHandler<ActionEvent> {
	
	private Controleur controleur;
	private LivraisonPopup livraisonPopup = null;
	private ModificationPopup modificationPopup = null; 

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
			case FenetrePrincipale.EDIT_LIVRAISON_ID:
				controleur.editerLivraison();
				break;
			case FenetrePrincipale.ANNULER_ID:
				controleur.annuler();
				break;
			case LivraisonPopup.VALIDATE_ID:
				if(livraisonPopup != null) {
					if(livraisonPopup.checkTimeOk()) {
						controleur.calculerCheminsNouvelleLivraison(livraisonPopup.getNewDuree(), livraisonPopup.getNewHeureDeb(), livraisonPopup.getNewHeureFin());	
						livraisonPopup.selfDestruct();
						livraisonPopup = null;
					}
				}
				break;
			case LivraisonPopup.CANCEL_ID:
				if(livraisonPopup != null) {
					if(livraisonPopup.checkTimeOk()) {
						controleur.annuler();	
						livraisonPopup.selfDestruct();
						livraisonPopup = null;
					}
				}
				break;
		
			
			default:
				System.out.println("Unmapped Button");
			}
			System.out.println((String)sender.getUserData());
		}
	}
	
	void setLivraisonPopup(LivraisonPopup popup) {
		this.livraisonPopup = popup;
	}
	
	void setModificationPopup(ModificationPopup popup) {
		this.modificationPopup = popup;
	}

}
