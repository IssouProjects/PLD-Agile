package org.apache.project.modele;

import java.util.List;

/**
 * La classe <tt>Chemin</tt> représente le chemin qu'il faut parcourir pour
 * aller d'une <tt>Intersection</tt> à une autre
 */
public class Chemin {

	private int duree;
	private Intersection debut;
	private Intersection fin;
	private List<Troncon> listeTroncons;

	/**
	 * Constructeur (cas où on connaît tous les attributs du chemin qu'on crée)
	 * 
	 * @param debut
	 *            Intersection de départ
	 * @param fin
	 *            Intersection d'arrivée
	 * @param duree
	 *            Temps en secondes pour parcourir le chemin
	 * @param listeTroncons
	 *            Tronçons élémentaires constituant le chemin entre debut et fin
	 */
	public Chemin(Intersection debut, Intersection fin, int duree, List<Troncon> listeTroncons) {
		this.debut = debut;
		this.fin = fin;
		this.duree = duree;
		this.listeTroncons = listeTroncons;
	}

	public int getDuree() {
		return duree;
	}

	public Intersection getDebut() {
		return debut;
	}

	public Intersection getFin() {
		return fin;
	}

	public List<Troncon> getTroncons() {
		return listeTroncons;
	}
	
	public List<String> getListeRues() {
		List<String> listeRues = null;
		for(int i=0; i<listeTroncons.size(); i++) {
			listeRues.add(listeTroncons.get(i).getNomRue());
		}
		return listeRues;
	}


	@Override
	public String toString() {
		String chemin_s = "De " + debut.getIdNoeud() + " à " + fin.getIdNoeud();
		return chemin_s;
	}

}
