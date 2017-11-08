package org.apache.project.controleur;

import java.util.LinkedList;
import java.util.Observable;

/**
 *
 */
public class ListeDeCommandes extends Observable {

	private LinkedList<Commande> liste;
	private int i; // TODO pas super explicite comme nom de variable (╯°□°）╯︵ ┻━┻

	/**
	 * 
	 */
	public ListeDeCommandes() {
		liste = new LinkedList<Commande>();
		i = -1;
	}

	/**
	 * @param commande
	 *            Commande qu'on ajoute.
	 * @return un entier correspondant au code de retour, 0 si ok, sinon autre
	 *         chose.
	 */
	public int ajouteCommande(Commande commande) {
		int temp = i + 1;
		while (temp < liste.size()) {
			liste.remove(temp);
		}
		i++;
		int retour = commande.doCommande();
		if (retour == 0) {
			liste.add(i, commande);
		} else {
			i--;
		}
		setChanged();
		notifyObservers();
		return retour;
	}

	/**
	 * 
	 */
	public void undo() {
		if (!liste.isEmpty() && i >= 0) {
			liste.get(i).undoCommande();
			i--;
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * 
	 */
	public void redo() {
		if (!liste.isEmpty() && i < liste.size() - 1) {
			i++;
			liste.get(i).doCommande();
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * @return
	 */
	public Commande getFirstUndoableCommand() {
		if (!liste.isEmpty() && i >= 0) {
			return liste.get(i);
		}
		return null;
	}

	/**
	 * @return
	 */
	public Commande getFirstRedoableCommand() {
		if (!liste.isEmpty() && i < liste.size() - 1) {
			return liste.get(i + 1);
		}
		return null;
	}

	/**
	 * 
	 */
	public void clearCommandes() {
		i = -1;
		liste.clear();
		setChanged();
		notifyObservers();
	}
}
