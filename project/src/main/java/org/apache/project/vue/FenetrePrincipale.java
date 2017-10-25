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
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;


public class FenetrePrincipale extends Application {

	MapContainer mapContainer;
	Controleur controleur;
	EcouteurDeBouton edb;
	EcouteurDeMap edm;

	Button loadMapButton;
	Button fitMapButton;
	Button calculerTourneeButton;
	Button loadLivraisonButton;
	Button ajouterLivraisonButton;

	ListDisplay listeLivraisons;
	
	Label mainLabel;
	
	// String appearing in the user interface
	public static final String LOAD_MAP = "Charger plan";
	public static final String LOAD_LIVRAISON = "Charger livraisons";
	public static final String CALCULATE_TOURNEE = "Calculer tournée";
	public static final String ADD_LIVRAISON = "Ajouter livraison";

	public static void launchApp(String[] args) {
		Application.launch(FenetrePrincipale.class, args);
	}
  
	  @Override
	  public void start(Stage stage) {
	
	    controleur = controleur.getInstance();
	    controleur.setFenetre(this);
	    
	    stage.setTitle("SALTY DELIVERY");
	
	    // layout for the full window
	    GridPane layout = new GridPane();
	    layout.setStyle("-fx-padding: 10;");
	    layout.setHgap(10);
	    
	    
	
	    Scene scene = new Scene(layout, 1024, 500);
	    
		/////////////////////////////////////////////
		///// CREATING THE MAP AND ITS BUTTONS  /////
		/////////////////////////////////////////////
	
		// layout for the map and its controls buttons
		VBox mapLayout = new VBox();
	
		HBox mapButtonsLayout = new HBox();
	
		// buttons
		fitMapButton = new Button("Recentrer plan");
		fitMapButton.setDisable(true);
		loadMapButton = new Button(LOAD_MAP);
	
		mapButtonsLayout.setAlignment(Pos.CENTER);
		mapButtonsLayout.setSpacing(10);
	
		mapButtonsLayout.getChildren().add(loadMapButton);
		mapButtonsLayout.getChildren().add(fitMapButton);
		
		// map
	
		mapContainer = new MapContainer(2000, 2000);
		mapLayout.getChildren().add(mapContainer);
		mapLayout.getChildren().add(mapButtonsLayout);
		mapLayout.setSpacing(10d);
	
		layout.add(mapLayout, 0, 1);
	    
	    //////////////////////////////////////
		///// CREATING THE DELIVERY LIST /////
		//////////////////////////////////////
	
	    mainLabel = new Label("Livraisons: ");
	
	    layout.add(mainLabel, 1, 0);
	        
	    VBox listLayout = new VBox();
	
	    HBox listeButtonsLayout = new HBox();
	    listeButtonsLayout.setSpacing(10);
	    
	    // buttons
	    loadLivraisonButton = new Button(LOAD_LIVRAISON);
		loadLivraisonButton.setDisable(true);
		calculerTourneeButton = new Button(CALCULATE_TOURNEE);
		calculerTourneeButton.setDisable(true);
	    ajouterLivraisonButton = new Button(ADD_LIVRAISON);
	    ajouterLivraisonButton.setDisable(true);
	
	    listLayout.setSpacing(10);
	    
	    // list
	    listeLivraisons = new ListDisplay();

	    listLayout.getChildren().add(listeLivraisons);
	    listeButtonsLayout.getChildren().add(loadLivraisonButton);
	    listeButtonsLayout.getChildren().add(calculerTourneeButton);
	    listeButtonsLayout.getChildren().add(ajouterLivraisonButton);
	
	    listLayout.getChildren().add(listeButtonsLayout);
	
	    layout.add(listLayout, 1, 1);
	    
	    
		///////////////////////////
		/////  LAYOUT STYLE  //////
		///////////////////////////
	    
	    
 		ColumnConstraints MapCC = new ColumnConstraints();
 		MapCC.setPercentWidth(67.0);
 		MapCC.setHgrow(Priority.ALWAYS);
 		layout.getColumnConstraints().add(MapCC);

 		ColumnConstraints ListCC = new ColumnConstraints();
 		ListCC.setPercentWidth(33.0);
 		ListCC.setHgrow(Priority.ALWAYS);
 		layout.getColumnConstraints().add(ListCC);

		//////////////////////////////////////////
		///// MAPPING CONTROLS AND LISTENERS /////
		//////////////////////////////////////////

 		// button listener
 		
		edb = new EcouteurDeBouton(controleur);

		fitMapButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mapContainer.fitMapInView();
			}
		});

		loadMapButton.setOnAction(edb);
		loadLivraisonButton.setOnAction(edb);
		calculerTourneeButton.setOnAction(edb);
		ajouterLivraisonButton.setOnAction(edb);
		
		// map listener
		
		edm = new EcouteurDeMap(controleur);
		mapContainer.setEcouteurDeMap(edm);

		// we can now show the window
		stage.setScene(scene);
		stage.show();
	}
	  
	public void afficherPopupError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Erreur");
	    alert.setHeaderText("Une erreur a eu lieu");
	    alert.setContentText(message);

	    alert.showAndWait();
	}
	
	public void afficherPopupInfo(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Information");
	    alert.setHeaderText("Voici l'action à faire");
	    alert.setContentText(message);

	    alert.showAndWait();
	}
    
    public void afficherPlanDeVille(PlanDeVille plan){
    	mapContainer.getMapDisplay().afficherPlanDeVille(plan);
    	mapContainer.fitMapInView();
    	
    	loadMapButton.setDisable(false);
    	fitMapButton.setDisable(false);
    	loadLivraisonButton.setDisable(false);
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison livraison) {
    	mapContainer.getMapDisplay().afficherDemandeDeLivraison(livraison);
    	listeLivraisons.afficherTexteLivraisons(livraison);
    	
    	loadLivraisonButton.setDisable(false);
    	calculerTourneeButton.setDisable(false);
    }
    
    public void afficherTournee(Tournee tournee) {
    	mapContainer.getMapDisplay().afficherTournee(tournee);
    	listeLivraisons.afficherTexteLivraisonsOrdonnees(tournee);
    	double duree_min = tournee.getDureeTourneeSecondes()/60;
    	mainLabel.setText("Duree de la tournee " + (int)Math.ceil(duree_min)+ " minutes." );
    	calculerTourneeButton.setDisable(true);
    	ajouterLivraisonButton.setDisable(false);
    }
    
    public void clearPlanDeVille() {
    	clearLivraison();
    	clearTournee();
    	mapContainer.getMapDisplay().clearPlanDeVille();
    }
    
    public void clearLivraison() {
    	mapContainer.getMapDisplay().clearDemandeDeLivraison();
    }
    
    public void clearTournee() {
    	mapContainer.getMapDisplay().clearTournee();
    }
}
