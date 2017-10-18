package org.apache.project.vue;

import java.util.List;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FenetrePrincipale extends Application {

	MapContainer mapContainer;
	Controleur controleur;
	EcouteurDeBouton edb;

	Button loadMapButton;
	Button fitMapButton;
	Button calculerTourneeButton;
	Button loadLivraisonButton;

	ListView<Object> listeLivraisons;
	
	Label mainLabel;
	
	// String appearing in the user interface
	public static final String LOAD_MAP = "Charger plan";
	public static final String LOAD_LIVRAISON = "Charger livraisons";
	public static final String CALCULATE_TOURNEE = "Calculer tournee";

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
		///// CREATING THE MAP AND ITS CONTROLS /////
		/////////////////////////////////////////////

		// layout for the map and its controls buttons
		VBox mapLayout = new VBox();

		HBox mapButtonsLayout = new HBox();

		fitMapButton = new Button("Recentrer plan");
		fitMapButton.setDisable(true);
		loadMapButton = new Button(LOAD_MAP);
		loadLivraisonButton = new Button(LOAD_LIVRAISON);
		loadLivraisonButton.setDisable(true);
		calculerTourneeButton = new Button(CALCULATE_TOURNEE);
		calculerTourneeButton.setDisable(true);

		mapButtonsLayout.setAlignment(Pos.CENTER);
		mapButtonsLayout.setSpacing(10);

		mapButtonsLayout.getChildren().add(loadMapButton);
		mapButtonsLayout.getChildren().add(fitMapButton);

		mapContainer = new MapContainer(2000, 2000);
		mapLayout.getChildren().add(mapContainer);
		mapLayout.getChildren().add(mapButtonsLayout);
		mapLayout.setSpacing(10d);

		layout.add(mapLayout, 0, 1);
    
    /////////////////////////////////////////////
		///// CREATING THE DELIVERY LIST /////
		/////////////////////////////////////////////

    mainLabel = new Label("Livraisons: ");

    layout.add(mainLabel, 1, 0);
        
    VBox listLayout = new VBox();

    HBox listeButtonsLayout = new HBox();
    listeButtonsLayout.setSpacing(10);
    listeButtonsLayout.getChildren().add(calculerTourneeButton);

    listLayout.setSpacing(10);

    listeLivraisons = new ListView();

    listeLivraisons.getStylesheets().add(getClass().getResource("list.css").toExternalForm());
    listLayout.getChildren().add(listeLivraisons);
    listeButtonsLayout.getChildren().add(loadLivraisonButton);

    listLayout.getChildren().add(listeButtonsLayout);
    //liste.setMaxHeight(Double.MAX_VALUE);

    layout.add(listLayout, 1, 1);

		/////////////////////////////////////////////
		///// MAPPING BUTTONS /////
		/////////////////////////////////////////////

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
    	loadLivraisonButton.setDisable(false);
    }
    
    public void afficherDemandeDeLivraison(DemandeDeLivraison livraison) {
    	mapContainer.getMapDisplay().afficherDemandeDeLivraison(livraison);
    	loadLivraisonButton.setDisable(true);
    	calculerTourneeButton.setDisable(false);
    	afficherTexteLivraisons(livraison);
    }
    
    public void afficherTournee(Tournee tournee) {
    	mapContainer.getMapDisplay().afficherTournee(tournee);
    	calculerTourneeButton.setDisable(true);
    	afficherTexteLivraisonsOrdonnees(tournee);
    	double duree_min = tournee.getDureeTourneeSecondes()/60;
    	mainLabel.setText("Duree de la tournee " + (int)Math.ceil(duree_min)+ " minutes." );
    }
    
    private void afficherTexteLivraisons(DemandeDeLivraison demandeLivraison){
    	List<Livraison> livraisons = demandeLivraison.getListeLivraison();
    	for(Livraison livraison : livraisons ) {
    		listeLivraisons.getItems().add("Livraison: " + "\n" + livraison.toString());
    	}
    }
    
    private void afficherTexteLivraisonsOrdonnees(Tournee tournee) {
    	listeLivraisons.getItems().clear();
    	List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
    	int i = 1;
    	for(Livraison livraison : livraisons ) {
    		listeLivraisons.getItems().add("Livraison " + i + ":\n" + livraison.toString());
    		++i;
    	}
    }
}
