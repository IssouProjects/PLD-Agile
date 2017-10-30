package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

public class EcouteurDeMap {
	Controleur controleur;
	MapContainer mapContainer;
	Livraison livraisonHighlited; 
	
	public EcouteurDeMap(Controleur c, MapContainer mapContainer) {
		controleur = c;
		this.mapContainer = mapContainer;
		this.livraisonHighlited = null;
	}
	
	public void onIntersectionClicked(Intersection intersection) {
		System.out.println(intersection);
		controleur.intersectionClicked(intersection);
	}
	
	public void onTronconClicked(Troncon troncon) {
		//TODO
	}
	
	public void onLivraisonClicked(Livraison livraison) {
		
		if(livraisonHighlited != null)
			mapContainer.getMapDisplay().unHighlightLivraison(livraisonHighlited);
		
		livraisonHighlited = livraison;
		mapContainer.getMapDisplay().highlightLivraison(livraison);
		controleur.livraisonClicked(livraison);
	}
	
	public void onEntrepotClicked(Intersection intersection) {
		controleur.entrepotClicked();
	}
	
}
