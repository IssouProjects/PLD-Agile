package org.apache.project.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * La classe <tt>PlanDeVille</tt> représente un plan des rues d'une
 * ville/agglomération. Il n'est composé que de tronçons et d'intersections.
 */
public class PlanDeVille extends Observable {

	private Map<Long, Intersection> intersections;
	private List<Troncon> troncons;

	/**
	 * Construit un plan routier vide.
	 */
	public PlanDeVille() {
		intersections = new HashMap<Long, Intersection>();
		troncons = new ArrayList<Troncon>();
	}

	/**
	 * Ajoute une intersection au plan.
	 * 
	 * @param id
	 *            identifiant de l'<tt>Intersection</tt>
	 * @param coordX
	 *            coordonnée x de l'<tt>Intersection</tt>
	 * @param coordY
	 *            coordonnée y de l'<tt>Intersection</tt>
	 */
	public void ajouterIntersection(Long id, Long coordX, Long coordY) {
		intersections.put(id, new Intersection(id, coordX, coordY));
	}

	/**
	 * Ajoute un tronçon au plan.
	 * 
	 * @param longueur
	 *            longueur du <tt>Troncon</tt> en mètres.
	 * @param numDepart
	 *            id de l'<tt>Intersection</tt> de départ
	 * @param numArrivee
	 *            id de l'<tt>Intersection</tt> d'arrivée
	 * @param nomRue
	 *            nom usuel du tronçon (exemple: "Boulevard Roger Salengro")
	 */
	public boolean ajouterTroncon(double longueur, Long numDepart, Long numArrivee, String nomRue) {
		if(intersections.get(numDepart) == null || intersections.get(numArrivee) == null)
		{
			return false;
		}
		Troncon troncon = new Troncon(longueur, intersections.get(numDepart), intersections.get(numArrivee), nomRue);
		intersections.get(numDepart).ajouterTronconPartant(troncon);
		troncons.add(troncon);
		return true;
	}

	/**
	 * La méthode renvoie l'ensemble des <tt>Intersection</tt> du plan.
	 * 
	 * @return la <tt>Map(Long, Intersection)</tt> du plan.
	 */
	public Map<Long, Intersection> getAllIntersections() {
		return intersections;
	}

	/**
	 * La méthode renvoie la liste complète des tronçons du plan.
	 * 
	 * @return la liste des tronçons du plan.
	 */
	public List<Troncon> getAllTroncons() {
		return troncons;
	}

	/**
	 * La méthode renvoie l'intersection du plan dont l'identifiant correspond à
	 * l'<tt>id</tt> fourni en paramètre si elle existe, autrement elle renvoie
	 * null.
	 * 
	 * @param id
	 *            identifiant de l'intersection recherchée.
	 * @return l'intersection correspondante à l'<tt>id</tt> fourni si elle existe,
	 *         sinon <tt>null</tt>.
	 */
	public Intersection getIntersectionById(Long id) {
		return intersections.get(id);
	}

	/**
	 * La méthode supprime toutes les données (intersections et tronçons) du plan.
	 */
	public void clear() {
		intersections.clear();
		troncons.clear();
	}
}
