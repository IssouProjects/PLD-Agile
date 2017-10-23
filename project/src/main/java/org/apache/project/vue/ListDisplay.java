package org.apache.project.vue;

import java.sql.Time;
import java.util.List;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;

import javafx.scene.control.ListView;

public class ListDisplay extends ListView<String>{
	
	public ListDisplay() {
		getStylesheets().add(getClass().getResource("list.css").toExternalForm());
	}
	
	public void afficherTexteLivraisons(DemandeDeLivraison demandeLivraison){
    	List<Livraison> livraisons = demandeLivraison.getListeLivraison();
    	Time heureDepart = 	demandeLivraison.getHeureDepart();
    	getItems().add("Entrepôt - départ à " + heureDepart);
    	for(Livraison livraison : livraisons ) {
    		getItems().add("Livraison: " + "\n" + livraison.toString());
    	}
    }
    
    public void afficherTexteLivraisonsOrdonnees(Tournee tournee) {
    	getItems().clear();
    	List<Livraison> livraisons = tournee.getLivraisonsOrdonnees();
    	Time heureDepart = 	tournee.getHeureDepart();
    	getItems().add("Entrepôt - départ à " + heureDepart);
    	int i = 1;
    	for(Livraison livraison : livraisons ) {
    		getItems().add("Livraison " + i + ":\n" + livraison.toString());
    		++i;
    	}
    }
}
