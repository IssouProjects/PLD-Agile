package org.apache.project.controleur;

import org.apache.project.vue.FenetrePrincipale;

/**
 *
 */
public class EtatFeuilleDeRoute extends EtatDefaut {

	@Override
	public void fermerFeuilleDeRoute(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		fenetrePrincipale.masquerFenetreFeuilleDeRoute();
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}

}
