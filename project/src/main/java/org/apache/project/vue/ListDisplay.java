package org.apache.project.vue;

import java.sql.Time;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.Tournee;

import javafx.scene.control.ListView;

public class ListDisplay extends ListView<String> implements Observer {

	public ListDisplay() {
		getStylesheets().add(getClass().getResource("list.css").toExternalForm());
	}

	public void afficherTexteLivraisons(DemandeDeLivraison demandeLivraison) {
		List<Livraison> livraisons = demandeLivraison.getListeLivraison();
		Time heureDepart = demandeLivraison.getHeureDepart();
		getItems().add("Entrepôt - départ à " + PlageHoraire.formatTime(heureDepart));
		int i = 0;
		for (Livraison livraison : livraisons) {
			if(i!=0)
				getItems().add("Livraison: " + "\n" + livraison.toString());
			++i;
		}
	}

	public void afficherTexteLivraisonsOrdonnees(Tournee tournee) {
		getItems().clear();
		List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
		Time heureDepart = tournee.getHeureDepart();
		getItems().add("Entrepôt - départ à " + PlageHoraire.formatTime(heureDepart));
		int i = 1;
		for (Livraison livraison : livraisons) {
			if(i!=0)
				getItems().add("Livraison " + i + ":\n" + livraison.toString());
			++i;
		}
	}
	
	public void clearList() {
		getItems().clear();
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
