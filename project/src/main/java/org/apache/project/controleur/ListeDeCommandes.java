package org.apache.project.controleur;

import java.util.LinkedList;

public class ListeDeCommandes {
	
	private LinkedList<Commande> liste;
	private int i;

	public ListeDeCommandes() {
		liste = new LinkedList<Commande>();
	}
	
	public void ajouteCommande(Commande commande) {
		liste.add(commande);
	}
	
	public void undo() {
		if(i>=0) {
			liste.get(i--).undoCommande();
		}
	}
	
	public void redo() {
		if(i<liste.size()-1) {
			liste.get(i++).doCommande();
		}
	}
	
}
