package org.apache.project.vue;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class FenetrePrincipale extends Application{
	public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage stage) {
    	
    	stage.setTitle("Issou delivery optimiser 2000");
        
    	// layout for the full window
    	HBox layout = new HBox();
    	layout.setStyle("-fx-padding: 10; -fx-spacing: 10;");
    	
    	Scene scene = new Scene(layout, 1024, 500);
    	
    	/////////////////////////////////////////////
    	///// CREATING THE MAP AND ITS CONTROLS /////
    	/////////////////////////////////////////////
    	
    	// layout for the map and its controls buttons
    	VBox mapLayout = new VBox();
    	
    	HBox mapButtonsLayout = new HBox();
    	
    	Button resetPosButton = new Button("Reset map position");
    	Button resetScaleButton = new Button("Reset map zoom");
    	
    	mapButtonsLayout.setAlignment(Pos.CENTER);
    	mapButtonsLayout.setSpacing(10);
    	
    	mapButtonsLayout.getChildren().add(resetPosButton);
    	mapButtonsLayout.getChildren().add(resetScaleButton);
        
        MapContainer map = new MapContainer(600,600, scene);
        mapLayout.getChildren().add(map);
        mapLayout.getChildren().add(mapButtonsLayout);
        mapLayout.setSpacing(10d);
    	
        layout.getChildren().add(mapLayout);
        
		/////////////////////////////////////////////
		///// 	CREATING THE DELIVERY LIST	    /////
		/////////////////////////////////////////////
        
        ListView liste = new ListView();
        liste.getItems().add("Livraison 1");
        liste.getItems().add("Livraison 2");
        liste.getItems().add("Livraison 3");
        liste.getItems().add("Livraison 4");
        
        layout.getChildren().add(liste);
        layout.setHgrow(liste, Priority.ALWAYS);
        liste.setMaxWidth(Double.MAX_VALUE);
        
        
		/////////////////////////////////////////////
		///// 			MAPPING BUTTONS  	    /////
		/////////////////////////////////////////////
        
        resetPosButton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	        map.resetMapPosition();
    	    }
    	});
        resetScaleButton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	        map.resetMapZoom();
    	    }
    	});
        
        
        stage.setScene(scene);
        stage.show();

    }
}



