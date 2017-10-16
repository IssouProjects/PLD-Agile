package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;

public interface Etat {

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Ouvrir un plan de ville"
	 * @param planDeVille
	 * @param fenetrePrincipale
	 */
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale);
}
