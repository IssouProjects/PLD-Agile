package org.apache.project.modele;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * La classe <tt>DemandeDeLivraison</tt> représente la demande de livraison
 * (obtenue après désérialisation du fichier xml la contenant).
 */
public class DemandeDeLivraison extends Observable {

	private Livraison entrepot;
	private Time heureDepart;
	private List<Livraison> livraisons;

	public DemandeDeLivraison() {
		this.entrepot = null;
		this.heureDepart = null;
		this.livraisons = new ArrayList<Livraison>();
	}

	public Livraison getEntrepot() {
		return entrepot;
	}

	public void setEntrepot(Livraison entrepot) {
		this.entrepot = entrepot;
		this.ajouterLivraison(entrepot);
	}

	public Time getHeureDepart() {
		return heureDepart;
	}

	public void setHeureDepart(Time heureDepart) {
		this.heureDepart = heureDepart;
	}

	public void ajouterLivraison(Livraison uneLivraison) {
		livraisons.add(uneLivraison);
	}

	public List<Livraison> getListeLivraison() {
		return livraisons;
	}

	/**
	 * Remet à zéro tous les attributs de la <tt>DemandeDeLivraison</tt>
	 */
	public void clear() {
		this.entrepot = null;
		this.heureDepart = null;
		this.livraisons.clear();
	}
}
