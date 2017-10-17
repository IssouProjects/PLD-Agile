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

	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale){
		try {
			Deserialisateur.chargerDemandeLivraison(demandeDeLivraison, planDeVille);
			controleur.setEtatCourant(controleur.etatDemandeLivraisonCharge);
			fenetrePrincipale.afficherDemandeDeLivraison(demandeDeLivraison);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExceptionXML e) {
			e.printStackTrace();
		}
	}
}
