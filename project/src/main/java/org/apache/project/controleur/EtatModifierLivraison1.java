package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatModifierLivraison1 extends EtatDefaut {

	Livraison livraison;

	@Override
	public void validerModificationLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale,
			Tournee tournee, Time heureDeb, Time heureFin, Integer duree, ListeDeCommandes commandes) {

		// treating the case where there are no modifications:
		if (livraison.getPlageHoraire() == null) {
			if (heureDeb == null || heureFin == null) {
				// do nothing
			} else {
				commandes.ajouteCommande(new CdeModifierLivraison(livraison, heureDeb, heureFin, duree));
			}
		} else {
			if (heureDeb.toString().equals(livraison.getPlageHoraire().getDebut().toString())
					&& heureFin.toString().equals(livraison.getPlageHoraire().getFin().toString())) {
				// do nothing
			} else {
				commandes.ajouteCommande(new CdeModifierLivraison(livraison, heureDeb, heureFin, duree));
			}
		}

		tournee.calculerDureeTotale();
		// TODO: Notify
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("done");
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé, vous êtes libre");
	}

	protected void actionEntreeEtatModifierLivraison1(Livraison livraison) {
		this.livraison = livraison;
	}
}
