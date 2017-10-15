package org.apache.project.modele;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	
	private int idNoeud;
	private int coordX;
	private int coordY;
	private List<Troncon> tronconsPartants;
	
	public Intersection(int id, int x, int y) {
		this.setIdNoeud(id);
		this.setCoordX(x);
		this.setCoordY(y);
		this.tronconsPartants=new ArrayList<Troncon>();
	}

	public int getIdNoeud() {
		return idNoeud;
	}

	public void setIdNoeud(int idNoeud) {
		this.idNoeud = idNoeud;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	
	public void ajouterTronconPartant(Troncon unTroncon) {
		this.tronconsPartants.add(unTroncon);
	}
}
