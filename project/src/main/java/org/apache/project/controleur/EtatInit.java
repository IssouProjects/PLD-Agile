package org.apache.project.controleur;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

public class EtatInit extends EtatDefaut {

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale, ListeDeCommandes commandes){
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.PDV_FILE_DESCRIPTION, 
				FenetrePrincipale.PDV_FILE_EXTENSION, FenetrePrincipale.PDV_FILEDIALOG_DESCRIPTION);
		if(file == null) {
			return;
		}
			
		controleur.chargerPlanDeVille(file);
	}
	
	@Override
	public void chargerPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale, File fichier) {
		if(fichier == null)
			return;
		try {
			Deserialisateur.chargerPlanDeVilleFichier(planDeVille, fichier);
			controleur.setEtatCourant(controleur.etatPlanCharge);
			fenetrePrincipale.afficherPlanDeVille(planDeVille);
			fenetrePrincipale.afficherInfo("Charger une demande de livraison");
		} catch (ParserConfigurationException e) {
			fenetrePrincipale.afficherPopupError(e.getMessage());
		} catch (SAXException e) {
			fenetrePrincipale.afficherPopupError(e.getMessage());
		} catch (IOException e) {
			fenetrePrincipale.afficherPopupError("Format non valide.");
		} catch (ExceptionXML e) {
			fenetrePrincipale.afficherPopupError(e.getMessage());
		}
	}
}
