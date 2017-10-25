package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public interface Etat {

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Ouvrir un plan de ville"
	 * @param planDeVille
	 * @param fenetrePrincipale
	 */
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Ouvrir demande de livraison"
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDelivraison
	 * @param fenetrePrincipale
	 */
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Calculer tournée"
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDeLivraison
	 * @param tournee
	 * @param fenetrePrincipale
	 */
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale);
	
	public void calculerChemins(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Ajouter livraison"
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDeLivraison
	 * @param tournee
	 * @param fenetrePrincipale
	 */
	public void ajouterLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee après un clic sur une intersection
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDeLivraison
	 * @param tournee
	 * @param fenetrePrincipale
	 * @param intersection
	 */
	public void intersectionClicked(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection intersection);
	
	/**
	 * Methode appelée après un clic sur une livraison
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDeLivraison
	 * @param tournee
	 * @param fenetrePrincipale
	 * @param intersection
	 */
	public void livraisonClicked(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente);
	
	public void entrepotClicked(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale);
}
