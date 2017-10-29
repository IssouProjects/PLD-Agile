package org.apache.project.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * La classe <tt>PlanDeVille</tt> représente un plan des rues d'une
 * ville/agglomération, il n'a que des tronçons et des intersections.
 */
public class PlanDeVille extends Observable {

	private Map<Long, Intersection> intersections;
	private List<Troncon> troncons;

	/**
	 * Construis un plan routier vide.
	 */
	public PlanDeVille() {
		intersections = new HashMap<Long, Intersection>();
		troncons = new ArrayList<Troncon>();
	}

	/**
	 * @param id
	 * @param coordX
	 * @param coordY
	 */
	public void ajouterIntersection(Long id, Long coordX, Long coordY) {
		intersections.put(id, new Intersection(id, coordX, coordY));
	}

	/**
	 * @param distance
	 * @param numDepart
	 * @param numArrivee
	 * @param nomRue
	 */
	public void ajouterTroncon(double distance, Long numDepart, Long numArrivee, String nomRue) {
		Troncon troncon = new Troncon(distance, intersections.get(numDepart), intersections.get(numArrivee), nomRue);
		intersections.get(numDepart).ajouterTronconPartant(troncon);
		troncons.add(troncon);
	}

	/**
	 * @return
	 */
	public Map<Long, Intersection> getAllIntersections() {
		return intersections;
	}

	/**
	 * @param id
	 * @return
	 */
	public Intersection getIntersection(Long id) {
		return intersections.get(id);
	}

	/**
	 * @return
	 */
	public List<Troncon> getAllTroncons() {
		return troncons;
	}

	/**
	 * @param id
	 * @return
	 */
	public Intersection getIntersectionById(Long id) {
		return intersections.get(id);
	}

	/**
	 * 
	 */
	public void clear() {
		intersections.clear();
		troncons.clear();
	}
}
