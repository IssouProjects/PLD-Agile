package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

/**
 * 
 */
public class EtatModifierLivraison1 extends EtatDefaut {

	Livraison livraison;

	@Override
	public void validerModificationLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale,
			Tournee tournee, Time heureDeb, Time heureFin, Integer duree, ListeDeCommandes commandes) {

		// treating the case where there are no modifications:
		if (livraison.getPlageHoraire() == null) {
			if ((heureDeb != null && heureFin != null) || duree != livraison.getDuree()) {
				commandes.ajouteCommande(new CdeModifierLivraison(livraison, tournee, heureDeb, heureFin, duree));
			}
		} else {
			if (!heureDeb.toString().equals(livraison.getPlageHoraire().getDebut().toString())
					|| !heureFin.toString().equals(livraison.getPlageHoraire().getFin().toString())
					|| duree != livraison.getDuree()) {
				commandes.ajouteCommande(new CdeModifierLivraison(livraison, tournee, heureDeb, heureFin, duree));
			}
		}

		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Action libre");
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé. Action libre");
	}

	/**
	 * @param livraison
	 */
	protected void actionEntreeEtatModifierLivraison1(Livraison livraison) {
		this.livraison = livraison;
	}
}
