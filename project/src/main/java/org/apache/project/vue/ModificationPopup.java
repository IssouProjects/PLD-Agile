package org.apache.project.vue;

import java.sql.Time;
import java.time.LocalTime;

import org.apache.project.modele.Livraison;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ModificationPopup extends VBox {
	
	private Button boutonAnnuler;
	private Button boutonValider;

	private Region opaqueLayer;
	private Pane parentPane;

	private Label heureDebLabel;
	private Label heureFinLabel;
	private TimeSpinner heureDebSpinner;
	private TimeSpinner heureFinSpinner;
	
	private Label invalidLabel;

	private GridPane mainLayout;

	public static final String VALIDATE = "Valider";
	public static final String VALIDATE_ID = "validerModificationLivraisonButton";

	public static final String CANCEL = "Annuler";
	public static final String CANCEL_ID = "annulerModificationLivraisonButton";
	
	public ModificationPopup(Livraison livraison, Pane parent, EcouteurDeBouton edb) {
		parentPane = parent;
		edb.setModificationPopup(this);
		this.setMaxSize(400, 300);
		this.setSpacing(40);
		this.setPadding(new Insets(20,40,20,40));
		setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
		
		Label title = new Label("Modifier livraison");
		title.setStyle("-fx-font-weight: bold; -fx-font-size: 24;");
		this.getChildren().add(title);

		StackPane.setMargin(this, new Insets(100, 100, 100, 100));
		
		mainLayout = new GridPane();
		mainLayout.setHgap(10);
		mainLayout.setVgap(10);

		mainLayout.setAlignment(Pos.CENTER_LEFT);
		
		invalidLabel = new Label("Plage horaire invalide");
		invalidLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
		invalidLabel.setVisible(false);
		mainLayout.add(invalidLabel, 1, 1);

		mainLayout.setAlignment(Pos.CENTER_LEFT);
		heureDebLabel = new Label("Heure de début:");
		heureFinLabel = new Label("Heure de fin:");
	
		if(livraison.getPlageHoraire() == null) {
			heureDebSpinner = new TimeSpinner(LocalTime.of(12,0,0));
			heureFinSpinner = new TimeSpinner(LocalTime.of(12,15,0));
		}else {
			heureDebSpinner = new TimeSpinner(livraison.getPlageHoraire().getDebut().toLocalTime());
			heureFinSpinner = new TimeSpinner(livraison.getPlageHoraire().getFin().toLocalTime());
		}
		
		mainLayout.add(heureDebLabel, 0, 2);
		mainLayout.add(heureDebSpinner, 1, 2);
		mainLayout.add(heureFinLabel, 0, 3);
		mainLayout.add(heureFinSpinner, 1, 3);

		HBox buttonLayout = new HBox();
		boutonAnnuler = new Button(CANCEL);
		boutonAnnuler.setUserData(CANCEL_ID);
		boutonValider = new Button(VALIDATE);
		boutonValider.setUserData(VALIDATE_ID);
		buttonLayout.getChildren().add(boutonAnnuler);
		buttonLayout.getChildren().add(boutonValider);
		buttonLayout.setAlignment(Pos.CENTER_RIGHT);
		buttonLayout.setSpacing(10);

		getChildren().add(mainLayout);
		getChildren().add(buttonLayout);
		

		opaqueLayer = new Region();
		opaqueLayer.setStyle("-fx-background-color: #00000088;");
		opaqueLayer.setVisible(true);

		parent.getChildren().add(opaqueLayer);
		parent.getChildren().add(this);

		setAlignment(Pos.CENTER);
		
		ColumnConstraints cc1 = new ColumnConstraints();
		cc1.setPercentWidth(50d);
		mainLayout.getColumnConstraints().add(cc1);

		boutonValider.setOnAction(edb);
		boutonAnnuler.setOnAction(edb);
	}
	
	@SuppressWarnings("deprecation")
	public Time getNewHeureDeb() {
		return new Time(heureDebSpinner.getValue().getHour(), heureDebSpinner.getValue().getMinute(), heureDebSpinner.getValue().getSecond());
	}

	@SuppressWarnings("deprecation")
	public Time getNewHeureFin() {
		return new Time(heureFinSpinner.getValue().getHour(), heureFinSpinner.getValue().getMinute(), heureFinSpinner.getValue().getSecond());
	}
	
	public boolean checkTimeOk() {
		if(heureFinSpinner.getValue().isBefore(heureDebSpinner.getValue())) {
			setInvalid(true);
			return false;
		}
			
		setInvalid(false);
		
		return true;
	}
	
	public void setInvalid(boolean invalid) {
		invalidLabel.setVisible(invalid);
		
		if(invalid) {
			heureDebSpinner.setStyle("-fx-effect: dropshadow(three-pass-box, #FF0000, 10, 0, 0, 0);");
			heureFinSpinner.setStyle("-fx-effect: dropshadow(three-pass-box, #FF0000, 10, 0, 0, 0);");
		} else {
			heureDebSpinner.setStyle("");
			heureFinSpinner.setStyle("");
		}
	}

	public void selfDestruct() {
		parentPane.getChildren().remove(this);
		parentPane.getChildren().remove(opaqueLayer);
	}

}
