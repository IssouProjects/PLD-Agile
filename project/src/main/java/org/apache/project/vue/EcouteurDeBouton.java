package org.apache.project.vue;

import org.apache.project.controleur.Controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * classe permettant d'écouter les clics sur les boutons et de les transmettre
 * au controleur
 */
public class EcouteurDeBouton implements EventHandler<ActionEvent> {

	private Controleur controleur;
	private FenetrePrincipale fenetrePrincipale;
	private ModificationPopup modificationPopup = null;

	/**
	 * Crée un écouteur de bouton
	 * 
	 * @param c
	 * @param fp
	 */
	public EcouteurDeBouton(Controleur c, FenetrePrincipale fp) {
		super();
		fenetrePrincipale = fp;
		controleur = c;
	}

	@Override
	public void handle(ActionEvent event) {
		Button sender = (Button) event.getSource();
		if (sender.getUserData() instanceof String) {
			switch ((String) sender.getUserData()) {
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
			case FenetrePrincipale.UNDO_ID:
				controleur.undo();
				break;
			case FenetrePrincipale.REDO_ID:
				controleur.redo();
				break;
			case FenetrePrincipale.RECALCULER_ID:
				controleur.afficherFenetreTimeout();
				break;
			case LivraisonPopup.VALIDATE_ID:
				LivraisonPopup popup = fenetrePrincipale.getFenetreAjouterLivraison();
				if (popup != null && popup.checkTimeOk()) {
					controleur.calculerCheminsNouvelleLivraison(popup.getNewDuree(), popup.getNewHeureDeb(),
							popup.getNewHeureFin());
					fenetrePrincipale.masquerFenetreAjouterLivraison();
				}
				break;
			case LivraisonPopup.CANCEL_ID:
				LivraisonPopup popup2 = fenetrePrincipale.getFenetreAjouterLivraison();
				if (popup2 != null) {
					controleur.annuler();
					fenetrePrincipale.masquerFenetreAjouterLivraison();
				}
				break;
			case ModificationPopup.VALIDATE_ID:
				if ((modificationPopup != null) && (modificationPopup.checkTimeOk())) {
					controleur.validerModificationLivraison(modificationPopup.getNewDuree(),
							modificationPopup.getNewHeureDeb(), modificationPopup.getNewHeureFin());
					modificationPopup.selfDestruct();
					modificationPopup = null;
				}
				break;
			case ModificationPopup.CANCEL_ID:
				if (modificationPopup != null) {
					controleur.annuler();
					modificationPopup.selfDestruct();
					modificationPopup = null;
				}
				break;
			case TimeoutPopup.VALIDATE_ID:
				TimeoutPopup timeoutPopup = fenetrePrincipale.getFenetreTimeoutPopup();
				if (timeoutPopup != null) {
					fenetrePrincipale.masquerFenetreTimeoutPopup();
					controleur.calculerTournee(timeoutPopup.getNewDuree() * 1000);
				}
				break;
			case TimeoutPopup.CANCEL_ID:
				TimeoutPopup timeoutPopup2 = fenetrePrincipale.getFenetreTimeoutPopup();
				if (timeoutPopup2 != null) {
					controleur.annulerRecalcul();
					fenetrePrincipale.masquerFenetreTimeoutPopup();
				}
				break;
			case FenetrePrincipale.EXPORTER_ID:
				controleur.exporterFeuilleDeRoute();
				break;
			case FeuilleDeRoutePopup.OK_ID:
				controleur.fermerFeuilleDeRoute();
				break;
			default:
				System.out.println("Unmapped Button");
				break;
			}
			System.out.println((String) sender.getUserData());
		}
	}

	/**
	 * @param popup
	 */
	public void setModificationPopup(ModificationPopup popup) {
		this.modificationPopup = popup;
	}
}
