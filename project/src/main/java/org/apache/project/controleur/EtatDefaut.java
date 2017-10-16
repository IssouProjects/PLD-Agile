package org.apache.project.controleur;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.vue.FenetrePrincipale;

public abstract class EtatDefaut implements Etat {

	public void ouvrirPlanDeVille(Controleur controleur, PlanDeVille planDeVille, FenetrePrincipale fenetrePrincipale){}
}
