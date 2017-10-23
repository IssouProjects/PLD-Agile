package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class Controleur {
	
	private PlanDeVille planDeVille;
	private DemandeDeLivraison demandeDeLivraison;
	private FenetrePrincipale fenetrePrincipale;
	private Tournee tournee;
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
		instance = this;
		planDeVille = new PlanDeVille();
		demandeDeLivraison = new DemandeDeLivraison();
		tournee = new Tournee();
		etatCourant = etatInit;		
	}
	
	public static Controleur getInstance(){
		if (instance == null){
			instance = new Controleur();
		}
		return instance;
	}
	
	public void setFenetre(FenetrePrincipale fenetre) {
		fenetrePrincipale = fenetre;
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
	
	public void ouvrirDemandeDeLivraison() {
		etatCourant.ouvrirDemandeDeLivraison(this, planDeVille, demandeDeLivraison, fenetrePrincipale);
	}
	
	public void calculerTournee() {
		etatCourant.calculerTournee(this, planDeVille, demandeDeLivraison, tournee, fenetrePrincipale);
	}
	
	public void clearPlanDeVille() {
		planDeVille.clear();
	}
	
	public void clearDemandeDeLivraison() {
		demandeDeLivraison.clear();
	}
	
	public void clearTournee() {
		tournee.clear();
	}
}
