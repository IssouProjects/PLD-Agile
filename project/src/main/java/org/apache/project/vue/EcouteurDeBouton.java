package org.apache.project.vue;

import org.apache.project.controleur.Controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class EcouteurDeBouton implements EventHandler<ActionEvent> {
	
	Controleur controleur;

	public EcouteurDeBouton(Controleur c) {
		super();
		controleur = c;
	}
	
	@Override
	public void handle(ActionEvent event) {
		Button sender = (Button)event.getSource();
		
		switch(sender.getText()) {
		case FenetrePrincipale.LOAD_MAP:
			controleur.ouvrirPlanDeVille();
			break;
		case FenetrePrincipale.LOAD_LIVRAISON:
			
			break;
		default:
			System.out.println("Unmapped Button");
		}
		System.out.println(sender.getText());
		
	}

}
