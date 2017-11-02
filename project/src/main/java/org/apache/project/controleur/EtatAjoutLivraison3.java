package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatAjoutLivraison3 extends EtatDefaut {

	Livraison nouvelleLivraison;
	Livraison livraisonPrecedente;

	@Override
	public void calculerCheminsNouvelleLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee,
			FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin) {
		nouvelleLivraison.setDuree(duree);

		if (heureDeb != null && heureFin != null) {
			nouvelleLivraison.setPlageHoraire(new PlageHoraire(heureDeb, heureFin));
		}
		
		tournee.ajouterNouvelleLivraison(planDeVille, nouvelleLivraison, livraisonPrecedente);
		
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}

	protected void actionEntreeEtatAjoutLivraison3(PlanDeVille plan, Tournee tournee, Livraison livraisonPrecedente, Livraison nouvelleLivraison, ListeDeCommandes commandes) {
		this.nouvelleLivraison = nouvelleLivraison;
		this.livraisonPrecedente = livraisonPrecedente;
		commandes.ajouteCommande(new CdeAjouterLivraison(plan, tournee, nouvelleLivraison, livraisonPrecedente));		
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé, vous êtes libre");
	}
}
