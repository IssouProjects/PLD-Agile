package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

import java.io.File;
import java.sql.Time;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;


public abstract class EtatDefaut implements Etat {

	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){}
	public void chargerPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale, File fichier) {}
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale){}
	public void chargerDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale, File fichier) {}
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, int tempsLimite) {}
	public void calculerCheminsNouvelleLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin, ListeDeCommandes commandes) {}
	public void calculerCheminSupprLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale) {}
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {}
	public void ajouterLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale) {}
	public void supprimerLivraison(Controleur controleur, Tournee tournee, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale, ListeDeCommandes commandes) {}
	public void modifierLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale) {}
	public void validerModificationLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee, Time heureDeb, Time heureFin) {}
	public void intersectionClicked(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection intersection) {}
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan, 
			Tournee tournee, Livraison livraison, ListeDeCommandes commandes) {}
	public void undo(ListeDeCommandes commandes) {}
	public void redo(ListeDeCommandes commandes) {}
}
