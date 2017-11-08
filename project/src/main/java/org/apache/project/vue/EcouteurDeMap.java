package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

/**
 * classe permettant de transmettre les interaction de l'utilisateur avec la map
 * au controleur
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
	 * Méthode appelée quand un utilisateur clique sur une intersection
	 * 
	 * @param intersection
	 *            l'intersection sur laquelle l'utilisateur a cliqué
	 */
	public void onIntersectionClicked(Intersection intersection) {
		controleur.intersectionClicked(intersection);
	}

	/**
	 * Méthode appelée quand un utilisateur clique sur un tronçon
	 * 
	 * @param troncon
	 *            le tronçon sur lequel l'utilisateur a cliqué
	 */
	public void onTronconClicked(Troncon troncon) {
		controleur.tronconClicked(troncon);
	}

	/**
	 * Méthode appelée quand un utilisateur clique sur une livraison
	 * 
	 * @param livraison
	 *            la livraison sur laquelle l'utilisateur a cliqué
	 */
	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);
	}
}
