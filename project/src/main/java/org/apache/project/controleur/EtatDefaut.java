package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;
import org.apache.project.modele.DemandeDeLivraison;


public abstract class EtatDefaut implements Etat {

	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){}
	public void ouvrirDemandeDeLivraison(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDelivraison, FenetrePrincipale fenetrePrincipale){}
}
