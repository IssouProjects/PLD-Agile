package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

public class EcouteurDeMap {
	Controleur controleur;
	MapContainer mapContainer;
	
	Object highlightedObject = null;
	
	public EcouteurDeMap(Controleur c, MapContainer mapContainer) {
		controleur = c;
		this.mapContainer = mapContainer;
	}
	
	public void onIntersectionClicked(Intersection intersection) {
		controleur.intersectionClicked(intersection);
		
		if(highlightedObject != null)
			mapContainer.getMapDisplay().unHighlight(highlightedObject);
		
		if(mapContainer.getMapDisplay().highlight(intersection)) {
			highlightedObject = intersection;
		}
	}
	
	public void onTronconClicked(Troncon troncon) {
		//TODO
	}
	
	public void onLivraisonClicked(Livraison livraison) {
		
		if(highlightedObject != null)
			mapContainer.getMapDisplay().unHighlight(highlightedObject);
		
		if(mapContainer.getMapDisplay().highlight(livraison)) {
			highlightedObject = livraison;
		}
		controleur.livraisonClicked(livraison);
	}
	
	public void onEntrepotClicked(Intersection intersection) {
		controleur.entrepotClicked();
	}
	
}
