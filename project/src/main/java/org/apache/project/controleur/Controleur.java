package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;

public class Controleur {
	
	private PlanDeVille planDeVille;
	private FenetrePrincipale fenetrePrincipale;
	private Etat etatCourant;
	// Instances associees a chaque etat possible du controleur
	protected final EtatInit etatInit = new EtatInit();
	
	/**
	 * Cree le controleur de l'application
	 * @param planDeVille le plan de la ville 
	 */
	public Controleur(PlanDeVille planDeVille) {
		this.planDeVille = planDeVille;
		etatCourant = etatInit;
		//fenetrePrincipale = new FenetrePrincipale(plan, this);
	}
}
