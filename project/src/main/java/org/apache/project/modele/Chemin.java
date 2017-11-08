package org.apache.project.modele;

import java.util.List;
import java.util.ArrayList;

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

	/**
	 * @return
	 */
	public int getDuree() {
		return duree;
	}

	/**
	 * @return
	 */
	public Intersection getDebut() {
		return debut;
	}

	/**
	 * @return
	 */
	public Intersection getFin() {
		return fin;
	}

	/**
	 * @return
	 */
	public List<Troncon> getTroncons() {
		return listeTroncons;
	}

	/**
	 * @return
	 */
	public List<String> getListeRues() {
		List<String> listeRues = new ArrayList<String>();
		int distancePrecedente = (int) listeTroncons.get(0).getLongueur();
		String ruePrecedente = listeTroncons.get(0).getNomRue();

		for (int i = 1; i < listeTroncons.size(); i++) {
			String rueActuelle = listeTroncons.get(i).getNomRue();
			int distanceActuelle = (int) listeTroncons.get(i).getLongueur();

			if (rueActuelle.isEmpty()) {
				rueActuelle = "Rue Inconnue";
			}

			if (rueActuelle.equals(ruePrecedente)) {
				distancePrecedente += distanceActuelle;
			} else {
				listeRues.add(ruePrecedente + " sur " + distancePrecedente + "m.");
				distancePrecedente = distanceActuelle;
			}
			ruePrecedente = rueActuelle;
		}

		listeRues.add(ruePrecedente + " sur " + distancePrecedente + "m.");

		return listeRues;
	}

	@Override
	public String toString() {
		String chemin_s = "De " + debut.getIdNoeud() + " à " + fin.getIdNoeud();
		return chemin_s;
	}

}
