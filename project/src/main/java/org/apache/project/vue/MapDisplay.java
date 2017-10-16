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
	List<Circle> tourneeTroncons;
	
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
        
        demandeDeLivraisonInter = new ArrayList();
        tourneeInter = new ArrayList();
        tourneeTroncons = new ArrayList();
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
    	
    	Map<Long, Intersection> intersections = plan.getAllIntersections();
    	
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
    	List<Troncon> troncons = plan.getAllTroncons();
    	
    	for(Troncon t : troncons){
    		Line line = new Line();
    		
    		line.setStartX(t.getIntersectionDepart().getCoordX()-minimalX);
    		line.setStartY(t.getIntersectionDepart().getCoordY()-minimalY);
    		
    		line.setEndX(t.getIntersectionArrivee().getCoordX()-minimalX);
    		line.setEndY(t.getIntersectionArrivee().getCoordY()-minimalY);
    		
    		line.setStroke(defaultTronconColor);
    		line.setStrokeWidth(defaultTronconWidth);
    		
    		getChildren().add(line);
    	}
    	
    	// adding all intersections
    	for (Map.Entry<Long, Intersection> entry : intersections.entrySet()){
    		
            Circle circle = new Circle();
            
            circle.setCenterX(entry.getValue().getCoordX()-minimalX);
            circle.setCenterY(entry.getValue().getCoordY()-minimalY);
            
            circle.setFill(defaultIntersectionColor);
            circle.setStroke(defaultIntersectionColor);
            circle.setRadius(defaultIntersectionRadius);
            
            getChildren().add(circle);
    	}
    	
    	
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison demandeDelivraison) {
    	
    	if(!demandeDeLivraisonInter.isEmpty()) {
    		// we remove the previous livraison
    		getChildren().removeAll(demandeDeLivraisonInter);
    		demandeDeLivraisonInter.clear();
    	}
    	
    	List<Livraison> livraisons = demandeDelivraison.getListeLivraison();
    	
    	for(Livraison l : livraisons) {
    		Circle circle = new Circle();
            
            circle.setCenterX(l.getLieuDeLivraison().getCoordX()-minimalX);
            circle.setCenterY(l.getLieuDeLivraison().getCoordY()-minimalY);
            
            circle.setFill(defaultLivraisonColor);
            circle.setStroke(defaultLivraisonColor);
            circle.setRadius(defaultIntersectionRadius);
            
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
    	}
    	
    	List<Chemin> chemins = tournee.getChemins();
    	
    	for(Chemin c : chemins) {
    		
    	}
    	
    }
}
