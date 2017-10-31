package org.apache.project.controleur;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

public class CdeAjouterLivraison implements Commande {
	
	private Tournee tournee;
	private Livraison livraison;
	
	public CdeAjouterLivraison(Tournee tournee, Livraison livraison) {
		this.tournee = tournee;
		this.livraison = livraison;
	}
	
	@Override
	public void doCommande() {
		tournee.ajouterLivraison(livraison);
	}

	@Override
	public void undoCommande() {
		int index = tournee.getLivraisonsOrdonnees().indexOf(livraison);
		tournee.supprimerLivraison(index);
	}

}
