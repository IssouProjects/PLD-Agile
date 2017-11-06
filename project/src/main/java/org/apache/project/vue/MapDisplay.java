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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MapDisplay extends Pane {

	DoubleProperty myScale = new SimpleDoubleProperty(1.0);

	Map<Intersection, Circle> mapIntersections;
	
	Map<Troncon, Line> mapTroncons;

	Map<Livraison, Circle> mapLivraisons;
	List<Circle> tourneeInter;
	Map<Troncon, Line> mapTourneeTroncons;

	List<Label> numerosLivraisons;

	// highlight management
	Object highlightedObject = null;
	Paint oldColor = null;

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
	final Color defaultTourneeTronconColors[] = {Color.web("#3399ff"), Color.web("#33ffbe"), Color.web("#44ff33"), Color.web("e0ff33"), Color.web("ff8133")};
	final Color defaultTourneeIntersectionColor = Color.web("#3399ff");
	final Color defaultTourneeLivraisonColor = Color.web("#425087");

	Long minimalX = Long.MAX_VALUE;
	Long maximalX = 0l;
	Long minimalY = Long.MAX_VALUE;
	Long maximalY = 0l;	
	
    public MapDisplay(int height, int width) {
        setPrefSize(height, width);
        setStyle("-fx-background-color: #b2b2b2;");        

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
        
        mapLivraisons = new HashMap<Livraison, Circle>();
        mapIntersections = new HashMap<Intersection, Circle>();
        mapTroncons = new HashMap<Troncon, Line>();
        tourneeInter = new ArrayList<Circle>();
        mapTourneeTroncons = new HashMap<Troncon, Line>();
        numerosLivraisons = new ArrayList<Label>();
    }
    
    public double getScale() {
        return myScale.get();
    }

    public void setScale( double scale) {
        myScale.set(scale);
    }

    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
    
    public void afficherPlanDeVille(PlanDeVille plan){
    	
    	clearPlanDeVille();
    	
    	final Map<Long, Intersection> intersections = plan.getAllIntersections();
    	
    	if(intersections.isEmpty())
    		return;
    	
    	// first we must find the minimum and the maximum coordinate to
    	// find if the coordinate system must be changed
    	minimalX = Long.MAX_VALUE;
    	maximalX = 0l;
    	minimalY = Long.MAX_VALUE;
    	maximalY = 0l;
    	for (Map.Entry<Long, Intersection> entry : intersections.entrySet()){
    		Intersection i = entry.getValue();
    		if(i.getCoordX() < minimalX) minimalX = i.getCoordX();
    		if(i.getCoordX() > maximalX) maximalX = i.getCoordX();
    		if(i.getCoordY() < minimalY) minimalY = i.getCoordY();
    		if(i.getCoordY() > maximalY) maximalY = i.getCoordY();
    	}
    	
    	this.setPrefHeight(maximalX-minimalX);
    	this.setPrefWidth(maximalY-minimalY);
    	
    	//adding all troncons
    	final List<Troncon> troncons = plan.getAllTroncons();
    	
    	for(Troncon t : troncons){
    		Line line = this.creerVueTroncon(t, defaultTronconColor);
    		getChildren().add(line);
    		mapTroncons.put(t, line);
    	}
    	
    	// adding all intersections
    	for (Map.Entry<Long, Intersection> entry : intersections.entrySet()){
            Circle circle = this.creerVueIntersection(entry.getValue(), defaultIntersectionColor);
            getChildren().add(circle);
            mapIntersections.put(entry.getValue(), circle);
    	}
    	
    	this.setPrefHeight(maximalX-minimalX);
    	this.setPrefWidth(maximalX-minimalX);
    	
    }
    
    public void clearPlanDeVille() {
    	this.getChildren().clear();
    	mapIntersections.clear();
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison demandeDeLivraison) {
    	
    	clearDemandeDeLivraison();
    	    	
    	// showing all the Livraison
    	final List<Livraison> livraisons = demandeDeLivraison.getListeLivraison();
    	
    	boolean entrepot = true;
    	for(Livraison l : livraisons) {
    		if(entrepot) {
    			Circle circle = creerVueLivraison(l, defaultEntrepotColor, livraisonIntersectionRadius);
        		mapLivraisons.put(l, circle);
                getChildren().add(circle);
                entrepot = false;
    		}else {
    			Circle circle = creerVueLivraison(l, defaultLivraisonColor, livraisonIntersectionRadius);
    			mapLivraisons.put(l, circle);
                getChildren().add(circle);
    		}
    	}    	
    }
    
    public void resetAndHighlight(Object obj) {
    	unhighlight();
		
		if(highlight(obj)) {
			highlightedObject = obj;
		}
    }
    
    public void unhighlight() {
    	if(highlightedObject != null)
			unHighlight(highlightedObject);
    }
    
    public boolean highlight(Object obj) {
    	boolean found = false;
    	if(obj instanceof Livraison) {
    		found = highlightLivraison((Livraison)obj);
    	}else if(obj instanceof Intersection) {
    		found = highlightIntersection((Intersection)obj);
    	}else if(obj instanceof Troncon) {
    		found = highlightTroncon((Troncon)obj);
    	}
    	
    	if(found) {
    		highlightedObject = obj;
    	}
    	
    	return false;
    }
    
    public boolean unHighlight(Object obj) {
    	if(obj instanceof Livraison) {
    		return unHighlightLivraison((Livraison)obj);
    	}else if(obj instanceof Intersection) {
    		return unHighlightIntersection((Intersection)obj);
    	}else if(obj instanceof Troncon) {
    		return unHighlightTroncon((Troncon)obj);
    	}
    	highlightedObject = null;
    	return false;
    }
    
    public boolean highlightLivraison(Livraison livraison) {
    	Circle circle = mapLivraisons.get(livraison);
    	
    	if(circle != null) {
    		circle.setRadius(livraisonIntersectionRadiusHL);
    		oldColor = circle.getFill();
    		circle.setStyle("-fx-effect: dropshadow(three-pass-box, #FFFFFFAA, 8000, 0.5, 0, 0);");
    		return true;
    	}
    	return false;
    }
    
    public boolean unHighlightLivraison(Livraison livraison) {
    	Circle circle = mapLivraisons.get(livraison);
    	
    	if(circle != null) {
    		circle.setRadius(livraisonIntersectionRadius);
    		if(oldColor != null) {
    			circle.setFill(oldColor);
    			oldColor = null;
    		}
    		circle.setStyle("");
    		return true;
    	}
    	return false;
    }
    
    public boolean highlightIntersection(Intersection intersection) {
    	Circle circle = mapIntersections.get(intersection);
    	
    	if(circle != null) {
    		circle.setRadius(defaultIntersectionRadiusHL);
    		oldColor = circle.getFill();
    		circle.setFill(defaultIntersectionColorHL);
    		circle.setStyle("-fx-effect: dropshadow(three-pass-box, #FFFFFFAA, 8000, 0.5, 0, 0);");
    		return true;
    	}
    	return false;
    }
    
    public boolean unHighlightIntersection(Intersection intersection) {
    	Circle circle = mapIntersections.get(intersection);
    	
    	if(circle != null) {
    		circle.setRadius(defaultIntersectionRadius);
    		if(oldColor != null) {
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
    
    public boolean highlightTroncon(Troncon troncon) {
    	Line line = mapTourneeTroncons.get(troncon);
    	if(line == null) {
    		line = mapTroncons.get(troncon);
    	}
    	
    	if(line != null) {
    		line.toFront();
    		oldColor = line.getStroke();
    		line.setStroke(defaultTronconColorHL);
    		line.setStrokeWidth(defaultTronconWidthHL);
    		line.setStyle("-fx-effect: dropshadow(three-pass-box, #FFFFFFAA, 8000, 0.5, 0, 0);");
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean unHighlightTroncon(Troncon troncon) {
    	Line line1 = mapTourneeTroncons.get(troncon);
    	Line line2 = null;
    	if(line1 == null) {
    		line2 = mapTroncons.get(troncon);
    	}
    	if(line1 != null) {
    		if(oldColor != null) {
    			line1.setStroke(oldColor);
    			oldColor = null;
    		} else {
    			line1.setStroke(this.defaultTronconColor);
    		}
    		line1.setStrokeWidth(tourneeTronconWidth);
    		line1.setStyle("");
    		livraisonToFront();
    		return true;
    	}else if(line2 != null) {
    		if(oldColor != null) {
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
    
    private void livraisonToFront() {
    	if(!mapLivraisons.isEmpty()) {
    		for(Circle c : mapLivraisons.values()) {
    			c.toFront();
    		}
    	}
    }
    
    public void clearDemandeDeLivraison() {
    	if(!mapLivraisons.isEmpty()) {
    		// we remove the previous livraison
    		// Might create errors
    		for(Map.Entry<Livraison, Circle> entry : mapLivraisons.entrySet())
    			getChildren().remove(entry.getValue());
    		mapLivraisons.clear();

    		getChildren().removeAll(numerosLivraisons);
    		numerosLivraisons.clear();
    	}
    	 
    	
    	if(!numerosLivraisons.isEmpty()) {
    		getChildren().removeAll(numerosLivraisons);
    		numerosLivraisons.clear();
    	}
    }
    
    public void afficherTournee(Tournee tournee) {
    	clearDemandeDeLivraison();
    	clearTournee();
    	
    	final List<Chemin> chemins = tournee.getChemins();
    	
    	int nombreTotalTronconsTournee = 0;
    	for(Chemin c : chemins) {
    		nombreTotalTronconsTournee += c.getTroncons().size();
    	}
    	
    	Map<Troncon, Integer> tronconCount = new HashMap<Troncon, Integer>();
    	
    	// we show the Tournee
    	int positionTronconDansTournee = 0;
    	for(Chemin c : chemins) {
    		
    		final List<Troncon> troncons = c.getTroncons();
    		
    		for(Troncon t : troncons){
    			Circle circle = this.creerVueIntersection(t.getIntersectionArrivee(), getColorGradientPoint(positionTronconDansTournee, nombreTotalTronconsTournee), tourneeIntersectionRadius);
                tourneeInter.add(circle);
                getChildren().add(circle);
                mapIntersections.put(t.getIntersectionArrivee(), circle);
               
                Line line = mapTourneeTroncons.get(t);
                if(line == null) {
	                line = this.creerVueTroncon(t, getColorGradientPoint(positionTronconDansTournee, nombreTotalTronconsTournee), tourneeTronconWidth);
	                mapTourneeTroncons.put(t,line);
	                getChildren().add(line);
	                tronconCount.put(t, new Integer(1));
                } else {
                	Integer i = tronconCount.get(t);
                	i++;
                	tronconCount.put(t, i);
                }

                positionTronconDansTournee++;
    		}
    	}
    	
    	// ici, ajouter la méthode pour superposer les troncons
    	
    	// we show the Livraisons
    	final List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
    	
    	Circle circle = creerVueLivraison(livraisons.get(0), defaultEntrepotColor, livraisonIntersectionRadius);
    	mapLivraisons.put(livraisons.get(0), circle);
    	getChildren().add(circle);
    	
    	for(int i = 1; i< livraisons.size()-1; i++) {
    		circle = creerVueLivraison(livraisons.get(i), defaultTourneeLivraisonColor, livraisonIntersectionRadius);
	
    		mapLivraisons.put(livraisons.get(i), circle);
    		getChildren().add(circle);
        
    		Label label = creerNumeroLivraison(livraisons.get(i).getLieuDeLivraison(), Integer.toString(i) , defaultTourneeLivraisonColor, defaultFontSize);
    		numerosLivraisons.add(label);
    		getChildren().add(label);
    	}
    	
    }
    
    public void clearTournee() {
    	if(!tourneeInter.isEmpty()) {
    		getChildren().removeAll(tourneeInter);
    		for(Circle i : tourneeInter) {
    			mapIntersections.remove(i.getUserData());
    		}
    		tourneeInter.clear();
    		
    		for(Entry<Troncon, Line> entry : mapTourneeTroncons.entrySet())
    			getChildren().remove(entry.getValue());
    		mapTourneeTroncons.clear();
    	}
    	
    	if(!mapLivraisons.isEmpty()) {
    		// Might create errors
    		for(Map.Entry<Livraison, Circle> entry : mapLivraisons.entrySet())
    			getChildren().remove(entry.getValue());
    		mapLivraisons.clear();
    	}
    	
    	if(!numerosLivraisons.isEmpty()) {
    		getChildren().removeAll(numerosLivraisons);
    		numerosLivraisons.clear();
    	}
    }
    
    public Label creerNumeroLivraison(Intersection inter, String numero, Color color, int taille) {
    	Label label = new Label(numero);
    	
    	label.setTranslateX(getTransformedX(inter.getCoordY()) + livraisonIntersectionRadius*2);
    	label.setTranslateY(getTransformedY(inter.getCoordX()) - livraisonIntersectionRadius*2);
    	
    	label.setFont(Font.font("Helvetica", FontWeight.BOLD, taille));
    	label.setTextFill(color);
    	
    	label.setText(numero);
        
        return label;
    }
    
    public Circle creerVueIntersection(Intersection inter,  Color color) {
    	Circle circle = new Circle();
    	
    	circle.setUserData(inter);
    	
    	circle.setCenterX(getTransformedX(inter.getCoordY()));
    	circle.setCenterY(getTransformedY(inter.getCoordX()));
    	
    	circle.setFill(color);
        circle.setStroke(color);
        circle.setRadius(defaultIntersectionRadius);
        
        return circle;
    }
    
    public Circle creerVueIntersection(Intersection inter, Color color, int radius) {
    	Circle circle = creerVueIntersection(inter, color);
        circle.setRadius(radius);
        
        return circle;
    }
    
    public Line creerVueTroncon(Troncon tronc, Color color) {
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

	public Line creerVueTroncon(Troncon tronc, Color color, int width) {
		Line line = creerVueTroncon(tronc, color);
		line.setStrokeWidth(width);

		return line;
	}

	public Circle creerVueLivraison(Livraison livraison, Color color, int width) {
		Circle circle = creerVueIntersection(livraison.getLieuDeLivraison(), color, width);
		circle.setUserData(livraison);

		return circle;
	}

	public double getTransformedX(double coordY) {
		return (coordY - minimalY) * (maximalX - minimalX) / (maximalY - minimalY);
	}

	public double getTransformedY(double coordX) {
		return getPrefHeight() - (coordX - minimalX);
	}
	
	private Color getColorGradientPoint(int positionTroncon, int nombreTroncons) {
		double percentage = (double)positionTroncon/(double)nombreTroncons;
		Color colorGradientPoint = defaultTourneeTronconColors[0].interpolate(defaultTourneeTronconColors[1], percentage);
		return colorGradientPoint;
	}
}
