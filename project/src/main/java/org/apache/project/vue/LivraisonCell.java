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
	private Label userMsg = new Label();
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
        grid.add(userMsg, 1, 2);
        grid.add(editButton, 3, 0);
        grid.add(deleteButton, 3, 1);
        
        titleText.getStyleClass().add("titleText");
        subText.getStyleClass().add("subText");
        userMsg.getStyleClass().add("userMsg");
        editButton.getStyleClass().add("editButton");
        deleteButton.getStyleClass().add("deleteButton");
        
        	
		editButton.setPrefWidth(100d);
		deleteButton.setPrefWidth(100d);
		
		editButton.setUserData(FenetrePrincipale.EDIT_LIVRAISON_ID);
		deleteButton.setUserData(FenetrePrincipale.SUPPR_LIVRAISON_ID);
		
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
		EcouteurDeBouton edb = ((ListDisplay) this.getListView()).getEcouteurDeBouton();
		deleteButton.setOnAction(edb);
		editButton.setOnAction(edb);
		
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
			
			String livraison_s = "";
			PlageHoraire plageHoraire = livraison.getPlageHoraire();
			if (plageHoraire != null) {
				livraison_s += "Plage horaire: " + PlageHoraire.timeToString(plageHoraire.getDebut()) + " - "
						+ PlageHoraire.timeToString(plageHoraire.getFin());
				if (livraison.getHeureArrivee() != null) {
					long avance = plageHoraire.getDebut().getTime() - livraison.getHeureArrivee().getTime();
					if (avance > 0) {
						userMsg.setText("Horaire invalide : " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(avance) + " d'avance");
					}
				}
			} else {
				livraison_s += "Horaire libre";
			}
			livraison_s += "\n";
			livraison_s += "Duree sur place: " + PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(livraison.getDuree() * 1000);
			subText.setText(livraison_s);
			editButton.setDisable(false);
			deleteButton.setDisable(false);
			
			// test si l'heure d'arrivée n'est pas en conflit avec la plage horaire
			if(livraison.getHeureArrivee() != null && livraison.getPlageHoraire() != null) {
				int heureArriveeAsSeconds = livraison.getHeureArrivee().getHours()*3600
						+livraison.getHeureArrivee().getMinutes()*60
						+livraison.getHeureArrivee().getSeconds();
				
				int plageHoraireDebutAsSeconds = livraison.getPlageHoraire().getDebut().getHours()*3600
						+livraison.getPlageHoraire().getDebut().getMinutes()*60
						+livraison.getPlageHoraire().getDebut().getSeconds();
				
				int plageHoraireFinAsSeconds = livraison.getPlageHoraire().getFin().getHours()*3600
						+livraison.getPlageHoraire().getFin().getMinutes()*60
						+livraison.getPlageHoraire().getFin().getSeconds();
				
				if(heureArriveeAsSeconds < plageHoraireDebutAsSeconds || heureArriveeAsSeconds > plageHoraireFinAsSeconds) {
					icon.getStyleClass().clear();
					icon.getStyleClass().add("iconWarning");
				}else {
					icon.getStyleClass().clear();
					icon.getStyleClass().add("iconOk");
				}
			} else {
				icon.getStyleClass().clear();
				icon.getStyleClass().add("iconOk");
			}
		}
        
		setGraphic(grid);        
}
	
	public void setEditMode(boolean editMode) {
		subText.setVisible(editMode);
		editButton.setVisible(editMode);
		deleteButton.setVisible(editMode);
		userMsg.setVisible(editMode);
		
		subText.setManaged(editMode);
		editButton.setManaged(editMode);
		deleteButton.setManaged(editMode);
		userMsg.setManaged(editMode);
	}
}
