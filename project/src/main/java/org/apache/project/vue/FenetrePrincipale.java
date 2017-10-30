package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
	Button supprLivraisonButton;
	Button annulerBouton;

	ListDisplay listeLivraisons;

	StackPane stack;

	Label listLabel;
	Label mapLabel;

	// String appearing in the user interface
	public static final String LOAD_MAP = "Charger plan";
	public static final String LOAD_MAP_ID = "loadMapButton";
	public static final String LOAD_LIVRAISON = "Charger livraisons";
	public static final String LOAD_LIVRAISON_ID = "loadLivraisonButton";
	public static final String CALCULATE_TOURNEE = "Calculer tournée";
	public static final String CALCULATE_TOURNEE_ID = "calculateTourneeButton";
	public static final String ADD_LIVRAISON = "Ajouter livraison";
	public static final String ADD_LIVRAISON_ID = "addLivraisonButton";
	public static final String SUPPR_LIVRAISON = "Supprimer livraison";
	public static final String SUPPR_LIVRAISON_ID = "supprLivraisonButton";	
	public static final String ANNULER = "Annuler";
	public static final String ANNULER_ID = "AnnulerButton";;

	public static void launchApp(String[] args) {
		Application.launch(FenetrePrincipale.class, args);
	}

	@Override
	public void start(Stage stage) {

		controleur = Controleur.getInstance();
		controleur.setFenetre(this);

		stage.setTitle("SALTY DELIVERY");

		// layout for the full window
		GridPane layout = new GridPane();
		layout.setStyle("-fx-padding: 10;");
		layout.setHgap(10);

		stack = new StackPane(layout);
		Scene scene = new Scene(stack, 1024, 500);

		/////////////////////////////////////////////
		///// CREATING THE MAP AND ITS BUTTONS /////
		/////////////////////////////////////////////

		// layout for the map and its controls buttons
		VBox mapLayout = new VBox();

		HBox mapButtonsLayout = new HBox();

		// buttons
		fitMapButton = new Button("Recentrer plan");
		fitMapButton.setDisable(true);
		loadMapButton = new Button(LOAD_MAP);
		loadMapButton.setUserData(LOAD_MAP_ID);

		mapButtonsLayout.setAlignment(Pos.CENTER);
		mapButtonsLayout.setSpacing(10);

		mapButtonsLayout.getChildren().add(loadMapButton);
		mapButtonsLayout.getChildren().add(fitMapButton);

		// map
		mapLabel = new Label("Action à realiser: Charger un plan");
		layout.add(mapLabel, 0, 0);

		mapContainer = new MapContainer(2000, 2000);
		mapLayout.getChildren().add(mapContainer);
		mapLayout.getChildren().add(mapButtonsLayout);
		mapLayout.setSpacing(10d);

		layout.add(mapLayout, 0, 1);

		//////////////////////////////////////
		///// CREATING THE DELIVERY LIST /////
		//////////////////////////////////////

		listLabel = new Label("Livraisons: ");

		layout.add(listLabel, 1, 0);

		VBox listLayout = new VBox();

		HBox listeButtonsLayout1 = new HBox();
		listeButtonsLayout1.setSpacing(10);
		
		HBox listeButtonsLayout2 = new HBox();
		listeButtonsLayout2.setSpacing(10);

		// buttons
		loadLivraisonButton = new Button(LOAD_LIVRAISON);
		loadLivraisonButton.setUserData(LOAD_LIVRAISON_ID);
		loadLivraisonButton.setDisable(true);
		calculerTourneeButton = new Button(CALCULATE_TOURNEE);
		calculerTourneeButton.setUserData(CALCULATE_TOURNEE_ID);
		calculerTourneeButton.setDisable(true);
		ajouterLivraisonButton = new Button(ADD_LIVRAISON);
		ajouterLivraisonButton.setUserData(ADD_LIVRAISON_ID);
		ajouterLivraisonButton.setDisable(true);
		supprLivraisonButton = new Button(SUPPR_LIVRAISON);
		supprLivraisonButton.setUserData(SUPPR_LIVRAISON_ID);
		supprLivraisonButton.setDisable(true);
		annulerBouton = new Button(ANNULER);
		annulerBouton.setUserData(ANNULER_ID);
		annulerBouton.setDisable(true);

		listLayout.setSpacing(10);

		// list
		listeLivraisons = new ListDisplay();

		listLayout.getChildren().add(listeLivraisons);
		listeButtonsLayout1.getChildren().add(loadLivraisonButton);
		listeButtonsLayout1.getChildren().add(calculerTourneeButton);
		listeButtonsLayout2.getChildren().add(ajouterLivraisonButton);
		listeButtonsLayout2.getChildren().add(supprLivraisonButton);
		listeButtonsLayout2.getChildren().add(annulerBouton);

		listLayout.getChildren().add(listeButtonsLayout1);
		listLayout.getChildren().add(listeButtonsLayout2);
		

		layout.add(listLayout, 1, 1);

		///////////////////////////
		///// LAYOUT STYLE //////
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
		supprLivraisonButton.setOnAction(edb);
		annulerBouton.setOnAction(edb);

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

	public void afficherInfo(String message) {
		mapLabel.setText("Action à réaliser: " + message);
	}

	public void afficherPlanDeVille(PlanDeVille plan) {
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
		double duree_min = tournee.getDureeTourneeSecondes();
		listLabel.setText(
				"Durée de la tournée " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(duree_min * 1000));
		calculerTourneeButton.setDisable(true);
		ajouterLivraisonButton.setDisable(false);
		supprLivraisonButton.setDisable(false);
		annulerBouton.setDisable(false);
	}

	public void clearPlanDeVille() {
		clearLivraison();
		clearTournee();
		mapContainer.getMapDisplay().clearPlanDeVille();
	}

	public void clearLivraison() {
		mapContainer.getMapDisplay().clearDemandeDeLivraison();
		listeLivraisons.clearList();
		listLabel.setText("Livraisons:");
	}

	public void clearTournee() {
		mapContainer.getMapDisplay().clearTournee();
		listeLivraisons.clearList();
		listLabel.setText("Livraisons:");
	}

	public void afficherFenetreAjouterLivraison(Livraison l) {
		new LivraisonPopup(l, stack, edb);
	}
}
