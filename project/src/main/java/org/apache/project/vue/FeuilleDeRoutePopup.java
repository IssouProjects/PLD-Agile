package org.apache.project.vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 */
public class FeuilleDeRoutePopup extends VBox {

	private Button boutonOk;

	private TextArea textArea;

	public static final String OK = "Ok";
	public static final String OK_ID = "okFeuilleDeRouteButton";

	/**
	 * @param feuilleDeRoute
	 * @param edb
	 */
	public FeuilleDeRoutePopup(String feuilleDeRoute, EcouteurDeBouton edb) {
		this.setMaxSize(700, 500);
		this.setSpacing(40);
		this.setPadding(new Insets(20, 40, 20, 40));
		this.setAlignment(Pos.CENTER);

		setStyle(
				"-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

		Label title = new Label("Feuille de route");

		title.setStyle("-fx-font-weight: bold; -fx-font-size: 24;");
		this.getChildren().add(title);

		textArea = new TextArea();
		textArea.setEditable(false);
		VBox.setVgrow(textArea, Priority.ALWAYS);

		textArea.setText(feuilleDeRoute);

		this.getChildren().add(textArea);

		boutonOk = new Button(OK);
		boutonOk.setUserData(OK_ID);

		boutonOk.setOnAction(edb);

		this.getChildren().add(boutonOk);
	}
}
