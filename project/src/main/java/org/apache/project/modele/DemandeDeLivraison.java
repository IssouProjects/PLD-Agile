package org.apache.project.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * La classe <tt>DemandeDeLivraison</tt> représente la demande de livraison
 * (obtenue après désérialisation du fichier xml la contenant).
 */
public class DemandeDeLivraison extends Observable {

	private Entrepot entrepot;
	private List<Livraison> livraisons;

	public DemandeDeLivraison() {
		this.entrepot = null;
		this.livraisons = new ArrayList<Livraison>();
	}

	public Entrepot getEntrepot() {
		return entrepot;
	}

	public void setEntrepot(Entrepot entrepot) {
		this.entrepot = entrepot;
		this.ajouterLivraison(entrepot);
	}

	public void ajouterLivraison(Livraison uneLivraison) {
		livraisons.add(uneLivraison);
	}

	public List<Livraison> getListeLivraison() {
		return livraisons;
	}

	/**
	 * Réinitialise tous les attributs de la <tt>DemandeDeLivraison</tt>
	 */
	public void clear() {
		this.entrepot = null;
		this.livraisons.clear();
	}
}
