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
	 * Methode appelee par controleur apres un clic sur le bouton "Ouvrir un plan de
	 * ville"
	 * 
	 * @param planDeVille
	 *            l'objet <tt>PlanDeVille</tt> qu'on va compléter en désérialisant
	 *            un fichier XML.
	 * @param fenetrePrincipale
	 *            la fenêtre principale de l'application.
	 */
	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale);

	/**
	 * Méthode appelée par controleur apres un clic sur le bouton "Ouvrir demande de
	 * livraison"
	 * 
	 * @param controleur
	 *            contrôleur de l'application.
	 * @param planDeVille
	 *            plan de la ville où a lieu les livraisons.
	 * @param demandeDelivraison
	 *            objet <tt>DemandeDeLivraison</tt> qu'on va compléter en
	 *            désérialisant un fichier XML.
	 * @param fenetrePrincipale
	 *            la fenêtre principale de l'application.
	 */
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale);

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Calculer tournée"
	 * 
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDeLivraison
	 * @param tournee
	 * @param fenetrePrincipale
	 */
	public void calculerTournee(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison,
			Tournee tournee, FenetrePrincipale fenetrePrincipale);

  /**
	 * Methode appelee par controleur pour annuler une action
	 * @param controleur
	 * @param fenetrePrincipale
	 */
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale);

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Valider" de la popup d'ajout de livraison
	 * @param controleur
	 * @param planDeVille
	 * @param tournee
	 * @param fenetrePrincipale
	 * @param duree
	 * @param heureDeb
	 * @param heureFin
	 */
	public void calculerCheminsNouvelleLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin);
	
  /**
	 * Methode appelee par controleur apres un clic sur le bouton "Ajouter
	 * livraison"
	 * 
	 * @param controleur
	 * @param planDeVille
	 * @param tournee
	 * @param fenetrePrincipale
	 */
	public void calculerCheminSupprLivraison(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Ajouter livraison"
	 * @param controleur
	 * @param fenetrePrincipale
	 */
	public void ajouterLivraison(Controleur controleur,  FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Supprimer livraison"
	 * @param controleur
	 * @param fenetrePrincipale
	 */
	public void supprimerLivraison(Controleur controleur, Tournee tournee, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Modifier livraison"
	 * @param controleur
	 * @param fenetrePrincipale
	 */
	public void  modifierLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Valider" de la popup de modification
	 * @param controleur
	 * @param fenetrePrincipale
	 * @param tournee
	 * @param heureDeb
	 * @param heureFin
	 */
	public void validerModificationLivraison(Controleur controleur, FenetrePrincipale fenetrePrincipale, Tournee tournee, Time heureDeb, Time heureFin);
	
	/**
	 * Methode appelee apres un clic sur une intersection
	 * @param controleur
	 * @param planDeVille
	 * @param demandeDeLivraison
	 * @param tournee
	 * @param fenetrePrincipale
	 * @param intersection
	 */
	public void intersectionClicked(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale,
			Intersection intersection);

	/**
	 * Methode appelee apres un clic sur une livraison
	 * @param controleur
	 * @param fenetrePrincipale
	 * @param livraisonPrecedente
	 */
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, PlanDeVille plan, 
			Tournee tournee, Livraison livraison, ListeDeCommandes commandes);

}
