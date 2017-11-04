package org.apache.project.controleur;

import java.util.LinkedList;
import java.util.Observable;

public class ListeDeCommandes extends Observable{
	
	private LinkedList<Commande> liste;
	private int i; //pas super explicite comme nom de variable (╯°□°）╯︵ ┻━┻

	public ListeDeCommandes() {
		liste = new LinkedList<Commande>();
		i=-1;
	}
	
	public void ajouteCommande(Commande commande) {
		int temp = i+1;
        while(temp<liste.size()){
            liste.remove(temp);
        }
        i++;
        liste.add(i, commande);
        commande.doCommande();
        setChanged();
        notifyObservers();
	}
	
	public void undo() {
		if(!liste.isEmpty() && i>=0) {
			liste.get(i).undoCommande();
			i--;
			setChanged();
			notifyObservers();
		}
	}
	
	public void redo() {
		if(!liste.isEmpty() && i<liste.size()-1) {
			i++;
			liste.get(i).doCommande();
			setChanged();
			notifyObservers();
		}
	}
	
	public Commande getFirstUndoableCommand() {
		if(!liste.isEmpty() && i>=0) {
			return liste.get(i);
		}
		return null;
	}
	
	public Commande getFirstRedoableCommand() {
		if(!liste.isEmpty() && i<liste.size()-1) {
			return liste.get(i+1);
		}
		return null;
	}	
}
