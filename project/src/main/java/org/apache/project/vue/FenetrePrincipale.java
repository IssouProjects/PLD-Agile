package org.apache.project.vue;

import java.io.File;

import org.apache.project.controleur.Controleur;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.modele.Troncon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;

public class FenetrePrincipale extends Application {

	private Stage stage;

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
	Button recalculerBouton;
	
	ImageView imageView;

	ListDisplay listeLivraisons;

	private UndoRedoWidget undoRedoWidget;

	private LivraisonPopup livraisonPopup = null;
	private TimeoutPopup timeoutPopup = null;
	private Region opaqueLayer;

	private StackPane stack;

	private Label listLabel;
	private Label mapLabel;

	private VBox streetDisplay;
	private Label streetLabel;

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
	public static final String UNDO_ID = "UndoButton";
	public static final String REDO_ID = "RedoButton";
	public static final String RECALCULER = "Recalculer tournée";
	public static final String RECALCULER_ID = "RecalculerTourneeButton";

	public static final String PDV_FILE_DESCRIPTION = "Fichier de plan de ville";
	public static final String PDV_FILEDIALOG_DESCRIPTION = "Ouvrir un plan de ville";
	public static final String PDV_FILE_EXTENSION = "*.xml";

	public static final String DDL_FILE_DESCRIPTION = "Fichier de demande de livraison";
	public static final String DDL_FILEDIALOG_DESCRIPTION = "Ouvrir une demande de livraison";
	public static final String DDL_FILE_EXTENSION = "*.xml";

	public static void launchApp(String[] args) {
		Application.launch(FenetrePrincipale.class, args);
	}

	@Override
	public void start(Stage stage) {

		this.stage = stage;

		stage.setTitle("Salty delivery");
		stage.getIcons().add(new Image(getClass().getResource("winicon.png").toExternalForm()));

		// layout for the full window
		GridPane layout = new GridPane();

		layout.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

		stack = new StackPane(layout);
		Scene scene = new Scene(stack, 1050, 576);

		/////////////////////////////////////////////
		///// CREATING THE MAP AND ITS BUTTONS /////
		/////////////////////////////////////////////

		// layout for the map and its controls buttons

		HBox mapButtonsLayout = new HBox();

		// displaying the street's name
		streetDisplay = new VBox();
		streetDisplay.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		streetLabel = new Label("");
		streetDisplay.getChildren().add(streetLabel);
		streetDisplay.setMouseTransparent(true);
		VBox.setMargin(streetLabel, new Insets(60, 0, 0, 20));
		streetLabel.getStyleClass().add("streetDisplay");
		stack.getChildren().add(streetDisplay);
		streetDisplay.setVisible(false);

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
		
		imageView = new ImageView();

		//////////////////////////////////////
		///// CREATING THE DELIVERY LIST /////
		//////////////////////////////////////

		GridPane undoRedoLayout = new GridPane();
		listLabel = new Label("Livraisons :");
		GridPane.setValignment(listLabel, VPos.BOTTOM);

		undoRedoWidget = new UndoRedoWidget(edb);

		undoRedoLayout.setAlignment(Pos.CENTER_LEFT);
		undoRedoLayout.setHgap(5);
		HBox.setHgrow(listLabel, Priority.ALWAYS);
		undoRedoLayout.add(listLabel, 0, 0);
		undoRedoLayout.add(undoRedoWidget, 1, 0);
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
		recalculerBouton = new Button(RECALCULER);
		recalculerBouton.setUserData(RECALCULER_ID);
		recalculerBouton.setVisible(false);

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
		// ListCC.setHgrow(Priority.ALWAYS);
		layout.getColumnConstraints().add(ListCC);

		RowConstraints LabelRC = new RowConstraints();
		layout.getRowConstraints().add(LabelRC);

		RowConstraints MapListRC = new RowConstraints();
		MapListRC.setVgrow(Priority.ALWAYS);
		layout.getRowConstraints().add(LabelRC);

		//////////////////////////////////////////
		///// MAPPING CONTROLS AND LISTENERS /////
		//////////////////////////////////////////

		controleur = Controleur.getInstance();
		controleur.setFenetre(this);

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
		undoRedoWidget.setEcouteurDeBouton(edb);
		recalculerBouton.setOnAction(edb);

		// map listener
		edm = new EcouteurDeMap(controleur, mapContainer);
		mapContainer.setEcouteurDeMap(edm);

		// list listener
		edl = new EcouteurDeListe(controleur);
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

	public void afficherPopupInfo(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Erreur");
		alert.setHeaderText("Une erreur a eu lieu");
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void afficherInfo(String message) {
		mapLabel.setText("Action à réaliser: " + message);
	}

	public void afficherPlanDeVille(PlanDeVille plan) {
		mapContainer.getMapDisplay().unhighlight();
		mapContainer.getMapDisplay().afficherPlanDeVille(plan);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mapContainer.fitMapInView();
			}
		});

		loadMapButton.setDisable(false);
		fitMapButton.setDisable(false);
		loadLivraisonButton.setDisable(false);
	}

	public void afficherDemandeDeLivraison(DemandeDeLivraison livraison) {
		mapContainer.getMapDisplay().unhighlight();
		mapContainer.getMapDisplay().afficherDemandeDeLivraison(livraison);
		listeLivraisons.afficherTexteLivraisons(livraison);

		loadLivraisonButton.setDisable(false);
		calculerTourneeButton.setDisable(false);
	}

	public void afficherTournee(Tournee tournee) {
		mapContainer.getMapDisplay().unhighlight();
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
		if (livraisonPopup != null)
			return;
		livraisonPopup = new LivraisonPopup(l, edb);
		opaqueLayer = new Region();
		opaqueLayer.setStyle("-fx-background-color: #00000088;");
		opaqueLayer.setVisible(true);

		stack.getChildren().add(opaqueLayer);
		stack.getChildren().add(livraisonPopup);
	}

	public LivraisonPopup getFenetreAjouterLivraison() {
		return livraisonPopup;
	}

	public void masquerFenetreAjouterLivraison() {
		stack.getChildren().remove(livraisonPopup);
		stack.getChildren().remove(opaqueLayer);

		livraisonPopup = null;
		opaqueLayer = null;
	}

	public void afficherFenetreTimeout() {
		if (timeoutPopup != null)
			return;
		timeoutPopup = new TimeoutPopup(edb);
		opaqueLayer = new Region();
		opaqueLayer.setStyle("-fx-background-color: #00000088;");
		opaqueLayer.setVisible(true);

		stack.getChildren().add(opaqueLayer);
		stack.getChildren().add(timeoutPopup);
	}

	public TimeoutPopup getFenetreTimeoutPopup() {
		return timeoutPopup;
	}

	public void masquerFenetreTimeoutPopup() {
		stack.getChildren().remove(timeoutPopup);
		stack.getChildren().remove(opaqueLayer);
		timeoutPopup = null;
		opaqueLayer = null;
	}

	public void afficherFenetreModifierLivraison(Livraison l) {
		new ModificationPopup(l, stack, edb);
	}

	/**
	 * Affiche une boite de dialogue pour ouvrir un fichier. Cette méthode est
	 * bloquante : on n'en sors pas tant que l'utilisateur n'a pas choisi un fichier
	 * ou annulé l'opération.
	 */

	public void highlightLivraison(Livraison l) {
		mapContainer.getMapDisplay().resetAndHighlight(l);
		streetDisplay.setVisible(false);
		listeLivraisons.selectLivraison(l);
	}

	public void highlightIntersection(Intersection I) {
		listeLivraisons.selectLivraison(null);
		mapContainer.getMapDisplay().resetAndHighlight(I);
		streetDisplay.setVisible(false);
	}

	public void highlightTroncon(Troncon t) {
		listeLivraisons.selectLivraison(null);
		mapContainer.getMapDisplay().resetAndHighlight(t);
		streetDisplay.setVisible(true);
		streetLabel.setText(t.getNomRue());
	}

	public ListDisplay getListDisplay() {
		return listeLivraisons;
	}

	public MapContainer getMapContainer() {
		return mapContainer;
	}

	public Livraison getSelectedLivraison() {
		return listeLivraisons.getSelectedLivraison();
	}

	public UndoRedoWidget getUndoRedoWidget() {
		return undoRedoWidget;
	}

	/**
	 * Affiche une boite de dialogue pour ouvrir un fichier. Cette méthode est
	 * bloquante : on n'en sors pas tant que l'utilisateur n'a pas choisi un fichier
	 * ou annulé l'opération. >>>>>>> master
	 * 
	 * @param fileDescription
	 *            Description du fichier (exemple : fichier de plan XML)
	 * @param fileExtension
	 *            Extension du fichier à ouvrir (exemple : *.xml)
	 * @param windowTitle
	 *            Titre de la fenetre (exemple : Ouvrir un fichier XML)
	 * 
	 * @return Le fichier, ou null si l'utilisateur à annulé l'opération
	 */
    public File ouvrirFichierXml(String fileDescription, String fileExtension, String windowTitle) {
    	return FileOpener.ouvrirFichier(stage, fileDescription, fileExtension, windowTitle);
    }
    
    public void setVisibleRecalculerButton(boolean visible) {
    	recalculerBouton.setVisible(visible);
    }
    
    public void afficherLoading() {
    	
    	imageView.setImage(new Image(getClass().getResource("loading.gif").toExternalForm()));
    	opaqueLayer = new Region();
 		opaqueLayer.setStyle("-fx-background-color: #00000088;");
 		opaqueLayer.setVisible(true);
 		
 		stack.getChildren().add(opaqueLayer);
    	stack.getChildren().add(imageView);
    }
    
    public void removeLoading() {
    	stack.getChildren().remove(imageView);
		stack.getChildren().remove(opaqueLayer);
		
		imageView = null;
		opaqueLayer = null;
    }
}
