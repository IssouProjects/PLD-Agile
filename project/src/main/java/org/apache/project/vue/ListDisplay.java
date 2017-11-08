package org.apache.project.vue;

import java.util.HashMap;
import java.util.List;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 * 
 */
public class ListDisplay extends Pane {

	private EcouteurDeListe ecouteurDeListe;
	private EcouteurDeBouton ecouteurDeBouton;
	private ListView<Livraison> liste = new ListView<Livraison>();
	private boolean disableEditState = false;
	private boolean enableMoveState = false;

	private GridPane addNotifier = new GridPane();
	private Pane notifierCircle = new Pane();

	private List<Livraison> livraisonsTmp = null;

	private long lastAutoscrollTime = 0l;

	private ChangeListener<Livraison> onSelected = new ChangeListener<Livraison>() {
		@Override
		public void changed(ObservableValue<? extends Livraison> observable, Livraison oldValue, Livraison newValue) {
			ecouteurDeListe.onLivraisonClicked(newValue);
		}
	};

	/**
	 * Crée une liste d'affichage de livraison vide
	 */
	public ListDisplay() {

		this.getChildren().add(liste);

		// using custom class LivraisonCell to display cells
		liste.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>() {
			@Override
			public ListCell<Livraison> call(ListView<Livraison> param) {
				return new LivraisonCell();
			}
		});

		liste.getStylesheets().add(getClass().getResource("list.css").toExternalForm());
		liste.getSelectionModel().selectedItemProperty().addListener(onSelected);
		liste.prefWidthProperty().bind(this.widthProperty());
		liste.prefHeightProperty().bind(this.heightProperty());

		// auto scroll on drag management
		liste.addEventFilter(DragEvent.DRAG_OVER, new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				ScrollBar verticalScrollBar = (ScrollBar) liste.lookup(".scroll-bar:vertical");
				double proximity = 100;
				Bounds tableBounds = liste.getLayoutBounds();
				double dragY = event.getY();
				double topYProximity = tableBounds.getMinY() + proximity;
				double bottomYProximity = tableBounds.getMaxY() - proximity;

				long currentTime = System.currentTimeMillis();

				if (currentTime - lastAutoscrollTime > 50) {

					lastAutoscrollTime = currentTime;

					if (dragY < topYProximity) {
						if (verticalScrollBar != null) {
							verticalScrollBar.decrement();
						}

					} else if (dragY > bottomYProximity) {
						if (verticalScrollBar != null) {
							verticalScrollBar.increment();
						}
					}
				}

			}

		});

		createNotifier();
	}

	/**
	 * Affiche dans la liste une demande de livraison
	 * 
	 * @param demandeLivraison
	 *            la demande de livraison à afficher
	 */
	public void afficherTexteLivraisons(DemandeDeLivraison demandeLivraison) {
		clearList();
		List<Livraison> livraisons = demandeLivraison.getListeLivraison();
		liste.getItems().addAll(livraisons);
		this.disableEdit(disableEditState);
		if(enableMoveState) {
			this.enableMoveLivraison();
		} else {
			this.disableMoveLivraison();
		}
	}

	/**
	 * Affiche dans la liste une liste de livraison composant une tournée
	 * 
	 * @param tournee
	 *            la tournée à afficher
	 */
	public void afficherTexteLivraisonsOrdonnees(Tournee tournee) {
		clearList();
		livraisonsTmp = tournee.getLivraisonsOrdonnees();
		for (int i = 0; i < livraisonsTmp.size() - 1; i++) {
			liste.getItems().add(livraisonsTmp.get(i));
		}
		this.disableEdit(disableEditState);
		if(enableMoveState) {
			this.enableMoveLivraison();
		} else {
			this.disableMoveLivraison();
		}
	}

	/**
	 * Réinitialise l'affichage
	 */
	public void clearList() {
		liste.getItems().clear();
		livraisonsTmp = null;
	}

	/**
	 * Permet d'afficher une aide graphique indiquant à l'utilisateur où ajouter /
	 * déplacer une livraison
	 * 
	 * @param yPosition
	 *            position Y de l'indicateur souhaitée
	 * @param width
	 *            la largeur de l'indicateur souhaitée
	 * 
	 */
	public void placeAddHintAt(double yPosition, double width) {
		addNotifier.setVisible(true);
		this.positionInArea(addNotifier, -24, yPosition + 4 - addNotifier.getHeight() / 2, this.getWidth(),
				addNotifier.getHeight(), 0, HPos.CENTER, VPos.CENTER);
	}

	/**
	 * Masque l'indicateur
	 */
	public void hideHint() {
		addNotifier.setVisible(false);
	}

	/**
	 * Permet d'activer l'indicateur graphique d'ajout
	 */
	public void enableAddHint() {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		this.useAddNotifier();
		for (LivraisonCell lc : map.values()) {
			lc.enableAddHint();
		}
	}

	/**
	 * Permet de désactiver l'indicateur graphique d'ajout
	 */
	public void disableAddHint() {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		for (LivraisonCell lc : map.values()) {
			lc.disableAddHint();
		}
	}

	/**
	 * Permet d'activer le déplacement de livraison par drag and drop
	 */
	public void enableMoveLivraison() {
		enableMoveState = true;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
				useAddNotifier();
				for (LivraisonCell lc : map.values()) {
					lc.enableMove();
				}
			}
		});
	}

	/**
	 * Permet de désactiver le déplacement de livraison par drag and drop
	 */
	public void disableMoveLivraison() {
		enableMoveState = false;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
				useAddNotifier();
				for (LivraisonCell lc : map.values()) {
					lc.disableMove();
				}
			}
		});
	}

	/**
	 * Permet d'activer les boutons de modification des livraisons
	 * 
	 * @param disable
	 *            l'état souhaité
	 */
	public void disableEdit(boolean disable) {
		disableEditState = disable;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
				useAddNotifier();
				for (LivraisonCell lc : map.values()) {
					lc.setEditDisabled(disableEditState);
				}
			}
		});
	}

	/**
	 * méthode qui permet de transmettre à l'écouteur de liste l'information de
	 * déplacement de livraison
	 * 
	 * @param livraisonMoved
	 *            la livraison ayant été déplacée
	 * @param newIndex
	 *            la nouvelle position de la livraison
	 */
	public void livraisonMoved(Livraison livraisonMoved, int newIndex) {
		ecouteurDeListe.onMoveLivraison(livraisonMoved, newIndex);
	}

	/**
	 * @param edc
	 */
	public void setEcouteurDeListe(EcouteurDeListe edc) {
		ecouteurDeListe = edc;
	}

	/**
	 * @param edb
	 */
	public void setEcouteurDeBouton(EcouteurDeBouton edb) {
		ecouteurDeBouton = edb;
	}

	/**
	 * permet de sélectionner (de mettre en valeur) la livraison donnée en paramètre
	 * 
	 * @param livraison
	 */
	public void selectLivraison(Livraison livraison) {
		liste.getSelectionModel().select(livraison);
	}

	/**
	 * permet d'obtenir la livraison actuellement sélectionnée
	 * 
	 * @return
	 */
	public Livraison getSelectedLivraison() {
		return liste.getSelectionModel().getSelectedItem();
	}

	/**
	 * @return
	 */
	public EcouteurDeBouton getEcouteurDeBouton() {
		return ecouteurDeBouton;
	}

	/**
	 * permet de créer l'indicateur graphique d'ajout / de déplacement de livraison
	 */
	private void createNotifier() {
		this.getChildren().add(addNotifier);
		addNotifier.setVisible(false);

		notifierCircle.getStyleClass().add("circleAdd");
		addNotifier.mouseTransparentProperty().set(true);
		addNotifier.setPadding(new Insets(0, 0, 0, 10));
		addNotifier.add(notifierCircle, 0, 0);

		addNotifier.getStylesheets().add(getClass().getResource("list.css").toExternalForm());
		addNotifier.getStyleClass().add("addNotifier");
		addNotifier.prefWidthProperty().bind(this.widthProperty());
	}

	/**
	 * Permet de changer l'apparence de l'indicateur pour indiquer un ajout de
	 * livraison
	 */
	public void useAddNotifier() {
		notifierCircle.getStyleClass().clear();
		notifierCircle.getStyleClass().add("circlePlus");
	}

	/**
	 * Permet de changer l'apparence de l'indicateur pour indiquer un déplacement de
	 * livraison
	 */
	public void useMoveNotifier() {
		notifierCircle.getStyleClass().clear();
		notifierCircle.getStyleClass().add("circleMove");
	}
}
