package org.apache.project.vue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.modele.Troncon;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/* Contient toutes les données de visualisation du plan (représentations des Troncons, des Intersections, couleurs, etc.)
 *
 */
public class MapDisplay extends Pane {

	private DoubleProperty myScale = new SimpleDoubleProperty(1.0);

	private Map<Intersection, Circle> mapIntersections;

	private Map<Troncon, Line> mapTroncons;

	private Map<Livraison, Circle> mapLivraisons;
	private Map<Troncon, Line> mapTourneeTroncons;

	private List<Label> numerosLivraisons;

	// highlight management
	private Object highlightedObject = null;
	private Paint oldColor = null;

	// default map display
	final int defaultFontSize = 1000;

	final int defaultIntersectionRadius = 80;
	final int defaultIntersectionRadiusHL = 200;
	final Color defaultIntersectionColor = Color.WHITE;
	final Color defaultIntersectionColorHL = Color.web("#007B02");

	final int defaultTronconWidth = 80;
	final Color defaultTronconColor = Color.WHITE;
	final int defaultTronconWidthHL = 200;
	final Color defaultTronconColorHL = Color.web("#007B02");

	// default livraison display
	final int livraisonIntersectionRadius = 300;
	final Color defaultLivraisonColor = Color.web("#425087");
	final Color defaultEntrepotColor = Color.BLACK;

	// highlighted livraison display
	final int livraisonIntersectionRadiusHL = 600;
	final Color defaultLivraisonColorHL = Color.web("#FAA12D");
	final Color defaultEntrepotColorHL = Color.BLACK;

	// default tournee display
	final int tourneeIntersectionRadius = 50;
	final int tourneeTronconWidth = 300;
	final Color defaultTourneeTronconColors[] = { Color.web("#33cc33"), Color.web("#003300") };
	final Color defaultTourneeIntersectionColor = Color.web("#3399ff");
	final Color defaultTourneeLivraisonColor = Color.web("#425087");

	private Long minimalX = Long.MAX_VALUE;
	private Long maximalX = 0l;
	private Long minimalY = Long.MAX_VALUE;
	private Long maximalY = 0l;

	/**
	 * Crée un objet <tt>MapDisplay</tt> à partir des dimensions de la zone du plan
	 * dans l'interface
	 * 
	 * @param height
	 *            Hauteur de la zone de visualisation de la carte
	 * @param width
	 *            Largeur de la zone de visualisation de la carte
	 */
	public MapDisplay(int height, int width) {
		setPrefSize(height, width);
		setStyle("-fx-background-color: #b2b2b2;");

		// add scale transform
		scaleXProperty().bind(myScale);
		scaleYProperty().bind(myScale);

		mapLivraisons = new HashMap<Livraison, Circle>();
		mapIntersections = new HashMap<Intersection, Circle>();
		mapTroncons = new HashMap<Troncon, Line>();
		mapTourneeTroncons = new HashMap<Troncon, Line>();
		numerosLivraisons = new ArrayList<Label>();
	}

	/**
	 * @return l'Échelle de la carte
	 */
	public double getScale() {
		return myScale.get();
	}

	/**
	 * @param scale
	 *            l'Échelle de la carte
	 */
	public void setScale(double scale) {
		myScale.set(scale);
	}

	/**
	 * Déplace le point de pivot de la carte. Il s'agit du point sur lequel se
	 * centre la vue lors du zoom.
	 * 
	 * @param x
	 *            La nouvelle coordonnée x du pivot
	 * @param y
	 *            La nouvelle coordonnée y du pivot
	 */
	public void setPivot(double x, double y) {
		setTranslateX(getTranslateX() - x);
		setTranslateY(getTranslateY() - y);
	}

	/**
	 * Affiche le plan de ville passé en paramètre dans l'interface.
	 * 
	 * @param plan
	 *            L'objet <tt>PlanDeVille</tt> contenant les données du plan à
	 *            afficher.
	 */
	public void afficherPlanDeVille(PlanDeVille plan) {

		clearPlanDeVille();

		final Map<Long, Intersection> intersections = plan.getAllIntersections();

		if (intersections.isEmpty())
			return;

		// first we must find the minimum and the maximum coordinate to
		// find if the coordinate system must be changed
		minimalX = Long.MAX_VALUE;
		maximalX = 0l;
		minimalY = Long.MAX_VALUE;
		maximalY = 0l;
		for (Map.Entry<Long, Intersection> entry : intersections.entrySet()) {
			Intersection i = entry.getValue();
			if (i.getCoordX() < minimalX)
				minimalX = i.getCoordX();
			if (i.getCoordX() > maximalX)
				maximalX = i.getCoordX();
			if (i.getCoordY() < minimalY)
				minimalY = i.getCoordY();
			if (i.getCoordY() > maximalY)
				maximalY = i.getCoordY();
		}

		this.setPrefHeight(maximalX - minimalX);
		this.setPrefWidth(maximalY - minimalY);

		// adding all troncons
		final List<Troncon> troncons = plan.getAllTroncons();

		for (Troncon t : troncons) {
			Line line = this.creerVueTroncon(t, defaultTronconColor);
			getChildren().add(line);
			mapTroncons.put(t, line);
		}

		// adding all intersections
		for (Map.Entry<Long, Intersection> entry : intersections.entrySet()) {
			Circle circle = this.creerVueIntersection(entry.getValue(), defaultIntersectionColor);
			getChildren().add(circle);
			mapIntersections.put(entry.getValue(), circle);
		}

		this.setPrefHeight(maximalX - minimalX);
		this.setPrefWidth(maximalX - minimalX);

	}

	/**
	 * Efface le plan de l'interface
	 */
	public void clearPlanDeVille() {
		this.getChildren().clear();
		mapIntersections.clear();
	}

	/**
	 * Affiche les points de livraison sur la carte
	 * 
	 * @param demandeDeLivraison
	 *            La demande de livraison à afficher
	 */
	public void afficherDemandeDeLivraison(DemandeDeLivraison demandeDeLivraison) {

		clearDemandeDeLivraison();

		// showing all the Livraison
		final List<Livraison> livraisons = demandeDeLivraison.getListeLivraison();

		boolean entrepot = true;
		for (Livraison l : livraisons) {
			if (entrepot) {
				Circle circle = creerVueLivraison(l, defaultEntrepotColor, livraisonIntersectionRadius);
				mapLivraisons.put(l, circle);
				getChildren().add(circle);
				entrepot = false;
			} else {
				Circle circle = creerVueLivraison(l, defaultLivraisonColor, livraisonIntersectionRadius);
				mapLivraisons.put(l, circle);
				getChildren().add(circle);
			}
		}
	}

	/**
	 * Met en valeur (sélectionne) un objet de la carte et réinitialise les objets
	 * précedemment mis en valeur
	 * 
	 * @param obj
	 *            L'objet à mettre en valeur
	 */
	public void resetAndHighlight(Object obj) {
		unhighlight();

		if (highlight(obj)) {
			highlightedObject = obj;
		}
	}

	/**
	 * Si un objet de la carte a été mis en valeur (sélectionné) par la méthode
	 * highlight, cette méthode réinitialise son format dans l'interface
	 */
	public void unhighlight() {
		if (highlightedObject != null)
			unHighlight(highlightedObject);
	}

	/**
	 * Met en valeur (sélectionne) un objet de la carte
	 * 
	 * @param obj
	 *            L'objet à mettre en valeur
	 * @return <tt>True</tt> si un objet Livraison, Intersection, ou Troncon, a été
	 *         mis en valeur<br>
	 *         <tt>False</tt> dans le cas contraire
	 */
	public boolean highlight(Object obj) {
		boolean found = false;
		if (obj instanceof Livraison) {
			found = highlightLivraison((Livraison) obj);
		} else if (obj instanceof Intersection) {
			found = highlightIntersection((Intersection) obj);
		} else if (obj instanceof Troncon) {
			found = highlightTroncon((Troncon) obj);
		}

		if (found) {
			highlightedObject = obj;
		}

		return false;
	}

	/**
	 * Réinitialise le format d'un objet mis en valeur (sélectionné)
	 * 
	 * @param obj
	 *            L'objet dont on doit réinitialiser le format
	 * @return True si le format d'un objet Livraison, Intersection, ou Troncon, a
	 *         été réinitialisé<br>
	 *         False dans le cas contraire
	 */
	public boolean unHighlight(Object obj) {
		if (obj instanceof Livraison) {
			return unHighlightLivraison((Livraison) obj);
		} else if (obj instanceof Intersection) {
			return unHighlightIntersection((Intersection) obj);
		} else if (obj instanceof Troncon) {
			return unHighlightTroncon((Troncon) obj);
		}
		highlightedObject = null;
		return false;
	}

	/**
	 * Met en valeur (sélectionne) une livraison
	 * 
	 * @param livraison
	 *            Livraison à mettre en valeur
	 * @return True si la livraison a été mise en valeur<br/>
	 *         False dans le cas contraire
	 */
	private boolean highlightLivraison(Livraison livraison) {
		Circle circle = mapLivraisons.get(livraison);

		if (circle != null) {
			circle.setRadius(livraisonIntersectionRadiusHL);
			oldColor = circle.getFill();
			circle.setStyle("-fx-effect: dropshadow(three-pass-box, #FFFFFFAA, 8000, 0.5, 0, 0);");
			return true;
		}
		return false;
	}

	/**
	 * Réinitialise le format d'une livraison (désélectionne)
	 * 
	 * @param livraison
	 *            Livraison dont on veut réinitialiser le format
	 * @return True si le format de la livraison a été réinitalisé<br/>
	 *         False dans le cas contraire
	 */
	private boolean unHighlightLivraison(Livraison livraison) {
		Circle circle = mapLivraisons.get(livraison);

		if (circle != null) {
			circle.setRadius(livraisonIntersectionRadius);
			if (oldColor != null) {
				circle.setFill(oldColor);
				oldColor = null;
			}
			circle.setStyle("");
			return true;
		}
		return false;
	}

	/**
	 * Met en valeur (sélectionne) une intersection
	 * 
	 * @param intersection
	 *            Intersection à mettre en valeur
	 * @return True si l'intersection a été mise en valeur<br/>
	 *         False dans le cas contraire
	 */
	private boolean highlightIntersection(Intersection intersection) {
		Circle circle = mapIntersections.get(intersection);

		if (circle != null) {
			circle.setRadius(defaultIntersectionRadiusHL);
			oldColor = circle.getFill();
			circle.setFill(defaultIntersectionColorHL);
			circle.setStyle("-fx-effect: dropshadow(three-pass-box, #FFFFFFAA, 8000, 0.5, 0, 0);");
			return true;
		}
		return false;
	}

	/**
	 * Réinitialise le format d'une intersection (désélectionne)
	 * 
	 * @param intersection
	 *            Intersection dont on veut réinitialiser le format
	 * @return True si le format de l'intersection a été réinitalisé<br/>
	 *         False dans le cas contraire
	 */
	private boolean unHighlightIntersection(Intersection intersection) {
		Circle circle = mapIntersections.get(intersection);

		if (circle != null) {
			circle.setRadius(defaultIntersectionRadius);
			if (oldColor != null) {
				circle.setFill(oldColor);
				oldColor = null;
			} else {
				circle.setFill(defaultIntersectionColor);
			}
			circle.setStyle("");
			return true;
		}
		return false;
	}

	/**
	 * Met en valeur (sélectionne) un troncon
	 * 
	 * @param troncon
	 *            Troncon à mettre en valeur
	 * @return True si le troncon a été mise en valeur<br/>
	 *         False dans le cas contraire
	 */
	private boolean highlightTroncon(Troncon troncon) {
		Line line = mapTourneeTroncons.get(troncon);
		if (line == null) {
			line = mapTroncons.get(troncon);
		}

		if (line != null) {
			line.toFront();
			oldColor = line.getStroke();
			line.setStroke(defaultTronconColorHL);
			line.setStrokeWidth(defaultTronconWidthHL);
			line.setStyle("-fx-effect: dropshadow(three-pass-box, #FFFFFFAA, 8000, 0.5, 0, 0);");
			return true;
		}

		return false;
	}

	/**
	 * Réinitialise le format d'un troncon (désélectionne)
	 * 
	 * @param troncon
	 *            Troncon dont on veut réinitialiser le format
	 * @return True si le format du troncon a été réinitalisé<br/>
	 *         False dans le cas contraire
	 */
	private boolean unHighlightTroncon(Troncon troncon) {
		Line line1 = mapTourneeTroncons.get(troncon);
		Line line2 = null;
		if (line1 == null) {
			line2 = mapTroncons.get(troncon);
		}
		if (line1 != null) {
			if (oldColor != null) {
				line1.setStroke(oldColor);
				oldColor = null;
			} else {
				line1.setStroke(this.defaultTronconColor);
			}
			line1.setStrokeWidth(tourneeTronconWidth);
			line1.setStyle("");
			livraisonToFront();
			return true;
		} else if (line2 != null) {
			if (oldColor != null) {
				line2.setStroke(oldColor);
				oldColor = null;
			} else {
				line2.setStroke(this.defaultTronconColor);
			}
			line2.setStrokeWidth(defaultTronconWidth);
			line2.setStyle("");
			line2.toBack();
			return true;
		}

		return false;
	}

	/**
	 * Affiche les livraisons par dessus des autres objets de la scène
	 */
	private void livraisonToFront() {
		if (!mapLivraisons.isEmpty()) {
			for (Circle c : mapLivraisons.values()) {
				c.toFront();
			}
		}
	}

	/**
	 * Efface l'affichage de la demande de livraison
	 */
	public void clearDemandeDeLivraison() {
		if (!mapLivraisons.isEmpty()) {
			// we remove the previous livraison
			// Might create errors
			for (Map.Entry<Livraison, Circle> entry : mapLivraisons.entrySet()) {
				getChildren().remove(entry.getValue());
			}
			mapLivraisons.clear();

			getChildren().removeAll(numerosLivraisons);
			numerosLivraisons.clear();
		}

		if (!numerosLivraisons.isEmpty()) {
			getChildren().removeAll(numerosLivraisons);
			numerosLivraisons.clear();
		}
	}

	/**
	 * Affiche la tournée passée en paramètre dans l'interface
	 * 
	 * @param tournee
	 *            L'objet <tt>Tournee</tt> contenant les données de la tournée que
	 *            l'on veut afficher
	 */
	public void afficherTournee(Tournee tournee) {
		clearDemandeDeLivraison();
		clearTournee();

		final List<Chemin> chemins = tournee.getChemins();

		int nombreTotalTronconsTournee = 0;
		for (Chemin c : chemins) {
			nombreTotalTronconsTournee += c.getTroncons().size();
		}

		Map<Troncon, Integer> tronconCount = new HashMap<Troncon, Integer>();
		Map<Troncon, List<Color>> tronconColors = new HashMap<Troncon, List<Color>>();

		// we show the Tournee
		int positionTronconDansTournee = 0;
		for (Chemin c : chemins) {

			final List<Troncon> troncons = c.getTroncons();

			for (Troncon t : troncons) {
				Color couleurTroncon = getColorGradientPoint(positionTronconDansTournee, nombreTotalTronconsTournee);

				Line line = mapTourneeTroncons.get(t);
				if (line == null) {
					line = this.creerVueTroncon(t, couleurTroncon, tourneeTronconWidth);

					mapTourneeTroncons.put(t, line);
					getChildren().add(line);
					tronconCount.put(t, new Integer(1));

					List<Color> listColorInitiale = new ArrayList<Color>();
					listColorInitiale.add(couleurTroncon);
					tronconColors.put(t, listColorInitiale);
				} else {
					Integer i = tronconCount.get(t);
					i++;
					tronconCount.put(t, i);

					List<Color> updatedListColor = tronconColors.get(t);
					updatedListColor.add(couleurTroncon);
					tronconColors.put(t, updatedListColor);
				}

				positionTronconDansTournee++;
			}
		}

		for (Map.Entry<Troncon, Integer> entry : tronconCount.entrySet()) {
			if (entry.getValue() > 1) {
				List<Color> listeCouleurs = tronconColors.get(entry.getKey());
				Line line = mapTourneeTroncons.get(entry.getKey());
				String style = this.getColorGradientPoint(listeCouleurs, line);
				line.setStyle(style);
			}
		}

		// we show the Livraisons
		final List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();

		Circle circle = creerVueLivraison(livraisons.get(0), defaultEntrepotColor, livraisonIntersectionRadius);
		mapLivraisons.put(livraisons.get(0), circle);
		getChildren().add(circle);

		for (int i = 1; i < livraisons.size() - 1; i++) {
			circle = creerVueLivraison(livraisons.get(i), defaultTourneeLivraisonColor, livraisonIntersectionRadius);

			mapLivraisons.put(livraisons.get(i), circle);
			getChildren().add(circle);

			Label label = creerNumeroLivraison(livraisons.get(i).getLieuDeLivraison(), Integer.toString(i),
					defaultTourneeLivraisonColor, defaultFontSize);
			numerosLivraisons.add(label);
			getChildren().add(label);
		}

	}

	/**
	 * Efface l'affichage de la tournée
	 */
	public void clearTournee() {
		if (!mapTourneeTroncons.isEmpty()) {
			for (Entry<Troncon, Line> entry : mapTourneeTroncons.entrySet())
				getChildren().remove(entry.getValue());
			mapTourneeTroncons.clear();
		}

		if (!mapLivraisons.isEmpty()) {
			// Might create errors
			for (Map.Entry<Livraison, Circle> entry : mapLivraisons.entrySet())
				getChildren().remove(entry.getValue());
			mapLivraisons.clear();
		}

		if (!numerosLivraisons.isEmpty()) {
			getChildren().removeAll(numerosLivraisons);
			numerosLivraisons.clear();
		}
	}

	/**
	 * Crée un Label contenant le numéro d'ordre de passage correspondant à une
	 * livraison
	 * 
	 * @param inter
	 *            L'intersection correspondant à la livraison à laquelle est
	 *            associée le numéro à afficher
	 * @param numero
	 *            La valeur à afficher
	 * @param color
	 *            La couleur du numéro à afficher
	 * @param taille
	 *            La taille de police du numéro à afficher
	 * @return L'objt <tt>Label</tt> du numéro d'ordre de passage associé à
	 *         l'intersection passée en paramètre
	 */
	private Label creerNumeroLivraison(Intersection inter, String numero, Color color, int taille) {
		Label label = new Label(numero);

		label.setTranslateX(getTransformedX(inter.getCoordY()) + livraisonIntersectionRadius * 2);
		label.setTranslateY(getTransformedY(inter.getCoordX()) - livraisonIntersectionRadius * 2);

		label.setFont(Font.font("Helvetica", FontWeight.BOLD, taille));
		label.setTextFill(color);

		label.setText(numero);

		return label;
	}

	/**
	 * Crée un cercle correspondant à la visualisation d'une intersection
	 * 
	 * @param inter
	 *            L'intersection dont on veut créer la vue
	 * @param color
	 *            La couleur souhaitée pour l'intersection
	 * @return Un objet <tt>Cercle</tt> représentant l'intersection passée en
	 *         paramètre
	 */
	private Circle creerVueIntersection(Intersection inter, Color color) {
		Circle circle = new Circle();

		circle.setUserData(inter);

		circle.setCenterX(getTransformedX(inter.getCoordY()));
		circle.setCenterY(getTransformedY(inter.getCoordX()));

		circle.setFill(color);
		circle.setStroke(color);
		circle.setRadius(defaultIntersectionRadius);

		return circle;
	}

	/**
	 * Crée un cercle correspondant à la visualisation d'une intersection
	 * 
	 * @param inter
	 *            L'intersection dont on veut créer la vue
	 * @param color
	 *            La couleur souhaitée pour l'intersection
	 * @param radius
	 *            Le rayon souhaité pour le cercle
	 * @returnUn objet <tt>Cercle</tt> représentant l'intersection passée en
	 *           paramètre
	 */
	private Circle creerVueIntersection(Intersection inter, Color color, int radius) {
		Circle circle = creerVueIntersection(inter, color);
		circle.setRadius(radius);

		return circle;
	}

	/**
	 * Crée une ligne correspondant à la visualisation d'un troncon
	 * 
	 * @param tronc
	 *            Le tronçon dont on veut créer la vue
	 * @param color
	 *            Le couleur souhaitée pour le troncon
	 * @return objet <tt>Line</tt> représentant un troncon passé en paramètre
	 */
	private Line creerVueTroncon(Troncon tronc, Paint color) {
		Line line = new Line();

		line.setUserData(tronc);

		line.setStartX(getTransformedX(tronc.getIntersectionDepart().getCoordY()));
		line.setStartY(getTransformedY(tronc.getIntersectionDepart().getCoordX()));

		line.setEndX(getTransformedX(tronc.getIntersectionArrivee().getCoordY()));
		line.setEndY(getTransformedY(tronc.getIntersectionArrivee().getCoordX()));

		line.setStroke(color);
		line.setStrokeWidth(defaultTronconWidth);

		return line;
	}

	/**
	 * Crée une ligne correspondant à la visualisation d'un troncon
	 * 
	 * @param tronc
	 *            Le tronçon dont on veut créer la vue
	 * @param color
	 *            Le couleur souhaitée pour le troncon
	 * @param width
	 *            La largeur du troncon
	 * @return objet <tt>Line</tt> représentant le troncon passé en paramètre
	 */
	private Line creerVueTroncon(Troncon tronc, Paint color, int width) {
		Line line = creerVueTroncon(tronc, color);
		line.setStrokeWidth(width);
		line.setStrokeLineCap(StrokeLineCap.ROUND);

		return line;
	}

	/**
	 * Crée un cercle correspondant à la visualisation d'une livraison
	 * 
	 * @param livraison
	 *            La livraison dont on veut créer la vue
	 * @param color
	 *            Le couleur souhaitée pour la livraison
	 * @param radius
	 *            Le rayon souhaitée pour le cercle
	 * @return objet <tt>Circle</tt> représentant un troncon passé en paramètre
	 */
	private Circle creerVueLivraison(Livraison livraison, Color color, int radius) {
		Circle circle = creerVueIntersection(livraison.getLieuDeLivraison(), color, radius);
		circle.setUserData(livraison);

		return circle;
	}

	/**
	 * @param coordX
	 * @return la coordonnée x transformée et rescalée pour s'afficher correctement
	 *         dans le plan
	 */
	private double getTransformedX(double coordY) {
		return (coordY - minimalY) * (maximalX - minimalX) / (maximalY - minimalY);
	}

	/**
	 * @param coordX
	 * @return la coordonnée x transformée et rescalée pour s'afficher correctement
	 *         dans le plan
	 */
	public double getTransformedY(double coordX) {
		return getPrefHeight() - (coordX - minimalX);
	}

	/**
	 * Donne la couleur d'un Troncon par rapport à sa position dans la tournée, pour
	 * créer un dégradé de couleur le long de la tournée
	 * 
	 * @param positionTroncon
	 *            La position du Troncon dans la tournée
	 * @param nombreTroncons
	 *            Le nombre de Troncon total de la tournée
	 * @return La couleur du Troncon
	 */
	private Color getColorGradientPoint(int positionTroncon, int nombreTroncons) {
		double percentage = (double) positionTroncon / (double) nombreTroncons;
		Color colorGradientPoint = defaultTourneeTronconColors[0].interpolate(defaultTourneeTronconColors[1],
				percentage);
		return colorGradientPoint;
	}

	/**
	 * Donne le gradient de couleur nécessaire pour afficher quand la livraison
	 * passe plusieurs fois dans le même Troncon, avec des couleurs différentes
	 * liées à l'avancement.
	 * 
	 * @param colors
	 *            La liste de couleurs à répartir dans le Troncon
	 * @param line
	 *            La ligne dans laquelle les couleurs doivent être ajoutées
	 * @return la chaine de caractère correspondant à la feuille de style CSS
	 *         nécessaire pour créer le gradient
	 */
	private String getColorGradientPoint(List<Color> colors, Line line) {
		if (!colors.isEmpty() && line != null) {
			int x1 = (int) line.getStartX();
			int x2 = (int) line.getEndX();

			int y1 = (int) line.getStartY();
			int y2 = (int) line.getEndY();

			int cx = (x1 + x2) / 2;
			int cy = (y1 + y2) / 2;

			x1 -= cx;
			y1 -= cy;
			x2 -= cx;
			y2 -= cy;

			// rotate both points
			int xtemp = x1;
			int ytemp = y1;
			x1 = -ytemp;
			y1 = xtemp;

			xtemp = x2;
			ytemp = y2;
			x2 = -ytemp;
			y2 = xtemp;

			// move the center point back to where it was
			x1 += cx;
			y1 += cy;
			x2 += cx;
			y2 += cy;

			String style = "-fx-stroke: linear-gradient(from " + x1 + " " + y1 + " to " + x2 + " " + y2;

			int size = colors.size();
			int i = 0;

			for (Color c : colors) {
				String hexColor = String.format("#%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255),
						(int) (c.getBlue() * 255));
				style += "," + hexColor + " " + (int) ((double) i / (double) size * 105) + '%';
				i++;
				style += "," + hexColor + " " + (int) ((double) i / (double) size * 95) + '%';
			}
			return style + ");";
		}
		return "";
	}
}
