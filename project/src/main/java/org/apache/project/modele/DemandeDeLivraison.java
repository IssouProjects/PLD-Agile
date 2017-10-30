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

	private Intersection adresseEntrepot;
	private Time heureDepart;
	private List<Livraison> livraisons;

	public DemandeDeLivraison(Intersection entrepot, Time heureDepart) {
		this.adresseEntrepot = entrepot;
		this.heureDepart = heureDepart;
		this.livraisons = new ArrayList<Livraison>();
	}

	/**
	 * Constructeur par défaut.
	 */
	public DemandeDeLivraison() {
		this.adresseEntrepot = null;
		this.heureDepart = null;
		this.livraisons = new ArrayList<Livraison>();
	}

	public Intersection getAdresseEntrepot() {
		return adresseEntrepot;
	}

	public void setAdresseEntrepot(Intersection adresseEntrepot) {
		this.adresseEntrepot = adresseEntrepot;
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
		this.adresseEntrepot = null;
		this.heureDepart = null;
		this.livraisons.clear();
	}
}
