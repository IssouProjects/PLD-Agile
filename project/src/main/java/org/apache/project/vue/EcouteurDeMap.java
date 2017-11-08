package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

/**
 * Ecouteur des interactions sur la carte (map).
 */
public class EcouteurDeMap {
	private Controleur controleur;

	/**
	 * Construit un nouvelle écouteur de map.
	 * 
	 * @param c
	 *            contrôleur de l'application
	 */
	public EcouteurDeMap(Controleur c) {
		controleur = c;
	}

	/**
	 * Gère le clic sur une intersection.
	 * 
	 * @param intersection
	 *            <tt>Intersection</tt> cliquée
	 */
	public void onIntersectionClicked(Intersection intersection) {
		controleur.intersectionClicked(intersection);
	}

	/**
	 * Gère le clic sur un tronçon.
	 * 
	 * @param troncon
	 *            <tt>Troncon</tt> cliqué
	 */
	public void onTronconClicked(Troncon troncon) {
		controleur.tronconClicked(troncon);
	}

	/**
	 * Gère le clic sur une livraison.
	 * 
	 * @param livraison
	 *            <tt>Livraison</tt> cliquée
	 */
	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);
	}
}
