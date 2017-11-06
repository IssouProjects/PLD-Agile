package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.modele.Troncon;
import org.apache.project.vue.FenetrePrincipale;

import java.io.File;
import java.sql.Time;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;

public abstract class EtatDefaut implements Etat {

	@Override
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale) {
	}

	@Override
	public void chargerPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale,
			File fichier) {
	}

	@Override
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale) {
	}

	@Override
	public void chargerDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale, File fichier) {
	}

	@Override
	public void calculerCheminsNouvelleLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee,
			FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin,
			ListeDeCommandes commandes) {
	}

	@Override
	public void calculerCheminSupprLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee,
			FenetrePrincipale fenetrePrincipale) {
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
	}

	@Override
	public void ajouterLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
	}

	@Override
	public void supprimerLivraison(Controleur controleur, Tournee tournee, PlanDeVille planDeVille,
			FenetrePrincipale fenetrePrincipale, ListeDeCommandes commandes) {
	}

	@Override
	public void modifierLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
	}

	@Override
	public void echangerLivraison(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale,
			Tournee tournee, int nouveauIndex, ListeDeCommandes commandes) {
	}

	@Override
	public void validerModificationLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale,
			Tournee tournee, Time heureDeb, Time heureFin, Integer duree, ListeDeCommandes commandes) {
	}

	@Override
	public void intersectionClicked(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale,
			Intersection intersection) {
	}

	@Override
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan,
			Tournee tournee, Livraison livraison, ListeDeCommandes commandes) {
	}

	@Override
	public void tronconClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan,
			Troncon troncon, ListeDeCommandes commandes) {
	}

	@Override
	public void undo(ListeDeCommandes commandes) {
	}

	@Override
	public void redo(ListeDeCommandes commandes) {
	}

	@Override
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison,
			Tournee tournee, FenetrePrincipale fenetrePrincipale, int tempsLimite) {
	}

	@Override
	public void annulerRecalcul(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee) {
	}

	@Override
	public void afficherFenetreTimeout(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
	}
}
