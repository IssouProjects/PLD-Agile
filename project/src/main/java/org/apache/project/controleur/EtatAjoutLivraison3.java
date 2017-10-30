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

		int indexPre = tournee.getLivraisonIndex(livraisonPrecedente);
		Livraison livraisonSuiv = tournee.getLivraison(indexPre + 1);

		Chemin chemin1 = tournee.calculerNouveauChemin(planDeVille, livraisonPrecedente.getLieuDeLivraison(),
				nouvelleLivraison.getLieuDeLivraison());
		Chemin chemin2 = tournee.calculerNouveauChemin(planDeVille, nouvelleLivraison.getLieuDeLivraison(),
				livraisonSuiv.getLieuDeLivraison());

		tournee.ajouterLivraison(nouvelleLivraison, indexPre + 1);
		tournee.supprimerChemin(indexPre);
		tournee.ajouterChemin(chemin1, indexPre);
		tournee.ajouterChemin(chemin2, indexPre + 1);

		tournee.calculerDureeTotale();
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}

	protected void actionEntreeEtatAjoutLivraison3(Livraison livraisonPrecedente, Livraison nouvelleLivraison) {
		this.nouvelleLivraison = nouvelleLivraison;
		this.livraisonPrecedente = livraisonPrecedente;
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé vous êtes libre");
	}
}
