package org.apache.project.vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TimeoutPopup extends VBox {

	private Button boutonAnnuler;
	private Button boutonValider;
	
	private Spinner<Integer> dureeSpinner;
	
	private GridPane mainLayout;

	public static final String VALIDATE = "Valider";
	public static final String VALIDATE_ID = "validerRecalculTourneeButton";

	public static final String CANCEL = "Annuler";
	public static final String CANCEL_ID = "annulerRecalculTourneeButton";
	
	public TimeoutPopup(EcouteurDeBouton edb) {
		this.setMaxSize(400, 200);
		this.setSpacing(40);
		this.setPadding(new Insets(20,40,20,40));
		setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
		
		Label title = new Label("Nouvelle durée calcul");
		title.setStyle("-fx-font-weight: bold; -fx-font-size: 24;");
		this.getChildren().add(title);
		
		mainLayout = new GridPane();
		mainLayout.setHgap(10);
		mainLayout.setVgap(10);
		
		dureeSpinner = new Spinner<Integer>();
		dureeSpinner.setEditable(true);
		final int initialValue = 10;
		 // Value factory.
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 600, initialValue);
        dureeSpinner.setValueFactory(valueFactory);
        dureeSpinner.setValueFactory(valueFactory);
        
		TextFormatter<Integer> formatter = new TextFormatter<Integer>(valueFactory.getConverter(),
				valueFactory.getValue());
        dureeSpinner.getEditor().setTextFormatter(formatter);
        // bidi-bind the values
        valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());

		mainLayout.setAlignment(Pos.CENTER);
		Label dureeLabel = new Label("Durée en secondes");
		
		mainLayout.add(dureeLabel, 0, 0);
		mainLayout.add(dureeSpinner, 0, 1);

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

		setAlignment(Pos.CENTER);

		boutonValider.setOnAction(edb);
		boutonAnnuler.setOnAction(edb);
	}
	
	public Integer getNewDuree() {
		return dureeSpinner.getValue();
	}


}
