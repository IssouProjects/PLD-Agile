package org.apache.project.controleur;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

public class EtatPlanCharge extends EtatDefaut {

	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale){
		File file = fenetrePrincipale.ouvrirFichierXml("Fichier de demande de livraison", "*.xml", "Ouvrir une demande de livraison");
		if(file == null) {
			return;
		}
		controleur.chargerDemandeDeLivraison(file);
	}
	
	@Override
	public void chargerDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale, File fichier){
		try {
			Deserialisateur.chargerDemandeLivraisonFichier(demandeDeLivraison, planDeVille, fichier);
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
		File file = fenetrePrincipale.ouvrirFichierXml("Fichier de plan", "*.xml", "Ouvrir un plan de ville");
		if(file == null)
			return;
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		controleur.clearPlanDeVille();
		controleur.chargerPlanDeVille(file);
	}
	
	public void intersectionClicked (Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection intersection) {
		fenetrePrincipale.highlightIntersection(intersection);
	}
}
