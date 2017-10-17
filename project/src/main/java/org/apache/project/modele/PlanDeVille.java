package org.apache.project.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class PlanDeVille extends Observable {
	
	private Map<Long,Intersection> intersections;
	private List<Troncon> troncons;
	
	public PlanDeVille() {
		intersections = new HashMap<Long, Intersection>();
		troncons = new ArrayList<Troncon>();
	}
	
	public void ajouterIntersection(Long id, Long coordX, Long coordY) {
		intersections.put(id, new Intersection(id,coordX,coordY));
	}
	
	public void ajouterTroncon(double distance, Long numDepart, Long numArrivee, String nomRue) {
		Troncon troncon = new Troncon(distance,intersections.get(numDepart),intersections.get(numArrivee),nomRue);
		intersections.get(numDepart).ajouterTronconPartant(troncon);
		troncons.add(troncon);
	}
	
	public Map<Long,Intersection> getAllIntersections(){
		return intersections;
	}
	
	public List<Troncon> getAllTroncons(){
		return troncons;
	}
	
	public Intersection getIntersectionById(Long id) {
		return intersections.get(id);
	}
}
