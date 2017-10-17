package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FenetrePrincipale extends Application{
	
	MapContainer mapContainer;
	Controleur controleur;
	
	Button loadMapButton;
	Button fitMapButton;
	
	// String appearing in the user interface
	public static final String LOAD_MAP = "Load map";
	
	public static void launchApp(String[] args) {
        Application.launch(FenetrePrincipale.class, args);
    }
	
    @Override
    public void start(Stage stage) {
    	
    	controleur = controleur.getInstance();
    	controleur.setFenetre(this);
    	
    	stage.setTitle("Issou delivery optimiser 2000");
        
    	// layout for the full window
    	GridPane layout = new GridPane();
    	layout.setStyle("-fx-padding: 10;");
    	
    	Scene scene = new Scene(layout, 1024, 500);
    	
    	/////////////////////////////////////////////
    	///// CREATING THE MAP AND ITS CONTROLS /////
    	/////////////////////////////////////////////
    	
    	// layout for the map and its controls buttons
    	VBox mapLayout = new VBox();
    	
    	HBox mapButtonsLayout = new HBox();
    	
    	fitMapButton = new Button("Fit map in view");
    	fitMapButton.setDisable(true);
    	loadMapButton = new Button(LOAD_MAP);
    	
    	mapButtonsLayout.setAlignment(Pos.CENTER);
    	mapButtonsLayout.setSpacing(10);
    	
    	mapButtonsLayout.getChildren().add(loadMapButton);
    	mapButtonsLayout.getChildren().add(fitMapButton);
        
        mapContainer = new MapContainer(2000,2000, scene);
        mapLayout.getChildren().add(mapContainer);
        mapLayout.getChildren().add(mapButtonsLayout);
        mapLayout.setSpacing(10d);
    	
        layout.add(mapLayout, 0, 0);
        
		/////////////////////////////////////////////
		///// 	CREATING THE DELIVERY LIST	    /////
		/////////////////////////////////////////////
        
        ListView liste = new ListView();
        liste.getItems().add("Livraison 1");
        liste.getItems().add("Livraison 2");
        liste.getItems().add("Livraison 3");
        liste.getItems().add("Livraison 4");
        
        layout.add(liste, 1, 0);
        layout.setHgrow(liste, Priority.ALWAYS);
        liste.setMaxWidth(Double.MAX_VALUE);
        
        
		/////////////////////////////////////////////
		///// 			MAPPING BUTTONS  	    /////
		/////////////////////////////////////////////
        
        EcouteurDeBouton edb = new EcouteurDeBouton(controleur);
        
        fitMapButton.setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent e) {
    	        mapContainer.fitMapInView();
    	    }
    	});
        
        loadMapButton.setOnAction(edb);
        
        // layout style
        
        ColumnConstraints MapCC = new ColumnConstraints();
        MapCC.setPercentWidth(67.0);
        MapCC.setHgrow(Priority.ALWAYS);
        layout.getColumnConstraints().add(MapCC);
        
        ColumnConstraints ListCC = new ColumnConstraints();
        ListCC.setPercentWidth(33.0);
        ListCC.setHgrow(Priority.ALWAYS);
        layout.getColumnConstraints().add(ListCC);
        
        
        // we can now show the window
        stage.setScene(scene);
        stage.show();
    }
    
    public void afficherPlanDeVille(PlanDeVille plan){
    	mapContainer.getMapDisplay().afficherPlanDeVille(plan);
    	mapContainer.fitMapInView();
    	loadMapButton.setDisable(true);
    	fitMapButton.setDisable(false);
    	
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison livraison) {
    	mapContainer.getMapDisplay().afficherDemandeDeLivraison(livraison);
    }
    
    public void afficherTournee(Tournee tournee) {
    	mapContainer.getMapDisplay().afficherTournee(tournee);
    }
}



