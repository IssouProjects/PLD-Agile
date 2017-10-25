package org.apache.project.controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

public class EtatPlanCharge extends EtatDefaut {

	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale){
		try {
			Deserialisateur.chargerDemandeLivraison(demandeDeLivraison, planDeVille);
			controleur.setEtatCourant(controleur.etatDemandeLivraisonCharge);
			fenetrePrincipale.afficherDemandeDeLivraison(demandeDeLivraison);
			fenetrePrincipale.afficherInfo("Calculer une tournée");
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		} catch (ExceptionXML e) {
			fenetrePrincipale.afficherPopupError(e.getMessage()+ "\nChargement de demande de livraison annulée.");
		}
	}
	
	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		controleur.clearPlanDeVille();
		controleur.ouvrirPlanDeVille();
	}
}
