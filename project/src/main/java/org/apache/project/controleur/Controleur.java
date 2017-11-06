package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;

import java.io.File;
import java.sql.Time;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Tournee;
import org.apache.project.modele.Troncon;
import org.apache.project.vue.FenetrePrincipale;

public class Controleur {

	private PlanDeVille planDeVille;
	private DemandeDeLivraison demandeDeLivraison;
	private FenetrePrincipale fenetrePrincipale;
	private Tournee tournee;
	private Etat etatCourant;
	private int tempsLimite = 10000;
	// Instances associees a chaque etat possible du controleur
	protected final EtatInit etatInit = new EtatInit();
	protected final EtatPlanCharge etatPlanCharge = new EtatPlanCharge();
	protected final EtatDemandeLivraisonCharge etatDemandeLivraisonCharge = new EtatDemandeLivraisonCharge();
	protected final EtatTourneeCalculee etatTourneeCalculee = new EtatTourneeCalculee();
	protected final EtatAjoutLivraison1 etatAjoutLivraison1 = new EtatAjoutLivraison1();
	protected final EtatAjoutLivraison2 etatAjoutLivraison2 = new EtatAjoutLivraison2();
	protected final EtatAjoutLivraison3 etatAjoutLivraison3 = new EtatAjoutLivraison3();
	protected final EtatSupprLivraison1 etatSupprLivraison1 = new EtatSupprLivraison1();
	protected final EtatModifierLivraison1 etatModifierLivraison1 = new EtatModifierLivraison1();

	private ListeDeCommandes commandes;

	private static Controleur instance = null;

	/**
	 * Cree le controleur de l'application
	 */
	private Controleur() {
		instance = this;
		planDeVille = new PlanDeVille();
		demandeDeLivraison = new DemandeDeLivraison();
		tournee = new Tournee();
		etatCourant = etatInit;
		commandes = new ListeDeCommandes();
	}

	public static Controleur getInstance() {
		if (instance == null) {
			instance = new Controleur();
		}
		return instance;
	}

	public void setFenetre(FenetrePrincipale fenetre) {
		fenetrePrincipale = fenetre;
		commandes.addObserver(fenetrePrincipale.getUndoRedoWidget());
	}

	/**
	 * Change l'etat courant du controleur
	 * 
	 * @param etat
	 *            le nouvel etat courant
	 */
	protected void setEtatCourant(Etat etat) {
		etatCourant = etat;
	}

	public void ouvrirPlanDeVille() {
		etatCourant.ouvrirPlanDeVille(this, planDeVille, fenetrePrincipale, commandes);
	}

	public void chargerPlanDeVille(File fichier) {
		etatCourant.chargerPlanDeVille(this, planDeVille, fenetrePrincipale, fichier);
	}

	public void ouvrirDemandeDeLivraison() {
		etatCourant.ouvrirDemandeDeLivraison(this, planDeVille, demandeDeLivraison, fenetrePrincipale, commandes);
	}

	public void chargerDemandeDeLivraison(File fichier) {
		etatCourant.chargerDemandeDeLivraison(this, planDeVille, demandeDeLivraison, fenetrePrincipale, fichier);
	}

	public void calculerTournee() {
		etatCourant.calculerTournee(this, planDeVille, demandeDeLivraison, tournee, fenetrePrincipale, tempsLimite);
	}

	public void calculerTournee(int nouveauTempsLimite) {
		etatCourant.calculerTournee(this, planDeVille, demandeDeLivraison, tournee, fenetrePrincipale,
				nouveauTempsLimite);
	}

	public void ajouterLivraison() {
		etatCourant.ajouterLivraison(this, fenetrePrincipale);
	}

	public void supprimerLivraison() {
		etatCourant.supprimerLivraison(this, tournee, planDeVille, fenetrePrincipale, commandes);
	}

	public void editerLivraison() {
		etatCourant.modifierLivraison(this, fenetrePrincipale);
	}

	public void validerModificationLivraison(Integer duree, Time heureDeb, Time heureFin) {
		etatCourant.validerModificationLivraison(this, fenetrePrincipale, tournee, heureDeb, heureFin, duree,
				commandes);
	}

	public void echangerLivraison(int nouveauIndex) {
		etatCourant.echangerLivraison(this, planDeVille, fenetrePrincipale, tournee, nouveauIndex, commandes);
	}

	public void annuler() {
		etatCourant.annuler(this, fenetrePrincipale);
	}

	public void annulerRecalcul() {
		etatCourant.annulerRecalcul(this, fenetrePrincipale, tournee);
	}

	public void intersectionClicked(Intersection intersection) {
		etatCourant.intersectionClicked(this, planDeVille, demandeDeLivraison, tournee, fenetrePrincipale,
				intersection);
	}

	public void livraisonClicked(Livraison livraison) {
		etatCourant.livraisonClicked(this, fenetrePrincipale, planDeVille, tournee, livraison, commandes);
	}

	public void tronconClicked(Troncon troncon) {
		etatCourant.tronconClicked(this, fenetrePrincipale, planDeVille, troncon, commandes);
	}

	public void calculerCheminsNouvelleLivraison(Integer duree, Time heureDeb, Time heureFin) {
		etatCourant.calculerCheminsNouvelleLivraison(this, planDeVille, tournee, fenetrePrincipale, duree, heureDeb,
				heureFin, commandes);
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

	public void undo() {
		etatCourant.undo(commandes);
		tournee.calculerDureeTotale();
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
	}

	public void redo() {
		etatCourant.redo(commandes);
		tournee.calculerDureeTotale();
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
	}

	public void afficherFenetreTimeout() {
		etatCourant.afficherFenetreTimeout(this, fenetrePrincipale);
	}
	
	public void exporterFeuilleDeRoute() {
		etatCourant.exporterFeuilleDeRoute(this, fenetrePrincipale, tournee);
	}
}
