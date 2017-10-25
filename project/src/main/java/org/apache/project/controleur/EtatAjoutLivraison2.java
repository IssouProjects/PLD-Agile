package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;


public class EtatAjoutLivraison2 extends EtatDefaut{
	
private Intersection intersectionLivraison;
private Livraison livraisonPrecedente;
	
	public void livraisonClicked(Controleur controleur, PlanDeVille planDeVille, DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		this.livraisonPrecedente = livraisonPrecedente;
		Livraison l = new Livraison(intersectionLivraison, 0);
		fenetrePrincipale.afficherFenetreAjouterLivraison(l);
		controleur.setEtatCourant(controleur.etatAjoutLivraison3);
	}

	protected void actionEntree(Intersection intersection) {
		this.intersectionLivraison = intersection;
	}

}
