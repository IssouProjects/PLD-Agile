package org.apache.project.modele;

/**
 * Un tronçon est une portion de rue qui porte son nom et part d'une
 * intersection pour finir sur une autre. Il a une longueur donnée.
 * 
 * Si une intersection A et une intersection B ne sont reliées que par un seul
 * tronçon, alors elle sont reliées par une rue à sens unique.
 * 
 * Si par contre A et B sont reliées par un tronçon allant de A vers B et un
 * tronçon allant de B vers A, alors elle sont reliées par une rue à double
 * sens.
 */
public class Troncon {

	private double longueur;
	private String nomRue;
	private Intersection IntersectionDepart;
	private Intersection IntersectionArrivee;

	/**
	 * Construit un tronçon partant de <tt>InterDepart</tt> et finissant à
	 * <tt>InterArrivee</tt>, de longueur <tt>longueur</tt> mètres et portant le nom
	 * <tt>nomRue</tt>
	 * 
	 * @param longueur
	 *            longueur en mètres du tronçon.
	 * @param InterDepart
	 *            intersection de départ du tronçon
	 * @param InterArrivee
	 *            intersection d'arrivée du tronçon.
	 * @param nomRue
	 *            nom de la rue dont fait partie le tronçon.
	 */
	public Troncon(double longueur, Intersection InterDepart, Intersection InterArrivee, String nomRue) {
		this.longueur = longueur;
		this.nomRue = nomRue;
		this.IntersectionDepart = InterDepart;
		this.IntersectionArrivee = InterArrivee;
	}

	public double getLongueur() {
		return longueur;
	}

	public void setLongueur(Long longueur) {
		this.longueur = longueur;
	}

	public String getNomRue() {
		return nomRue;
	}

	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}

	public Intersection getIntersectionDepart() {
		return IntersectionDepart;
	}

	public void setIntersectionDepart(Intersection IntersectionDepart) {
		this.IntersectionDepart = IntersectionDepart;
	}

	public Intersection getIntersectionArrivee() {
		return IntersectionArrivee;
	}

	public void setIntersectionArrivee(Intersection IntersectionArrivee) {
		this.IntersectionArrivee = IntersectionArrivee;
	}
}
