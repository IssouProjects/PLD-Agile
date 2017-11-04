package org.apache.project.vue;

import java.util.Observable;
import java.util.Observer;

import org.apache.project.controleur.CdeAjouterLivraison;
import org.apache.project.controleur.CdeSupprimerLivraison;
import org.apache.project.controleur.Commande;
import org.apache.project.controleur.ListeDeCommandes;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class UndoRedoWidget extends HBox implements Observer{
	
	private Button undoButton;
	private Button redoButton;
	
	public UndoRedoWidget(EcouteurDeBouton edb) {
		undoButton = new Button();
		undoButton.setTooltip(new Tooltip("Rien à annuler"));
		undoButton.setPrefSize(32d, 32d);
		undoButton.getStyleClass().add("undoButton");
		redoButton = new Button();
		redoButton.setTooltip(new Tooltip("Rien à rétablir"));
		redoButton.setPrefSize(32d, 32d);
		redoButton.getStyleClass().add("redoButton");
		
		undoButton.setUserData(FenetrePrincipale.UNDO_ID);
		redoButton.setUserData(FenetrePrincipale.REDO_ID);
		
		this.getChildren().add(undoButton);
		this.getChildren().add(redoButton);
		
		undoButton.setDisable(true);
		redoButton.setDisable(true);
		
		this.setSpacing(5d);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ListeDeCommandes) {
			ListeDeCommandes liste = (ListeDeCommandes)o;
			
			//gestion du undo
			Commande cmd = liste.getFirstUndoableCommand();
			
			if(cmd == null) {
				undoButton.setDisable(true);
				undoButton.setTooltip(new Tooltip("Rien à annuler"));
			}else if(cmd instanceof CdeAjouterLivraison) {
				undoButton.setDisable(false);
				undoButton.setTooltip(new Tooltip("Annuler Ajouter livraison"));
			}else if(cmd instanceof CdeSupprimerLivraison) {
				undoButton.setDisable(false);
				undoButton.setTooltip(new Tooltip("Annuler Supprimer livraison"));
			}
			
			//gestion du redo
			cmd = liste.getFirstRedoableCommand();
			if(cmd == null) {
				redoButton.setDisable(true);
				redoButton.setTooltip(new Tooltip("Rien à rétablir"));
			}else if(cmd instanceof CdeAjouterLivraison) {
				redoButton.setDisable(false);
				redoButton.setTooltip(new Tooltip("Rétablir Ajouter livraison"));
			}else if(cmd instanceof CdeSupprimerLivraison) {
				redoButton.setDisable(false);
				redoButton.setTooltip(new Tooltip("Rétablir Supprimer livraison"));
			}
		}
	}
	
	public void setEcouteurDeBouton(EcouteurDeBouton edb) {
		undoButton.setOnAction(edb);
		redoButton.setOnAction(edb);
	}

}
