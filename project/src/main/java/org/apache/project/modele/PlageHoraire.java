package org.apache.project.modele;

import java.sql.Time;

/**
 * La classe <tt>PlageHoraire</tt> représente une plage horaire, elle une heure
 * de début et une heure de fin.
 */
public class PlageHoraire {

	private Time debut;
	private Time fin;

	public PlageHoraire(Time debut, Time fin) {
		this.debut = debut;
		this.fin = fin;
	}

	public Time getDebut() {
		return debut;
	}

	public void setDebut(Time debut) {
		this.debut = debut;
	}

	public Time getFin() {
		return fin;
	}

	public void setFin(Time fin) {
		this.fin = fin;
	}

	/* TODO Put what is below in this file in a Util.java class ? */

	/**
	 * Méthode pour afficher un <tt>Time</tt> au format hh:mm
	 * 
	 * @param time
	 *            l'heure qu'on veut transformer en <tt>String</tt>
	 * @return une chaine de caractère affichant l'heure au format hh:mm, ex:
	 *         "08:13"
	 */
	public static String timeToString(Time time) {
		String result = time.toString();
		return result.substring(0, 5);
	}

	/**
	 * Calcule l'heure d'arrivée d'un trajet à partir de son heure de départ et de
	 * sa durée en secondes
	 * 
	 * @param heureDepart
	 *            heure de départ d'un trajet
	 * @param duree
	 *            durée du trajet en secondes
	 * @return l'heure d'arrivée du trajet
	 */
	@SuppressWarnings("deprecation")
	public static Time calculerHeureArrivee(Time heureDepart, int duree) {
		int min = duree / 60;
		int heures = (min / 60) + heureDepart.getHours();
		int minutes = (min % 60) + heureDepart.getMinutes();
		int secondes = (duree % 60) + heureDepart.getSeconds();
		if (secondes > 59) {
			minutes++;
			secondes = secondes % 60;
		}
		if (minutes > 59) {
			heures++;
			minutes = minutes % 60;
		}
		if (heures > 23) {
			heures = heures % 23;
		}
		return new Time(heures, minutes, secondes);
	}

	/**
	 * Transforme une durée en millisecondes (ms) en une <tt>String</tt> de type "Xh
	 * Ymin"
	 * 
	 * Exemple: duree= 3600000 ms => return "1h 0min"
	 * 
	 * @param duree
	 *            une durée en ms
	 * @return une <tt>String</tt> de type "Xh Ymin"
	 */
	public static String afficherMillisecondesEnHeuresEtMinutes(double duree) {
		int secondes = (int) (duree / 1000);
		int heures = secondes / 3600;
		int minutes = (secondes % 3600) / 60;
		String result = "";
		if (heures > 0) {
			result += heures + "h ";
		}
		result += minutes + "min";
		return result;
	}
}
