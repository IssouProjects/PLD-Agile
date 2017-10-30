package org.apache.project.vue;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LivraisonPopup extends VBox {

	private Button boutonAnnuler;
	private Button boutonValider;

	private Region opaqueLayer;
	private Pane parentPane;

	private Spinner<Integer> dureeSpinner;
	private Spinner<Integer> heureDebSpinner;
	private Spinner<Integer> heureFinSpinner;
	private CheckBox checkBox;

	private HBox heureLayout;

	public static final String VALIDATE = "Valider";
	public static final String VALIDATE_ID = "validerAjoutLivraisonButton";

	public static final String CANCEL = "Annuler";
	public static final String CANCEL_ID = "annulerAjoutLivraisonButton";

	public LivraisonPopup(Livraison livraison, Pane parent, EcouteurDeBouton edb) {
		parentPane = parent;
		edb.setPopup(this);

		setPrefSize(300, 300);
		setStyle("-fx-background-color: #FFFFFF;");

		SpinnerValueFactory<Integer> dureeFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3600, 100);

		StackPane.setMargin(this, new Insets(100, 100, 100, 100));

		HBox dureeLayout = new HBox();
		dureeLayout.setAlignment(Pos.CENTER_LEFT);
		Label dureeLabel = new Label("temps sur place");
		dureeSpinner = new Spinner<Integer>();
		dureeSpinner.setValueFactory(dureeFactory);
		dureeLayout.getChildren().add(dureeLabel);
		dureeLayout.getChildren().add(dureeSpinner);

		checkBox = new CheckBox();
		checkBox.setText("Plage horaire");
		checkBox.setSelected(false);
		checkBox.setAlignment(Pos.CENTER_LEFT);

		SpinnerValueFactory<Integer> heureFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 12);
		SpinnerValueFactory<Integer> heureFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 12);

		heureLayout = new HBox();
		heureLayout.setAlignment(Pos.CENTER_LEFT);
		Label heureDebLabel = new Label("Heure de début:");
		heureDebSpinner = new Spinner<Integer>();
		heureDebSpinner.setValueFactory(heureFactory);
		Label heureFinLabel = new Label("Heure de fin:");
		heureFinSpinner = new Spinner<Integer>();
		heureFinSpinner.setValueFactory(heureFactory2);
		heureLayout.getChildren().add(heureDebLabel);
		heureLayout.getChildren().add(heureDebSpinner);
		heureLayout.getChildren().add(heureFinLabel);
		heureLayout.getChildren().add(heureFinSpinner);
		heureLayout.setSpacing(10);
		heureLayout.setDisable(true);

		HBox buttonLayout = new HBox();
		boutonAnnuler = new Button(CANCEL);
		boutonAnnuler.setUserData(CANCEL_ID);
		boutonValider = new Button(VALIDATE);
		boutonValider.setUserData(VALIDATE_ID);
		buttonLayout.getChildren().add(boutonAnnuler);
		buttonLayout.getChildren().add(boutonValider);
		buttonLayout.setAlignment(Pos.CENTER_RIGHT);
		buttonLayout.setSpacing(10);

		getChildren().add(dureeLayout);
		getChildren().add(checkBox);
		getChildren().add(heureLayout);
		getChildren().add(buttonLayout);
		setSpacing(10);

		opaqueLayer = new Region();
		opaqueLayer.setStyle("-fx-background-color: #00000088;");
		opaqueLayer.setVisible(true);

		parent.getChildren().add(opaqueLayer);
		parent.getChildren().add(this);

		setAlignment(Pos.CENTER);

		boutonValider.setOnAction(edb);
		boutonAnnuler.setOnAction(edb);

		checkBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				heureLayout.setDisable(!checkBox.isSelected());
			}
		});
	}

	public Integer getNewDuree() {
		return dureeSpinner.getValue();
	}

	@SuppressWarnings("deprecation")
	public Time getNewHeureDeb() {
		if (checkBox.isSelected()) {
			return new Time(heureDebSpinner.getValue(), 0, 0);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public Time getNewHeureFin() {
		if (checkBox.isSelected()) {
			return new Time(heureFinSpinner.getValue(), 0, 0);
		}
		return null;
	}

	public void selfDestruct() {
		parentPane.getChildren().remove(this);
		parentPane.getChildren().remove(opaqueLayer);
	}

}
