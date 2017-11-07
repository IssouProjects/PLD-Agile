package org.apache.project.controleur;

public interface Commande {
	
	/**
	 * Méthode permettant d'exécuter la dernière commande effectuée
	 */
	public int doCommande();
	
	/**
	 * Méthode permettant de défaire la dernière commande effectuée
	 */
	public void undoCommande();
}
