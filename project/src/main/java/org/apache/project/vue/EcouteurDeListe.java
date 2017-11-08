package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Livraison;

/**
 *
 */
public class EcouteurDeListe {

	private Controleur controleur;

	/**
	 * @param c
	 */
	public EcouteurDeListe(Controleur c) {
		controleur = c;
	}

	/**
	 * @param livraison
	 */
	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);
	}

	/**
	 * @param livraisonToMove
	 * @param newIndex
	 */
	public void onMoveLivraison(Livraison livraisonToMove, int newIndex) {
		controleur.echangerLivraison(newIndex);
	}
}
