package org.apache.project.vue;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Entrepot;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ListDisplay extends ListView<Livraison> implements Observer {
	
	private EcouteurDeListe ecouteurDeListe;
	private EcouteurDeBouton ecouteurDeBouton;

	public ListDisplay() {
		this.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>(){
			@Override
			public ListCell<Livraison> call(ListView<Livraison> param) {
				return new LivraisonCell();
			}
		});
		
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
		for(int i =0; i<livraisons.size()-1; i++) {
			getItems().add(livraisons.get(i));
		}
	}

	public void clearList() {
		getItems().clear();
	}
	
	public void setEcouteurDeListe(EcouteurDeListe edc) {
		ecouteurDeListe = edc;
	}
	
	public void setEcouteurDeBouton(EcouteurDeBouton edb) {
		ecouteurDeBouton = edb;
	}
	
	public void selectLivraison(Livraison livraison) {
    	this.getSelectionModel().select(livraison);
	}
	
	public Livraison getSelectedLivraison() {
    	return this.getSelectionModel().getSelectedItem();
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

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DemandeDeLivraison) {
			afficherTexteLivraisons((DemandeDeLivraison) o);
		} else if (o instanceof Tournee) {
			afficherTexteLivraisonsOrdonnees((Tournee) o);
		}
	}
}
