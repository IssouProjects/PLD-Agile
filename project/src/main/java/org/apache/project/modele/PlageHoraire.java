package org.apache.project.modele;

import java.sql.Time;

public class PlageHoraire {

	private Time debut;
	private Time fin;
	
	public PlageHoraire(Time debut, Time fin) {
		this.debut=debut;
		this.fin=fin;
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
}
