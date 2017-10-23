package org.apache.project.modele;

import java.sql.Time;

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

	/* TODO Put what is below in a Util.java class ? */
	public static String formatTime(Time time) {
		String result = time.toString();
		return result.substring(0, 5);
	}

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
}
