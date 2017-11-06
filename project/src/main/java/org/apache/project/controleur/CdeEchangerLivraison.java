package org.apache.project.controleur;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;

public class CdeEchangerLivraison implements Commande {

	private PlanDeVille plan;
	private Tournee tournee;
	private Livraison livraison;
	private int ancienIndex;
	private int nouvelIndex;
	
	public CdeEchangerLivraison(PlanDeVille plan, Tournee tournee, Livraison livraison, int nouvelIndex) {
		this.plan = plan;
		this.tournee = tournee;
		this.livraison = livraison;
		this.ancienIndex = tournee.getLivraisonsOrdonnees().indexOf(livraison);
		this.nouvelIndex = nouvelIndex;
	}
	
	@Override
	public void doCommande() {
		tournee.deplacerLivraison(plan, livraison, nouvelIndex);
	}

	@Override
	public void undoCommande() {
		tournee.deplacerLivraison(plan, livraison, ancienIndex);
	}

}
