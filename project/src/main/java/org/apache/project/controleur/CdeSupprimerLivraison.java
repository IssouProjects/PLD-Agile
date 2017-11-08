package org.apache.project.controleur;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;

/**
 *
 */
public class CdeSupprimerLivraison implements Commande {

	private PlanDeVille plan;
	private Tournee tournee;
	private Livraison livraison;
	private Livraison livraisonPrecedente;

	/**
	 * @param plan
	 * @param tournee
	 * @param livraison
	 */
	public CdeSupprimerLivraison(PlanDeVille plan, Tournee tournee, Livraison livraison) {
		this.plan = plan;
		this.tournee = tournee;
		this.livraison = livraison;
		int index = tournee.getLivraisonsOrdonnees().indexOf(livraison);
		this.livraisonPrecedente = tournee.getLivraisonsOrdonnees().get(index - 1);
	}

	@Override
	public int doCommande() {
		int index = tournee.getLivraisonsOrdonnees().indexOf(livraison);
		tournee.supprimerLivraison(plan, index);

		return 0;
	}

	@Override
	public void undoCommande() {
		tournee.ajouterNouvelleLivraison(plan, livraison, livraisonPrecedente);
	}

}
