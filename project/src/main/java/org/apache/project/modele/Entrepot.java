/**
 * 
 */
package org.apache.project.modele;

import java.sql.Time;

/**
 * Un entrepôt est le lieu dont part une tournée.
 * 
 * On considère que le camion est chargé à l'heure de départ, donc l'attribut
 * <tt>duree</tt> est égal à 0.
 */
public class Entrepot extends Livraison {

	private Time heureDepart;

	/**
	 * Construit un entrepôt situé à l'intersection <tt>lieuDeLivraison</tt> et avec
	 * une heure de départ <tt>heureDepart</tt> pour les tournées.
	 * 
	 * @param lieuDeLivraison
	 *            intersection où est situé l'entrepôt
	 * @param heureDepart
	 *            heure de départ (pour une tournée) de l'entrepôt
	 */
	public Entrepot(Intersection lieuDeLivraison, Time heureDepart) {
		super(lieuDeLivraison, 0);
		this.setHeureDepart(heureDepart);
	}

	/**
	 * Retourne l'heure de départ de l'entrepôt
	 * 
	 * @return l'heure de départ (pour la tournée) de l'entrepôt
	 */
	public Time getHeureDepart() {
		return heureDepart;
	}

	/**
	 * Cette méthode fixe une nouvelle heure de départ dudit entrepôt
	 * 
	 * @param heureDepart
	 *            la nouvelle heure de départ de l'entrepôt
	 */
	public void setHeureDepart(Time heureDepart) {
		this.heureDepart = heureDepart;
	}
}
