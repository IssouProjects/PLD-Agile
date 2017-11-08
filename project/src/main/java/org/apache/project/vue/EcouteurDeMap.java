package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

/**
 *
 */
public class EcouteurDeMap {
	Controleur controleur;
	MapContainer mapContainer;

	/**
	 * @param c
	 * @param mapContainer
	 */
	public EcouteurDeMap(Controleur c, MapContainer mapContainer) {
		controleur = c;
		this.mapContainer = mapContainer;
	}

	/**
	 * @param intersection
	 */
	public void onIntersectionClicked(Intersection intersection) {
		controleur.intersectionClicked(intersection);
	}

	/**
	 * @param troncon
	 */
	public void onTronconClicked(Troncon troncon) {
		controleur.tronconClicked(troncon);
	}

	/**
	 * @param livraison
	 */
	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);
	}
}
