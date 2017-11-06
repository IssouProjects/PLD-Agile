package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.Livraison;

public class EcouteurDeListe {

	private Controleur controleur;

	public EcouteurDeListe(Controleur c) {
		controleur = c;
	}

	public void onLivraisonClicked(Livraison livraison) {
		controleur.livraisonClicked(livraison);
	}

	public void onMoveLivraison(Livraison livraisonToMove, int newIndex) {
		controleur.echangerLivraison(newIndex);
	}
}
