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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MapDisplay extends Pane{
	
	DoubleProperty myScale = new SimpleDoubleProperty(1.0);
	
	List<Circle> demandeDeLivraisonInter;
	List<Circle> tourneeInter;
	List<Line> tourneeTroncons;
	Circle entrepotInter = null;
	
	List<Label> numerosLivraisons;
	
	Intersection entrepot = null; //shame, shame, shame
	
	// Map elements display
	
	// default map display
	final int defaultFontSize = 1000;
	
	final int defaultIntersectionRadius = 50;
	final Color defaultIntersectionColor = Color.WHITE;
	
	final int defaultTronconWidth = 80;
	final Color defaultTronconColor = Color.WHITE;

	// default livraison display
	final int livraisonIntersectionRadius = 200;
	final Color defaultLivraisonColor = Color.web("#ff0000");
	final Color defaultEntrepotColor = Color.BLACK;
	
	// default tournee display
	final int tourneeIntersectionRadius = 50;
	final int tourneeTronconWidth = 150;
	final Color defaultTourneeTronconColor = Color.web("#3399ff");
	final Color defaultTourneeIntersectionColor = Color.web("#3399ff");
	final Color defaultTourneeLivraisonColor = Color.web("#ff0000");
	
	
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
    	}
    	
    	// adding all intersections
    	for (Map.Entry<Long, Intersection> entry : intersections.entrySet()){
            Circle circle = this.creerVueIntersection(entry.getValue(), defaultIntersectionColor);
            getChildren().add(circle);
    	}
    	
    	this.setPrefHeight(maximalX-minimalX);
    	this.setPrefWidth(maximalX-minimalX);
    	
    }
    
    public void clearPlanDeVille() {
    	this.getChildren().clear();
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison demandeDeLivraison) {
    	
    	clearDemandeDeLivraison();
    	    	
    	// showing all the Livraison
    	final List<Livraison> livraisons = demandeDeLivraison.getListeLivraison();
    	
    	boolean entrepot = true;
    	for(Livraison l : livraisons) {
    		if(entrepot) {
    			Circle circle = creerVueLivraison(l, defaultEntrepotColor, livraisonIntersectionRadius);
        		demandeDeLivraisonInter.add(circle);
                getChildren().add(circle);
                entrepot = false;
                System.out.println("test");
    		}else {
    			Circle circle = creerVueLivraison(l, defaultLivraisonColor, livraisonIntersectionRadius);
        		demandeDeLivraisonInter.add(circle);
                getChildren().add(circle);
    		}
    		
    	}
    	
    	/*
    	// showing the entrepot
    	entrepotInter = creerVueIntersection(demandeDeLivraison.getAdresseEntrepot(), defaultEntrepotColor, livraisonIntersectionRadius);
    	entrepot = demandeDeLivraison.getAdresseEntrepot();
    	getChildren().add(entrepotInter);    	
    	*/
    	
    }
    
    public void clearDemandeDeLivraison() {
    	if(!demandeDeLivraisonInter.isEmpty()) {
    		// we remove the previous livraison
    		getChildren().removeAll(demandeDeLivraisonInter);
    		demandeDeLivraisonInter.clear();
    		getChildren().remove(entrepotInter);
    		entrepot = null;
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
    	
    	int i=0;
    	
    	for(Livraison l : livraisons) {
    		
    		Circle circle;
    		if( i == 0) {
    			circle = creerVueLivraison(l, defaultEntrepotColor, livraisonIntersectionRadius);
    		}else    {
    			circle = creerVueLivraison(l, defaultTourneeLivraisonColor, livraisonIntersectionRadius);
    		}
    		i++;
    		demandeDeLivraisonInter.add(circle);
            getChildren().add(circle);
            
            Label label = creerNumeroLivraison(l.getLieuDeLivraison(), Integer.toString(i) , defaultTourneeLivraisonColor, defaultFontSize);
            numerosLivraisons.add(label);
            getChildren().add(label);
    	}
    	
    	/*
    	// we show the entrepot
    	entrepotInter = creerVueIntersection(tournee.getAdresseEntrepot(), defaultEntrepotColor, livraisonIntersectionRadius);
    	entrepot = tournee.getAdresseEntrepot();
    	getChildren().add(entrepotInter);*/
    	
    }
    
    public void clearTournee() {
    	if(!tourneeInter.isEmpty()) {
    		getChildren().removeAll(tourneeInter);
    		tourneeInter.clear();
    		
    		getChildren().removeAll(tourneeTroncons);
    		tourneeTroncons.clear();
    		
    		getChildren().removeAll(demandeDeLivraisonInter);
    		
    		getChildren().remove(entrepotInter);
    		entrepot = null;
    	}
    	
    	if(!demandeDeLivraisonInter.isEmpty()) {
    		getChildren().removeAll(demandeDeLivraisonInter);
    		demandeDeLivraisonInter.clear();
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
    	return (coordY-minimalY)*(maximalX-minimalX)/(maximalY-minimalY);
    }
    
    public double getTransformedY(double coordX) {
    	return getPrefHeight()-(coordX-minimalX);
    }
    
    public Intersection getEntrepot() {
    	return entrepot;
    }
}
