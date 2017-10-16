package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;

public class Controleur {
	
	private PlanDeVille planDeVille;
	private FenetrePrincipale fenetrePrincipale;
	private Etat etatCourant;
	// Instances associees a chaque etat possible du controleur
	protected final EtatInit etatInit = new EtatInit();
	protected final EtatPlanCharge etatPlanCharge = new EtatPlanCharge();
	protected final EtatDemandeLivraisonCharge etatDemandeLivraisonCharge = new EtatDemandeLivraisonCharge();
	
	/**
	 * Cree le controleur de l'application
	 * @param planDeVille le plan de la ville 
	 */
	public Controleur(PlanDeVille planDeVille) {
		this.planDeVille = planDeVille;
		etatCourant = etatInit;
		//fenetrePrincipale = new FenetrePrincipale(plan, this);
	}
	
	/**
	 * Change l'etat courant du controleur
	 * @param etat le nouvel etat courant
	 */
	protected void setEtatCourant(Etat etat){
		etatCourant = etat;
	}
	
	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Ouvrir un plan de ville"
	 */
	public void ouvrirPlanDeVille() {
		etatCourant.ouvrirPlanDeVille(this, planDeVille, fenetrePrincipale);
	}
}
