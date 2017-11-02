package org.apache.project.controleur;

import java.util.LinkedList;

public class ListeDeCommandes {
	
	private LinkedList<Commande> liste;
	private int i;

	public ListeDeCommandes() {
		liste = new LinkedList<Commande>();
		i=-1;
	}
	
	public void ajouteCommande(Commande commande) {
		liste.add(commande);
		i++;
	}
	
	public void undo() {
		if(i>=0) {
			liste.get(i).undoCommande();
			i--;
		}
	}
	
	public void redo() {
		if(i<liste.size()-1) {
			i++;
			liste.get(i).doCommande();
		}
	}
	
}
