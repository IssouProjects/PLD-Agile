package org.apache.project.controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

public class EtatInit extends EtatDefaut {

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){
		try {
			Deserialisateur.chargerPlanVille(planDeVille);
			controleur.setEtatCourant(controleur.etatPlanCharge);
			fenetrePrincipale.afficherPlanDeVille(planDeVille);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExceptionXML e) {
			e.printStackTrace();
			fenetrePrincipale.afficherPopupError(e.getMessage());
		}
	}
}
