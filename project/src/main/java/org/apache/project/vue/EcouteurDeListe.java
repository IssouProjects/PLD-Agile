package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Livraison;


public class EcouteurDeListe {
	
	private Controleur controleur;
	private ListDisplay listDisplay;
	
	public EcouteurDeListe(Controleur c, ListDisplay l) {
		controleur = c;
		listDisplay = l;
	}
	
	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);	
	}
	
	public void onMoveLivraison(Livraison livraisonToMove, int newIndex) {
		controleur.echangerLivraison(newIndex);
	}
}
