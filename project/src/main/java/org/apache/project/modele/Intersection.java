package org.apache.project.modele;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	
	private Long idNoeud;
	private Long coordX;
	private Long coordY;
	private List<Troncon> tronconsPartants;
	
	public Intersection(Long id, Long x, Long y) {
		this.setIdNoeud(id);
		this.setCoordX(x);
		this.setCoordY(y);
		this.tronconsPartants=new ArrayList<Troncon>();
	}

	public Long getIdNoeud() {
		return idNoeud;
	}

	public void setIdNoeud(Long idNoeud) {
		this.idNoeud = idNoeud;
	}

	public Long getCoordX() {
		return coordX;
	}

	public void setCoordX(Long coordX) {
		this.coordX = coordX;
	}

	public Long getCoordY() {
		return coordY;
	}

	public void setCoordY(Long coordY) {
		this.coordY = coordY;
	}
	
	public void ajouterTronconPartant(Troncon unTroncon) {
		this.tronconsPartants.add(unTroncon);
	}

	public List<Troncon> getTronconsPartants() {
		return tronconsPartants;
	}
	
}
