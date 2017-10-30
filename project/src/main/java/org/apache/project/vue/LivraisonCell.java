package org.apache.project.vue;

import org.apache.project.modele.Livraison;

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
        
        icon.getStyleClass().add("icon");
        
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
	}
	
	public void addContent(Livraison livraison) {
		setText(null);
        titleText.setText("Livraison");
        subText.setText(livraison.toString());
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
