package org.apache.project.vue;

import java.util.List;
import java.util.Map;

import org.apache.project.modele.Intersection;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Troncon;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class MapDisplay extends Pane{
	
	DoubleProperty myScale = new SimpleDoubleProperty(1.0);
	
	// Map elements display
	final int defaultIntersectionRadius = 3;
	final Color defaultIntersectionColor = Color.WHITE;
	
	final int defaultTronconWidth = 3;
	final Color defaultTronconColor = Color.WHITE;
	
	Integer minimalX = Integer.MAX_VALUE;
	Integer maximalX = 0;
	Integer minimalY = Integer.MAX_VALUE;
	Integer maximalY = 0;
	

    public MapDisplay(int height, int width) {
        setPrefSize(height, width);
        setStyle("-fx-background-color: #dfdfdf;");

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
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
    	
    	
    	Map<Integer, Intersection> intersections = plan.getAllIntersections();
    	
    	// first we must find the minimum and the maximum coordinate to
    	// find if the coordinate system must be changed
    	minimalX = Integer.MAX_VALUE;
    	maximalX = 0;
    	minimalY = Integer.MAX_VALUE;
    	maximalY = 0;
    	for (Map.Entry<Integer, Intersection> entry : intersections.entrySet()){
    		Intersection i = entry.getValue();
    		if(i.getCoordX() < minimalX) minimalX = i.getCoordX();
    		if(i.getCoordX() > maximalX) maximalX = i.getCoordX();
    		if(i.getCoordX() < minimalY) minimalY = i.getCoordY();
    		if(i.getCoordX() > maximalY) maximalY = i.getCoordY();
    	}
    	
    	// adding all intersections
    	for (Map.Entry<Integer, Intersection> entry : intersections.entrySet()){
    		
            Circle circle = new Circle();
            
            circle.setCenterX(entry.getValue().getCoordX()-minimalX);
            circle.setCenterY(entry.getValue().getCoordY()-minimalY);
            
            circle.setStroke(defaultIntersectionColor);
            circle.setRadius(defaultIntersectionRadius);
            
            getChildren().add(circle);
    	}
    	
    	//adding all troncons
    	List<Troncon> troncons = plan.getAllTroncons();
    	
    	for(Troncon t : troncons){
    		Line line = new Line();
    		
    		line.setStartX(t.getIdIntersectionDepart().getCoordX()-minimalX);
    		line.setStartY(t.getIdIntersectionDepart().getCoordY()-minimalY);
    		
    		line.setEndX(t.getIdIntersectionDepart().getCoordX()-minimalX);
    		line.setEndY(t.getIdIntersectionDepart().getCoordY()-minimalX);
    	}
    }
}
