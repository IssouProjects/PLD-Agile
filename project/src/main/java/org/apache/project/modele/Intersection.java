package org.apache.project.modele;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
	
	private Long idNoeud;
	private Long coordX;
	private Long coordY;
	private List<Troncon> tronconsPartants;
	private Boolean estSelectionnee;
	
	public Intersection(Long id, Long x, Long y) {
		this.setIdNoeud(id);
		this.setCoordX(x);
		this.setCoordY(y);
		this.estSelectionnee = false;
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
	
	public Boolean getEstSelectionnee() {
		return estSelectionnee;
	}

	public void setEstSelectionnee(Boolean estSelectionnee) {
		this.estSelectionnee = estSelectionnee;
	}

	public void ajouterTronconPartant(Troncon unTroncon) {
		this.tronconsPartants.add(unTroncon);
	}

	public List<Troncon> getTronconsPartants() {
		return tronconsPartants;
	}
	
	public Troncon getTronconParDestination(Long idIntersectionDestination)
	{
		int nombreTroncon = tronconsPartants.size();
		for(int i = 0; i < nombreTroncon; i++)
		{
			if(tronconsPartants.get(i).getIntersectionArrivee().getIdNoeud().longValue() == idIntersectionDestination.longValue())
			{
				return tronconsPartants.get(i);
			}
		}
		return null;
	}	
}
