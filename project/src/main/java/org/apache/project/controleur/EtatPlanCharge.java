package org.apache.project.controleur;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Troncon;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

/**
 *
 */
public class EtatPlanCharge extends EtatDefaut {

	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale, ListeDeCommandes commandes) {
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.DDL_FILE_DESCRIPTION,
				FenetrePrincipale.DDL_FILE_EXTENSION, FenetrePrincipale.DDL_FILEDIALOG_DESCRIPTION);
		if (file == null) {
			fenetrePrincipale.afficherPlanDeVille(planDeVille);
			return;
		}
		controleur.clearDemandeDeLivraison();
		controleur.chargerDemandeDeLivraison(file);
	}

	@Override
	public void chargerDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, FenetrePrincipale fenetrePrincipale, File fichier) {
		try {
			Deserialisateur.chargerDemandeLivraisonFichier(demandeDeLivraison, planDeVille, fichier);
			controleur.setEtatCourant(controleur.etatDemandeLivraisonCharge);
			fenetrePrincipale.afficherDemandeDeLivraison(demandeDeLivraison);
			fenetrePrincipale.afficherInfo("Calculer une tournée");
		} catch (ParserConfigurationException e) {
			fenetrePrincipale.afficherPopupError("Format non valide.\n" + e.getMessage());
		} catch (SAXException e) {
			fenetrePrincipale.afficherPopupError("Format non valide.\n" + e.getMessage());
		} catch (IOException e) {
			fenetrePrincipale.afficherPopupError("Format non valide.\n" + e.getMessage());
		} catch (ExceptionXML e) {
			fenetrePrincipale.afficherPlanDeVille(planDeVille);
			fenetrePrincipale.afficherPopupError("Format non valide.\n" + e.getMessage());
		}
	}

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale,
			ListeDeCommandes commandes) {
		File file = fenetrePrincipale.ouvrirFichierXml(FenetrePrincipale.PDV_FILE_DESCRIPTION,
				FenetrePrincipale.PDV_FILE_EXTENSION, FenetrePrincipale.PDV_FILEDIALOG_DESCRIPTION);
		if (file == null)
			return;
		controleur.setEtatCourant(controleur.etatInit);
		fenetrePrincipale.clearPlanDeVille();
		controleur.clearPlanDeVille();
		controleur.chargerPlanDeVille(file);
	}

	@Override
	public void tronconClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan,
			Troncon troncon, ListeDeCommandes commandes) {
		fenetrePrincipale.highlightTroncon(troncon);
	}
}
