package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class Controleur {
	
	private PlanDeVille planDeVille;
	private FenetrePrincipale fenetrePrincipale;
	private Etat etatCourant;
	// Instances associees a chaque etat possible du controleur
	protected final EtatInit etatInit = new EtatInit();
	protected final EtatPlanCharge etatPlanCharge = new EtatPlanCharge();
	protected final EtatDemandeLivraisonCharge etatDemandeLivraisonCharge = new EtatDemandeLivraisonCharge();
	protected final EtatTourneeCalculee etatTourneeCalculee = new EtatTourneeCalculee();
	
	private static Controleur instance = null;
	/**
	 * Cree le controleur de l'application
	 */
	private Controleur(){
		PlanDeVille planDeVille = new PlanDeVille();
		etatCourant = etatInit;
	}
	public static Controleur getInstance(){
		if (instance == null) instance = new Controleur();
		return instance;
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
