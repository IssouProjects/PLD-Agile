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
	
	// Map elements display
	final int defaultIntersectionRadius = 100;
	final Color defaultIntersectionColor = Color.WHITE;
	
	final int defaultTronconWidth = 80;
	final Color defaultTronconColor = Color.WHITE;
	
	final Color defaultLivraisonColor = Color.RED;
	
	final Color defaultTourneeTronconColor = Color.GREEN;
	final Color defaultTourneeIntersectionColor = Color.GREEN;
	final Color defaultTourneeLivraisonColor = Color.RED;
	
	
	Long minimalX = Long.MAX_VALUE;
	Long maximalX = 0l;
	Long minimalY = Long.MAX_VALUE;
	Long maximalY = 0l;
	

    public MapDisplay(int height, int width) {
        setPrefSize(height, width);
        setStyle("-fx-background-color: #dfdfdf;");

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
    	
    	this.setPrefWidth(maximalX-minimalX);
    	this.setPrefHeight(maximalY-minimalY);
    	
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
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison demandeDelivraison) {
    	
    	if(!demandeDeLivraisonInter.isEmpty()) {
    		// we remove the previous livraison
    		getChildren().removeAll(demandeDeLivraisonInter);
    		demandeDeLivraisonInter.clear();
    	}
    	
    	final List<Livraison> livraisons = demandeDelivraison.getListeLivraison();
    	
    	for(Livraison l : livraisons) {
    		Circle circle = creerVueIntersection(l.getLieuDeLivraison(), defaultLivraisonColor);
    		demandeDeLivraisonInter.add(circle);
            getChildren().add(circle);
    	}
    	
    }
    
    public void afficherTournee(Tournee tournee) {
    	
    	if(!tourneeInter.isEmpty()) {
    		// we remove the previous Tournee
    		getChildren().removeAll(tourneeInter);
    		tourneeInter.clear();
    		
    		getChildren().removeAll(tourneeTroncons);
    		tourneeTroncons.clear();
    		
    		getChildren().removeAll(demandeDeLivraisonInter);
    	}
    	
    	final List<Chemin> chemins = tournee.getChemins();
    	
    	// we show the Tournee
    	for(Chemin c : chemins) {
    		
    		final List<Troncon> troncons = c.getTroncons();
    		
    		for(Troncon t : troncons){
    			Circle circle = this.creerVueIntersection(t.getIntersectionArrivee(), defaultTourneeIntersectionColor);
                tourneeInter.add(circle);
                getChildren().add(circle);
                
                Line line = this.creerVueTroncon(t, defaultTourneeTronconColor);
                tourneeTroncons.add(line);
                getChildren().add(line);
    		}
    	}
    	
    	// we show the Livraisons
    	final List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
    	
    	for(Livraison l : livraisons) {
    		Circle circle = creerVueIntersection(l.getLieuDeLivraison(), defaultTourneeLivraisonColor);
    		demandeDeLivraisonInter.add(circle);
            getChildren().add(circle);
    	}
    	
    }
    
    public Circle creerVueIntersection(Intersection inter, Color color) {
    	Circle circle = new Circle();
    	
    	circle.setCenterX(inter.getCoordX()-minimalX);
    	circle.setCenterY(inter.getCoordY()-minimalY);
    	
    	circle.setFill(color);
        circle.setStroke(color);
        circle.setRadius(defaultIntersectionRadius);
        
        return circle;
    }
    
    public Line creerVueTroncon(Troncon tronc, Color color) {
    	Line line = new Line();
		
		line.setStartX(tronc.getIntersectionDepart().getCoordX()-minimalX);
		line.setStartY(tronc.getIntersectionDepart().getCoordY()-minimalY);
		
		line.setEndX(tronc.getIntersectionArrivee().getCoordX()-minimalX);
		line.setEndY(tronc.getIntersectionArrivee().getCoordY()-minimalY);
		
		line.setStroke(color);
		line.setStrokeWidth(defaultTronconWidth);
		
		return line;
    }
}
