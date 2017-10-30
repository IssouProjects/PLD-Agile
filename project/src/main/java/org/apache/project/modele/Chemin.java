package org.apache.project.modele;

import java.util.ArrayList;
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
	 * Constructeur (cas où on ne connait la liste des tronçons reliant debut à fin
	 * 
	 * @param debut
	 *            Intersection de départ
	 * @param fin
	 *            Intersection d'arrivée
	 * @param duree
	 *            Temps en secondes pour parcourir le chemin
	 */
	public Chemin(Intersection debut, Intersection fin, int duree) {
		this.debut = debut;
		this.fin = fin;
		this.duree = duree;
		listeTroncons = new ArrayList<Troncon>();
	}

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

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public Intersection getDebut() {
		return debut;
	}

	public void setDebut(Intersection debut) {
		this.debut = debut;
	}

	public Intersection getFin() {
		return fin;
	}

	public void setFin(Intersection fin) {
		this.fin = fin;
	}

	public List<Troncon> getTroncons() {
		return listeTroncons;
	}

	/**
	 * Ajoute un <tt>Troncon</tt> à la fin de la <tt>List(Troncon)</tt> constituant
	 * un chemin
	 * 
	 * @param unTroncon
	 *            <tt>Troncon</tt> à ajouter à la listeTroncons du <tt>Chemin</tt>
	 */
	public void ajouterTroncon(Troncon unTroncon) {
		this.listeTroncons.add(unTroncon);
	}

	@Override
	public String toString() {
		String chemin_s = "De " + debut.getIdNoeud() + " à " + fin.getIdNoeud();
		return chemin_s;
	}

}
