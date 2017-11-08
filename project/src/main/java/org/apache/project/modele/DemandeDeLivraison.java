package org.apache.project.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * La classe <tt>DemandeDeLivraison</tt> représente la demande de livraison(s)
 * (obtenue après désérialisation du fichier xml la contenant).
 */
public class DemandeDeLivraison extends Observable {

	private Entrepot entrepot;
	private List<Livraison> livraisons;

	/**
	 * Construit une demande de livraison(s) vide, elle n'a ni entrepôt de départ,
	 * ni livraison.
	 */
	public DemandeDeLivraison() {
		this.entrepot = null;
		this.livraisons = new ArrayList<Livraison>();
	}

	/**
	 * Renvoie l'entrepôt de départ de la demande de livraison(s)
	 * 
	 * @return
	 */
	public Entrepot getEntrepot() {
		return entrepot;
	}

	/**
	 * Met <tt>entrepot</tt> comme entrepôt de départ de la demande de livraison(s).
	 * 
	 * @param entrepot
	 *            l'entrepôt qu'on veut définir comme point de départ
	 */
	public void setEntrepot(Entrepot entrepot) {
		this.entrepot = entrepot;
		this.ajouterLivraison(entrepot);
	}

	/**
	 * Ajoute <tt>uneLivraison</tt> à la fin de la liste de livraisons de la
	 * demande.
	 * 
	 * @param uneLivraison
	 *            livraison qu'on ajoute à la demande de livraison(s)
	 */
	public void ajouterLivraison(Livraison uneLivraison) {
		livraisons.add(uneLivraison);
	}

	/**
	 * La méthode renvoie la liste des livraisons présente dans la
	 * <tt>DemandeDeLivraison</tt>.
	 * 
	 * @return
	 */
	public List<Livraison> getListeLivraison() {
		return livraisons;
	}

	/**
	 * Réinitialise tous les attributs de la <tt>DemandeDeLivraison</tt>.
	 */
	public void clear() {
		this.entrepot = null;
		this.livraisons.clear();
	}
}
