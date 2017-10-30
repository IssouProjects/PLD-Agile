/**
 * 
 */
package org.apache.project.modele;

import java.sql.Time;

/**
 * Un entrepôt est le lieu dont part une tournée.
 */
public class Entrepot extends Livraison {

	private Time heureDepart;

	public Entrepot(Intersection lieuDeLivraison, Time heureDepart) {
		super(lieuDeLivraison);
		this.setHeureDepart(heureDepart);
	}

	/**
	 * @return the heureDepart
	 */
	public Time getHeureDepart() {
		return heureDepart;
	}

	/**
	 * @param heureDepart the heureDepart to set
	 */
	public void setHeureDepart(Time heureDepart) {
		this.heureDepart = heureDepart;
	}

}
