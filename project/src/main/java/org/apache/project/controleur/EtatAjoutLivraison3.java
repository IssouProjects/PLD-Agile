package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.vue.MapGestures.SelectionMode;

public class EtatAjoutLivraison3 extends EtatDefaut {

	Livraison nouvelleLivraison;
	Livraison livraisonPrecedente;

	@Override
	public void calculerCheminsNouvelleLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee,
			FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin,
			ListeDeCommandes commandes) {
		nouvelleLivraison.setDuree(duree);

		if (heureDeb != null && heureFin != null) {
			nouvelleLivraison.setPlageHoraire(new PlageHoraire(heureDeb, heureFin));
		}

		int retour = commandes.ajouteCommande(new CdeAjouterLivraison(planDeVille, tournee, nouvelleLivraison, livraisonPrecedente));
		
		if(retour == 2) {
			fenetrePrincipale.afficherPopupError("Impossible de realiser le chemin entre deux livraisons avec les fichiers d'entrés");
		}
		
		fenetrePrincipale.getMapContainer().setSelectionMode(SelectionMode.Troncon);
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Action libre");
		fenetrePrincipale.setVisibleAnnulerButton(false);
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}

	protected void actionEntreeEtatAjoutLivraison3(Livraison livraisonPrecedente, Livraison nouvelleLivraison) {
		this.nouvelleLivraison = nouvelleLivraison;
		this.livraisonPrecedente = livraisonPrecedente;
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé. Action libre");
		fenetrePrincipale.getMapContainer().setSelectionMode(SelectionMode.Troncon);
		fenetrePrincipale.setVisibleAnnulerButton(false);
	}
}
