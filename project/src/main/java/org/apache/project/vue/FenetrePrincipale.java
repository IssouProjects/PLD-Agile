package org.apache.project.vue;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FenetrePrincipale extends Application {

	MapContainer mapContainer;
	Controleur controleur;
	EcouteurDeBouton edb;
	EcouteurDeMap edm;
	EcouteurDeListe edl;

	Button loadMapButton;
	Button fitMapButton;
	Button calculerTourneeButton;
	Button loadLivraisonButton;
	Button ajouterLivraisonButton;
	Button supprLivraisonButton;
	Button annulerBouton;
	Button undoButton;
	Button redoButton;

	ListDisplay listeLivraisons;
	
	private LivraisonPopup popup = null;
	private Region opaqueLayer;

	private StackPane stack;

	private Label listLabel;
	private Label mapLabel;

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
	public static final String ANNULER_ID = "AnnulerButton";
	public static final String EDIT_LIVRAISON_ID = "EditerLivraisonButton";;
	public static final String UNDO = "Défaire";
	public static final String UNDO_ID = "UndoButton";
	public static final String REDO = "Refaire";
	public static final String REDO_ID = "RedoButton";

	public static void launchApp(String[] args) {
		Application.launch(FenetrePrincipale.class, args);
	}

	@Override
	public void start(Stage stage) {

		controleur = Controleur.getInstance();
		controleur.setFenetre(this);

		stage.setTitle("Salty delivery");
		stage.getIcons().add(new Image(getClass().getResource("winicon.png").toExternalForm()));

		// layout for the full window
		GridPane layout = new GridPane();
		
		layout.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		
		stack = new StackPane(layout);
		Scene scene = new Scene(stack, 1024, 576);

		/////////////////////////////////////////////
		///// CREATING THE MAP AND ITS BUTTONS /////
		/////////////////////////////////////////////

		// layout for the map and its controls buttons

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
		mapLabel.getStyleClass().add("mapLabel");
		layout.add(mapLabel, 0, 0);
		GridPane.setValignment(mapLabel, VPos.BOTTOM);

		mapContainer = new MapContainer(2000, 2000);
		
		layout.add(mapContainer, 0, 1);
		layout.add(mapButtonsLayout, 0, 2);

		//////////////////////////////////////
		///// CREATING THE DELIVERY LIST /////
		//////////////////////////////////////

		GridPane undoRedoLayout = new GridPane();
		listLabel = new Label("Livraisons :");
		GridPane.setValignment(listLabel, VPos.BOTTOM);
		
		
		undoButton = new Button();
		undoButton.setTooltip(new Tooltip(UNDO));
		undoButton.setPrefSize(32d, 32d);
		undoButton.getStyleClass().add("undoButton");
		redoButton = new Button();
		redoButton.setTooltip(new Tooltip(REDO));
		redoButton.setPrefSize(32d, 32d);
		redoButton.getStyleClass().add("redoButton");
		
		undoButton.setUserData(UNDO_ID);
		redoButton.setUserData(REDO_ID);
		
		undoRedoLayout.setAlignment(Pos.CENTER_LEFT);
		undoRedoLayout.setHgap(5);
		HBox.setHgrow(listLabel, Priority.ALWAYS);
		undoRedoLayout.add(listLabel, 0, 0);
		undoRedoLayout.add(undoButton, 1, 0);
		undoRedoLayout.add(redoButton, 2 , 0);
		ColumnConstraints labelCC = new ColumnConstraints();
		labelCC.setHgrow(Priority.ALWAYS);
		undoRedoLayout.getColumnConstraints().add(labelCC);
		
		layout.add(undoRedoLayout, 1, 0);
		HBox listeButtonsLayout1 = new HBox();
		listeButtonsLayout1.setSpacing(10);

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

		// list
		listeLivraisons = new ListDisplay();

		layout.add(listeLivraisons, 1, 1);
		listeButtonsLayout1.getChildren().add(loadLivraisonButton);
		listeButtonsLayout1.getChildren().add(calculerTourneeButton);
		listeButtonsLayout1.getChildren().add(ajouterLivraisonButton);
		listeButtonsLayout1.getChildren().add(annulerBouton);
		

		layout.add(listeButtonsLayout1, 1, 2);

		///////////////////////////
		////// LAYOUT STYLE ///////
		///////////////////////////
		
		layout.setStyle("-fx-padding: 10;");
		layout.setHgap(10);
		layout.setVgap(10);

		ColumnConstraints MapCC = new ColumnConstraints();
		MapCC.setPercentWidth(50.0);
		MapCC.setHgrow(Priority.ALWAYS);
		layout.getColumnConstraints().add(MapCC);

		ColumnConstraints ListCC = new ColumnConstraints();
		ListCC.setPercentWidth(50.0);
		//ListCC.setHgrow(Priority.ALWAYS);
		layout.getColumnConstraints().add(ListCC);
		
		RowConstraints LabelRC = new RowConstraints();
		layout.getRowConstraints().add(LabelRC);
		
		RowConstraints MapListRC = new RowConstraints();
		MapListRC.setVgrow(Priority.ALWAYS);
		layout.getRowConstraints().add(LabelRC);
		

		//////////////////////////////////////////
		///// MAPPING CONTROLS AND LISTENERS /////
		//////////////////////////////////////////

		// button listener
		edb = new EcouteurDeBouton(controleur, this);

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
		undoButton.setOnAction(edb);
		redoButton.setOnAction(edb);

		// map listener
		edm = new EcouteurDeMap(controleur, mapContainer);
		mapContainer.setEcouteurDeMap(edm);
		
		// list listener
		edl = new EcouteurDeListe(controleur, listeLivraisons);
		listeLivraisons.setEcouteurDeListe(edl);
		listeLivraisons.setEcouteurDeBouton(edb);

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
		if(popup != null)
			return;
		popup = new LivraisonPopup(l, edb);
		opaqueLayer = new Region();
		opaqueLayer.setStyle("-fx-background-color: #00000088;");
		opaqueLayer.setVisible(true);

		stack.getChildren().add(opaqueLayer);
		stack.getChildren().add(popup);
	}
	
	public LivraisonPopup getFenetreAjouterLivraison(){
		return popup;
	}
	
	public void masquerFenetreAjouterLivraison() {
		stack.getChildren().remove(popup);
		stack.getChildren().remove(opaqueLayer);
		
		popup = null;
		opaqueLayer = null;
	}
	
	public void afficherFenetreModifierLivraison(Livraison l) {
		new ModificationPopup(l, stack, edb);
	}
    
    public void highlightLivraison(Livraison l) {
    	mapContainer.getMapDisplay().resetAndHighlight(l);
    	listeLivraisons.selectLivraison(l);
    }
    
    public void highlightIntersection(Intersection I) {
    	listeLivraisons.selectLivraison(null);
    	mapContainer.getMapDisplay().resetAndHighlight(I);
    }
    
    public Livraison getSelectedLivraison() {
    	return listeLivraisons.getSelectedLivraison();
    }
}
