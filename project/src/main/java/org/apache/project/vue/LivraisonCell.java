package org.apache.project.vue;

import java.util.HashMap;

import org.apache.project.modele.Entrepot;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Afficheur permettant d'afficher une livraison de la liste
 */
public class LivraisonCell extends ListCell<Livraison> {

	private static HashMap<Integer, LivraisonCell> instanceMap = new HashMap<Integer, LivraisonCell>();

	private GridPane grid = new GridPane();
	private Label icon = new Label();
	private Label titleText = new Label();
	private Label titleText2 = new Label();
	private Label titleMsg = new Label();
	private Label titleIcon = new Label();
	private Label subText = new Label();
	private Label bonusMsg = new Label();
	private Button editButton = new Button();
	private Button deleteButton = new Button();

	private final LivraisonCell thisCell = this;

	/**
	 * Crée une cellule permettant d'afficher une livraison
	 */
	public LivraisonCell() {

		instanceMap.put(this.hashCode(), this);

		grid.setHgap(10d);
		grid.setVgap(5d);

		ColumnConstraints column = new ColumnConstraints();
		column.setPrefWidth(20d);
		grid.getColumnConstraints().add(column);

		column = new ColumnConstraints();
		column.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().add(column);

		column = new ColumnConstraints();
		column.setPrefWidth(40d);
		grid.getColumnConstraints().add(column);

		HBox titleLayout = new HBox();
		titleLayout.setSpacing(10);
		titleLayout.getChildren().add(titleText);
		titleLayout.getChildren().add(titleText2);
		titleLayout.getChildren().add(titleIcon);
		titleLayout.getChildren().add(titleMsg);
		titleLayout.setAlignment(Pos.CENTER_LEFT);

		grid.add(icon, 0, 0);
		grid.add(titleLayout, 1, 0);
		grid.add(subText, 1, 1);
		grid.add(bonusMsg, 1, 2);
		grid.add(editButton, 2, 0);
		grid.add(deleteButton, 2, 1);

		titleText.getStyleClass().add("titleText");
		titleText2.getStyleClass().add("titleText");
		titleMsg.getStyleClass().add("titleMsg");
		subText.getStyleClass().add("subText");
		bonusMsg.getStyleClass().add("bonusMsg");
		editButton.getStyleClass().add("editButton");
		deleteButton.getStyleClass().add("deleteButton");

		editButton.setPrefWidth(100d);
		deleteButton.setPrefWidth(100d);

		editButton.setUserData(FenetrePrincipale.EDIT_LIVRAISON_ID);
		deleteButton.setUserData(FenetrePrincipale.SUPPR_LIVRAISON_ID);

		setEditMode(false);

		this.focusedProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				setEditMode((Boolean) observable.getValue());
			}
		});
	}

	@Override
	public void finalize() {
		instanceMap.remove(this.hashCode());
	}

	/**
	 * Permet de récupérer l'instance de LivraisonCell associée à l'ID donné en
	 * paramètre
	 * 
	 * @param classId
	 *            l'ID de la livraisonCell (hashcode)
	 * @return l'instance souhaitée ou null si l'ID ne correspond à aucune instance
	 */
	public LivraisonCell findInstance(String classId) {
		return instanceMap.get(Integer.parseInt(classId));
	}

	@Override
	public void updateItem(Livraison livraison, boolean empty) {
		super.updateItem(livraison, empty);
		if (empty) {
			clearContent();
			instanceMap.remove(this.hashCode());
		} else {
			addContent(livraison);
			instanceMap.put(this.hashCode(), this);
		}
	}

	/**
	 * Réinitialise le contenu de la cellule
	 */
	public void clearContent() {
		this.setText(null);
		titleText.setText(null);
		titleMsg.setText(null);
		subText.setText(null);
		bonusMsg.setText(null);
		setGraphic(null);
	}

	/**
	 * Désactive les boutons de suppression / de modification de livraison
	 * 
	 * @param disabled
	 */
	public void setEditDisabled(boolean disabled) {
		if (!(getItem() instanceof Entrepot)) {
			editButton.setDisable(disabled);
			deleteButton.setDisable(disabled);
		}
	}

	/**
	 * Affiche les informations relative à la livraison donnée en paramètre
	 * 
	 * @param livraison
	 *            la livraison à afficher
	 */
	@SuppressWarnings("deprecation")
	public void addContent(Livraison livraison) {
		clearContent();
		instanceMap.put(this.hashCode(), this);

		EcouteurDeBouton edb = ((ListDisplay) this.getListView().getParent()).getEcouteurDeBouton();
		deleteButton.setOnAction(edb);
		editButton.setOnAction(edb);

		titleIcon.setVisible(false);
		titleIcon.setManaged(false);
		titleMsg.setVisible(false);
		titleMsg.setManaged(false);

		if (livraison instanceof Entrepot) {
			titleText.setText("Entrepôt");
			String titleText2String = "- départ à "
					+ PlageHoraire.timeToString(((Entrepot) livraison).getHeureDepart());
			if (((Entrepot) livraison).getHeureDeFin() != null) {
				titleText2String += " - retour à " + PlageHoraire.timeToString(((Entrepot) livraison).getHeureDeFin());
			}
			titleText2.setText(titleText2String);
			icon.getStyleClass().clear();
			icon.getStyleClass().add("iconHome");
			editButton.setDisable(true);
			deleteButton.setDisable(true);
		} else if (livraison instanceof Livraison) {
			if (livraison.getPositionDansTournee() != -1)
				titleText.setText("Livraison " + livraison.getPositionDansTournee());
			else
				titleText.setText("Livraison");

			if (livraison.getHeureArrivee() != null) {
				titleText2.setText("- arrivée à " + PlageHoraire.timeToString(livraison.getHeureArrivee()));
			} else {
				titleText2.setText("");
			}

			String livraison_s = "";
			PlageHoraire plageHoraire = livraison.getPlageHoraire();
			if (plageHoraire != null) {
				livraison_s += "Plage horaire: " + PlageHoraire.timeToString(plageHoraire.getDebut()) + " - "
						+ PlageHoraire.timeToString(plageHoraire.getFin());
			} else {
				livraison_s += "Horaire libre";
			}
			livraison_s += "\n";
			livraison_s += "Durée sur place: "
					+ PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(livraison.getDuree() * 1000);
			subText.setText(livraison_s);
			editButton.setDisable(false);
			deleteButton.setDisable(false);

			// test si l'heure d'arrivée n'est pas en conflit avec la plage horaire
			if (livraison.getHeureArrivee() != null && livraison.getPlageHoraire() != null) {
				int heureArriveeAsSeconds = livraison.getHeureArrivee().getHours() * 3600
						+ livraison.getHeureArrivee().getMinutes() * 60 + livraison.getHeureArrivee().getSeconds();

				int plageHoraireDebutAsSeconds = livraison.getPlageHoraire().getDebut().getHours() * 3600
						+ livraison.getPlageHoraire().getDebut().getMinutes() * 60
						+ livraison.getPlageHoraire().getDebut().getSeconds();

				int plageHoraireFinAsSeconds = livraison.getPlageHoraire().getFin().getHours() * 3600
						+ livraison.getPlageHoraire().getFin().getMinutes() * 60
						+ livraison.getPlageHoraire().getFin().getSeconds();

				if (heureArriveeAsSeconds > plageHoraireFinAsSeconds) {
					long retard = livraison.getHeureArrivee().getTime() - plageHoraire.getFin().getTime();
					bonusMsg.setText(PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(retard) + " de retard");
					bonusMsg.getStyleClass().clear();
					bonusMsg.getStyleClass().add("bonusMsgWarning");
					icon.getStyleClass().clear();
					icon.getStyleClass().add("iconWarning");
				} else if (heureArriveeAsSeconds < plageHoraireDebutAsSeconds) {
					long avance = plageHoraire.getDebut().getTime() - livraison.getHeureArrivee().getTime();

					titleIcon.setVisible(true);
					titleIcon.setManaged(true);
					titleIcon.getStyleClass().clear();
					titleIcon.getStyleClass().add("iconTime");
					titleMsg.setVisible(true);
					titleMsg.setManaged(true);
					titleMsg.setText(PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(avance) + " d'avance");

					bonusMsg.setText(PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(avance) + " d'avance");
					bonusMsg.getStyleClass().clear();
					bonusMsg.getStyleClass().add("bonusMsgNice");
					icon.getStyleClass().clear();
					icon.getStyleClass().add("iconOk");
				} else if (heureArriveeAsSeconds + livraison.getDuree() > plageHoraireFinAsSeconds) {
					bonusMsg.setText("Pas assez de temps pour livrer.");
					bonusMsg.getStyleClass().clear();
					bonusMsg.getStyleClass().add("bonusMsgWarning");
					icon.getStyleClass().clear();
					icon.getStyleClass().add("iconWarning");
				} else {
					icon.getStyleClass().clear();
					icon.getStyleClass().add("iconOk");
				}
			} else {
				icon.getStyleClass().clear();
				icon.getStyleClass().add("iconOk");
			}
		}

		this.setGraphic(grid);
	}

	/**
	 * Permet d'afficher ou de masque les informations détaillées associée à la
	 * livraison (lorsqu'une cellule est sélectionnée)
	 * 
	 * @param editMode
	 */
	public void setEditMode(boolean editMode) {
		subText.setVisible(editMode);
		editButton.setVisible(editMode);
		deleteButton.setVisible(editMode);
		bonusMsg.setVisible(bonusMsg.getText() != null && editMode);

		subText.setManaged(editMode);
		editButton.setManaged(editMode);
		deleteButton.setManaged(editMode);
		bonusMsg.setManaged(bonusMsg.getText() != null && editMode);
	}

	/**
	 * Active l'indicateur d'ajout
	 */
	public void enableAddHint() {
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (thisCell.getItem() instanceof Livraison) {
					((ListDisplay) getListView().getParent()).placeAddHintAt(
							thisCell.getBoundsInParent().getMinY() + thisCell.getHeight(), thisCell.getWidth());
				}
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (thisCell.getItem() instanceof Livraison) {
					((ListDisplay) getListView().getParent()).hideHint();
				}
			}
		});
	}

	/**
	 * Désactive l'indicateur d'ajout
	 */
	public void disableAddHint() {
		((ListDisplay) getListView().getParent()).hideHint();
		this.setOnMouseEntered(null);
		this.setOnMouseExited(null);
	}

	/**
	 * Active l'indicateur de mouvement et autorise le drag/drop des livraisons pour
	 * gérer leur déplacement dans la tournée
	 */
	public void enableMove() {
		if (this.getItem() instanceof Entrepot) {
			disableMove();
			return;
		}

		//////////////////////////////////
		//// DRAG AND DROP MANAGEMENT ////
		//////////////////////////////////

		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (getItem() == null) {
					return;
				}
				((ListDisplay) getListView().getParent()).useMoveNotifier();

				Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(Integer.toString(this.hashCode()));

				SnapshotParameters snapshotParams = new SnapshotParameters();
				WritableImage image = thisCell.snapshot(snapshotParams, null);

				dragboard.setDragView(image, event.getX(), 0);
				dragboard.setContent(content);

				setOpacity(0.3);

				event.consume();

			}
		});

		setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (!(thisCell.getItem() instanceof Entrepot) && event.getGestureSource() != thisCell
						&& event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}

				event.consume();
			}
		});

		setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (!(thisCell.getItem() instanceof Entrepot) && event.getGestureSource() != thisCell
						&& event.getDragboard().hasString()) {
					if (event.getGestureSource() instanceof LivraisonCell) {
						LivraisonCell source = (LivraisonCell) event.getGestureSource();

						// we show a hint on hover so the user understands where the new position of his
						// livraison will be

						// if the new position of the livraison is after its current position, the hint
						// is displayed before
						// the hovered livraison. Otherwise, the hint is displayed before it (don't ask
						// why, no clue but it works)
						if (source.getItem().getPositionDansTournee() > thisCell.getItem().getPositionDansTournee()) {
							((ListDisplay) getListView().getParent())
									.placeAddHintAt(thisCell.getBoundsInParent().getMinY(), thisCell.getWidth());
						} else {
							((ListDisplay) getListView().getParent()).placeAddHintAt(
									thisCell.getBoundsInParent().getMinY() + thisCell.getHeight(), thisCell.getWidth());
						}
					}

				}
			}
		});

		setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (!(thisCell.getItem() instanceof Entrepot) && event.getGestureSource() != thisCell
						&& event.getDragboard().hasString()) {
					// hiding the hint
					((ListDisplay) getListView().getParent()).hideHint();
				}
			}
		});

		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (getItem() == null) {
					return;
				}
				if (event.getGestureSource() instanceof LivraisonCell) {
					LivraisonCell source = (LivraisonCell) event.getGestureSource();
					if (source.getItem().getPositionDansTournee() != -1
							&& thisCell.getItem().getPositionDansTournee() != -1) {
						// when the user release the mouse button, we displace the livraison
						((ListDisplay) getListView().getParent()).livraisonMoved(source.getItem(),
								thisCell.getItem().getPositionDansTournee());
					}
				}

				event.consume();
			}
		});

		setOnDragDone(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getDragboard().hasString()) {
					thisCell.setOpacity(1);
				}

				event.consume();
			}
		});
	}

	/**
	 * désactive le déplacement de la cellule
	 */
	public void disableMove() {
		setOnDragDetected(null);
		setOnDragOver(null);
		setOnDragEntered(null);
		setOnDragExited(null);
		setOnDragDropped(null);
		setOnDragDone(null);
	}

	/**
	 * Permet de récupérer la map dans laquelle on stocke toutes les instances
	 * 
	 * @return
	 */
	public static HashMap<Integer, LivraisonCell> getInstanceMap() {
		return instanceMap;
	}
}
