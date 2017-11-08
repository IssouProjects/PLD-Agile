package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Livraison;

/**
 * classe permettant de transmettre les différentes action effectuées sur la
 * liste au controleur
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
	 * Méthode appellée au clic sur une livraison
	 * 
	 * @param livraison
	 *            la livraison sur laquelle l'utilisateur a cliqué
	 */
	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);
	}

	/**
	 * @param livraisonToMove
	 *            la livraison à déplacer
	 * @param newIndex
	 *            le nouvel index de la livraison à déplacer
	 */
	public void onMoveLivraison(Livraison livraisonToMove, int newIndex) {
		controleur.echangerLivraison(newIndex);
	}
}
