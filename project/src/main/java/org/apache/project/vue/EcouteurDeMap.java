package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

public class EcouteurDeMap {
	Controleur controleur;
	
	public EcouteurDeMap(Controleur c) {
		controleur = c;
	}
	
	public void onIntersectionClicked(Intersection intersection) {
		//TODO
	}
	
	public void onTronconClicked(Troncon troncon) {
		//TODO
	}
	
	public void onLivraisonClicked(Livraison livraison) {
		//TODO
	}
	
}
