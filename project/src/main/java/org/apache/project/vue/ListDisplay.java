package org.apache.project.vue;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;

public class ListDisplay extends ListView<Livraison> implements Observer {
	
	private EcouteurDeListe ecouteurDeListe;

	public ListDisplay() {
		this.setCellFactory(i -> new LivraisonCell());
		getStylesheets().add(getClass().getResource("list.css").toExternalForm());
		this.getSelectionModel().selectedItemProperty().addListener(onSelected);
	}	
	
	public void afficherTexteLivraisons(DemandeDeLivraison demandeLivraison) {
		clearList();
		List<Livraison> livraisons = demandeLivraison.getListeLivraison();
		getItems().addAll(livraisons);

	}

	public void afficherTexteLivraisonsOrdonnees(Tournee tournee) {
		clearList();
		List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
		getItems().addAll(livraisons);
	}
	
	public void clearList() {
		getItems().clear();
	}
	
	public void setEcouteurDeListe(EcouteurDeListe edc) {
		ecouteurDeListe = edc;
	}
	
	private ChangeListener<Livraison> onSelected = new ChangeListener<Livraison>() {
		@Override
		public void changed(ObservableValue<? extends Livraison> observable, Livraison oldValue, Livraison newValue) {
			ecouteurDeListe.onLivraisonClicked(newValue);
		}
	};

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DemandeDeLivraison) {
			afficherTexteLivraisons((DemandeDeLivraison) o);
		} else if (o instanceof Tournee) {
			afficherTexteLivraisonsOrdonnees((Tournee) o);
		}
	}
}
