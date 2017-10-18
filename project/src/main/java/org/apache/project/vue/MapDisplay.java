package org.apache.project.vue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.modele.Troncon;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class MapDisplay extends Pane{
	
	DoubleProperty myScale = new SimpleDoubleProperty(1.0);
	
	List<Circle> demandeDeLivraisonInter;
	List<Circle> tourneeInter;
	List<Line> tourneeTroncons;
	Circle entrepotInter;
	
	// Map elements display
	
	// default map display
	final int defaultIntersectionRadius = 50;
	final Color defaultIntersectionColor = Color.WHITE;
	
	final int defaultTronconWidth = 80;
	final Color defaultTronconColor = Color.WHITE;

	// default livraison display
	final int livraisonIntersectionRadius = 400;
	final Color defaultLivraisonColor = Color.BLUE;
	final Color defaultEntrepotColor = Color.BLACK;
	
	// default tournee display
	final int tourneeIntersectionRadius = 200;
	final int tourneeTronconWidth = 150;
	final Color defaultTourneeTronconColor = Color.RED;
	final Color defaultTourneeIntersectionColor = Color.RED;
	final Color defaultTourneeLivraisonColor = Color.BLUE;
	
	
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
        
        demandeDeLivraisonInter = new ArrayList<Circle>();
        tourneeInter = new ArrayList<Circle>();
        tourneeTroncons = new ArrayList<Line>();
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
    	
    	// deleting the previous plan
    	this.getChildren().clear();
    	
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
    	}
    	
    	// adding all intersections
    	for (Map.Entry<Long, Intersection> entry : intersections.entrySet()){
            Circle circle = this.creerVueIntersection(entry.getValue(), defaultIntersectionColor);
            getChildren().add(circle);
    	}
    	
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison demandeDeLivraison) {
    	
    	if(!demandeDeLivraisonInter.isEmpty()) {
    		// we remove the previous livraison
    		getChildren().removeAll(demandeDeLivraisonInter);
    		demandeDeLivraisonInter.clear();
    		getChildren().remove(entrepotInter);
    	}
    	    	
    	// showing all the Livraison
    	final List<Livraison> livraisons = demandeDeLivraison.getListeLivraison();
    	
    	for(Livraison l : livraisons) {
    		Circle circle = creerVueIntersection(l.getLieuDeLivraison(), defaultLivraisonColor, livraisonIntersectionRadius);
    		demandeDeLivraisonInter.add(circle);
            getChildren().add(circle);
    	}
    	
    	// showing the entrepot
    	entrepotInter = creerVueIntersection(demandeDeLivraison.getAdresseEntrepot(), defaultEntrepotColor, livraisonIntersectionRadius); 
    	getChildren().add(entrepotInter);    	
    	
    }
    
    public void afficherTournee(Tournee tournee) {
    	
    	if(!tourneeInter.isEmpty()) {
    		// we remove the previous Tournee
    		getChildren().removeAll(tourneeInter);
    		tourneeInter.clear();
    		
    		getChildren().removeAll(tourneeTroncons);
    		tourneeTroncons.clear();
    		
    		getChildren().removeAll(demandeDeLivraisonInter);
    		
    		getChildren().remove(entrepotInter);
    	}
    	
    	final List<Chemin> chemins = tournee.getChemins();
    	
    	// we show the Tournee
    	for(Chemin c : chemins) {
    		
    		final List<Troncon> troncons = c.getTroncons();
    		
    		for(Troncon t : troncons){
    			Circle circle = this.creerVueIntersection(t.getIntersectionArrivee(), defaultTourneeIntersectionColor, tourneeIntersectionRadius);
                tourneeInter.add(circle);
                getChildren().add(circle);
                
                Line line = this.creerVueTroncon(t, defaultTourneeTronconColor, tourneeTronconWidth);
                tourneeTroncons.add(line);
                getChildren().add(line);
    		}
    	}
    	
    	// we show the Livraisons
    	final List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
    	
    	for(Livraison l : livraisons) {
    		Circle circle = creerVueIntersection(l.getLieuDeLivraison(), defaultTourneeLivraisonColor, livraisonIntersectionRadius);
    		demandeDeLivraisonInter.add(circle);
            getChildren().add(circle);
    	}
    	
    	// we show the entrepot
    	entrepotInter = creerVueIntersection(tournee.getAdresseEntrepot(), defaultEntrepotColor, livraisonIntersectionRadius);
    	getChildren().add(entrepotInter);
    	
    }
    
    public Circle creerVueIntersection(Intersection inter, Color color) {
    	Circle circle = new Circle();
    	
    	circle.setCenterX(inter.getCoordY()-minimalY);
    	circle.setCenterY(getPrefHeight()-(inter.getCoordX()-minimalX));
    	
    	circle.setFill(color);
        circle.setStroke(color);
        circle.setRadius(defaultIntersectionRadius);
        
        return circle;
    }
    
    public Circle creerVueIntersection(Intersection inter, Color color, int radius) {
    	Circle circle = new Circle();
    	
    	circle.setCenterX(inter.getCoordY()-minimalY);
    	circle.setCenterY(getPrefHeight()-(inter.getCoordX()-minimalX));
    	
    	circle.setFill(color);
        circle.setStroke(color);
        circle.setRadius(radius);
        
        return circle;
    }
    
    public Line creerVueTroncon(Troncon tronc, Color color) {
    	Line line = new Line();
		
		line.setStartX(tronc.getIntersectionDepart().getCoordY()-minimalY);
		line.setStartY(getPrefHeight()-(tronc.getIntersectionDepart().getCoordX()-minimalX));
		
		line.setEndX(tronc.getIntersectionArrivee().getCoordY()-minimalY);
		line.setEndY(getPrefHeight()-(tronc.getIntersectionArrivee().getCoordX()-minimalX));
		
		line.setStroke(color);
		line.setStrokeWidth(defaultTronconWidth);
		
		return line;
    }
    
    public Line creerVueTroncon(Troncon tronc, Color color, int width) {
    	Line line = new Line();
		
		line.setStartX(tronc.getIntersectionDepart().getCoordY()-minimalY);
		line.setStartY(getPrefHeight()-(tronc.getIntersectionDepart().getCoordX()-minimalX));
		
		line.setEndX(tronc.getIntersectionArrivee().getCoordY()-minimalY);
		line.setEndY(getPrefHeight()-(tronc.getIntersectionArrivee().getCoordX()-minimalX));
		
		line.setStroke(color);
		line.setStrokeWidth(width);
		
		return line;
    }
}
