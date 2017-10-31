package org.apache.project.vue;

import org.apache.project.modele.Entrepot;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class LivraisonCell extends ListCell<Livraison>{
	private GridPane grid = new GridPane();
	private Label icon = new Label();
	private Label titleText = new Label();
	private Label subText = new Label();
	private Button editButton = new Button();
	private Button deleteButton = new Button();
	
	public LivraisonCell() {
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
	    
		grid.add(icon, 0, 0);                    
        grid.add(titleText, 1, 0);        
        grid.add(subText, 1, 1);
        grid.add(editButton, 2, 0);
        grid.add(deleteButton, 2, 1);
        
        titleText.getStyleClass().add("titleText");
        subText.getStyleClass().add("subText");
        editButton.getStyleClass().add("editButton");
        deleteButton.getStyleClass().add("deleteButton");
        	
		editButton.setPrefWidth(100d);
		deleteButton.setPrefWidth(100d);
		
		setEditMode(false);
		
		this.focusedProperty().addListener(new ChangeListener<Object>(){
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				setEditMode((Boolean)observable.getValue());
			}
	      });
	}
	
	@Override 
	public void updateItem(Livraison livraison, boolean empty) {
        super.updateItem(livraison, empty);
        if (empty) {
            clearContent();
        } else {
            addContent(livraison);
        }
    }
	
	public void clearContent() {
		this.setText(null);
		titleText.setText(null);
		subText.setText(null);
		setGraphic(null);
	}
	
	public void addContent(Livraison livraison) {
		setText(null);
		
		if(livraison instanceof Entrepot) {
			titleText.setText("Entrepôt - départ à " + PlageHoraire.timeToString(((Entrepot) livraison).getHeureDepart()));
			subText.setText("départ à " + PlageHoraire.timeToString(((Entrepot) livraison).getHeureDepart()));
			icon.getStyleClass().clear();
			icon.getStyleClass().add("iconHome");
			editButton.setDisable(true);
			deleteButton.setDisable(true);
		}
		else if (livraison instanceof Livraison) {
			
			if(livraison.getHeureArrivee() != null) {
				titleText.setText("Livraison - passage à " + PlageHoraire.timeToString(livraison.getHeureArrivee()));
			}
			else {
				titleText.setText("Livraison");
			}
			icon.getStyleClass().clear();
			icon.getStyleClass().add("iconOk");
			
			String livraison_s = "";
			PlageHoraire plageHoraire = livraison.getPlageHoraire();
			if (plageHoraire != null) {
				livraison_s += "Plage horaire: " + PlageHoraire.timeToString(plageHoraire.getDebut()) + " - "
						+ PlageHoraire.timeToString(plageHoraire.getFin());
				if (livraison.getHeureArrivee() != null) {
					long avance = plageHoraire.getDebut().getTime() - livraison.getHeureArrivee().getTime();
					if (avance > 0) {
						livraison_s += "\n" + "Avance: " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(avance);
					}
				}
			} else {
				livraison_s += "Horaire libre";
			}
			livraison_s += "\n";
			livraison_s += "Duree sur place: " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(livraison.getDuree() * 1000);
			subText.setText(livraison_s);
		}
        
		setGraphic(grid);        
}
	
	public void setEditMode(boolean editMode) {
		subText.setVisible(editMode);
		editButton.setVisible(editMode);
		deleteButton.setVisible(editMode);
		
		subText.setManaged(editMode);
		editButton.setManaged(editMode);
		deleteButton.setManaged(editMode);
	}
}
