package org.apache.project.controleur;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

public class CdeSupprimerLivraison implements Commande {
	
	private Tournee tournee;
	private Livraison livraison;

	public CdeSupprimerLivraison(Tournee tournee, Livraison livraison) {
		this.tournee = tournee;
		this.livraison = livraison;
	}
	
	@Override
	public void doCommande() {
		int index = tournee.getLivraisonsOrdonnees().indexOf(livraison);
		tournee.supprimerLivraison(index);
	}

	@Override
	public void undoCommande() {
		tournee.ajouterLivraison(livraison);
	}

}
