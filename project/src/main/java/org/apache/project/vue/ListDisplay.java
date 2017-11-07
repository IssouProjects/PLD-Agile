package org.apache.project.vue;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

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

public class ListDisplay extends Pane implements Observer {

	private EcouteurDeListe ecouteurDeListe;
	private EcouteurDeBouton ecouteurDeBouton;
	private ListView<Livraison> liste = new ListView<Livraison>();

	private GridPane addNotifier = new GridPane();
	Pane notifierCircle = new Pane();

	private List<Livraison> livraisonsTmp = null;

	private long lastAutoscrollTime = 0l;

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

	public void afficherTexteLivraisons(DemandeDeLivraison demandeLivraison) {
		clearList();
		List<Livraison> livraisons = demandeLivraison.getListeLivraison();
		liste.getItems().addAll(livraisons);
	}

	public void afficherTexteLivraisonsOrdonnees(Tournee tournee) {
		clearList();
		livraisonsTmp = tournee.getLivraisonsOrdonnees();
		for (int i = 0; i < livraisonsTmp.size() - 1; i++) {
			liste.getItems().add(livraisonsTmp.get(i));
		}
	}

	public void clearList() {
		liste.getItems().clear();
		livraisonsTmp = null;
	}

	public void placeAddHintAt(double yPosition, double width) {
		addNotifier.setVisible(true);
		this.positionInArea(addNotifier, -24, yPosition + 4 - addNotifier.getHeight() / 2, this.getWidth(),
				addNotifier.getHeight(), 0, HPos.CENTER, VPos.CENTER);
	}

	public void hideHint() {
		addNotifier.setVisible(false);
	}

	public void enableAddHint() {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		this.useAddNotifier();
		for (LivraisonCell lc : map.values()) {
			lc.enableAddHint();
		}
	}

	public void disableAddHint() {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		for (LivraisonCell lc : map.values()) {
			lc.disableAddHint();
		}
	}

	public void enableMoveLivraison() {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		this.useAddNotifier();
		for (LivraisonCell lc : map.values()) {
			lc.enableMove();
		}
	}

	public void disableMoveLivraison() {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		this.useAddNotifier();
		for (LivraisonCell lc : map.values()) {
			lc.disableMove();
		}
	}
	
	public void disableEdit(boolean disable) {
		HashMap<Integer, LivraisonCell> map = LivraisonCell.getInstanceMap();
		this.useAddNotifier();
		for (LivraisonCell lc : map.values()) {
			lc.setEditDisabled(disable);
		}
	}

	public void livraisonMoved(Livraison livraisonMoved, int newIndex) {
		ecouteurDeListe.onMoveLivraison(livraisonMoved, newIndex);
	}

	public void setEcouteurDeListe(EcouteurDeListe edc) {
		ecouteurDeListe = edc;
	}

	public void setEcouteurDeBouton(EcouteurDeBouton edb) {
		ecouteurDeBouton = edb;
	}

	public void selectLivraison(Livraison livraison) {
		liste.getSelectionModel().select(livraison);
	}

	public Livraison getSelectedLivraison() {
		return liste.getSelectionModel().getSelectedItem();
	}

	private ChangeListener<Livraison> onSelected = new ChangeListener<Livraison>() {
		@Override
		public void changed(ObservableValue<? extends Livraison> observable, Livraison oldValue, Livraison newValue) {
			ecouteurDeListe.onLivraisonClicked(newValue);
		}
	};

	public EcouteurDeBouton getEcouteurDeBouton() {
		return ecouteurDeBouton;
	}

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

	public void useAddNotifier() {
		notifierCircle.getStyleClass().clear();
		notifierCircle.getStyleClass().add("circlePlus");
	}

	public void useMoveNotifier() {
		notifierCircle.getStyleClass().clear();
		notifierCircle.getStyleClass().add("circleMove");
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DemandeDeLivraison) {
			afficherTexteLivraisons((DemandeDeLivraison) o);
		} else if (o instanceof Tournee) {
			afficherTexteLivraisonsOrdonnees((Tournee) o);
		}
	}
}
