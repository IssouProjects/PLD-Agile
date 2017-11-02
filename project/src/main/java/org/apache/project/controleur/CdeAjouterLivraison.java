package org.apache.project.controleur;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;

public class CdeAjouterLivraison implements Commande {
	
	private PlanDeVille plan;
	private Tournee tournee;
	private Livraison livraisonPrecedente;
	private Livraison livraison;
	
	public CdeAjouterLivraison(PlanDeVille plan, Tournee tournee, Livraison livraison, Livraison livraisonPrecedente) {
		this.plan = plan;
		this.tournee = tournee;
		this.livraison = livraison;
		this.livraisonPrecedente = livraisonPrecedente;
	}
	
	@Override
	public void doCommande() {
		tournee.ajouterNouvelleLivraison(plan, livraison, livraisonPrecedente);
	}

	@Override
	public void undoCommande() {
		int index = tournee.getLivraisonsOrdonnees().indexOf(livraison);
		tournee.supprimerLivraison(plan, index);
	}

}
