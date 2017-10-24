package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatAjoutLivraison3 extends EtatDefaut{
	
	private Intersection intersectionLivraison;
	
	public void livraisonClicked(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		
	}

	protected void actionEntree(Intersection intersection) {
		this.intersectionLivraison = intersection;
	}
}
