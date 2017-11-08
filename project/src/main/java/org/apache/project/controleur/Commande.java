package org.apache.project.controleur;

/**
 *
 */
public interface Commande {

	/**
	 * Méthode permettant d'exécuter la dernière commande effectuée
	 * 
	 * @return 0 si la commande s'est bien déroulée, un autre <tt>int</tt> sinon
	 */
	public int doCommande();

	/**
	 * Méthode permettant d'annuler la dernière commande effectuée
	 */
	public void undoCommande();
}
