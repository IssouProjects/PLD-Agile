package org.apache.project.controleur;

public interface Commande {
	
	/**
	 * Méthode permettant d'exécuter la dernière commande effectuée
	 */
	public void doCommande();
	
	/**
	 * Méthode permettant de défaire la dernière commande effectuée
	 */
	public void undoCommande();
}
