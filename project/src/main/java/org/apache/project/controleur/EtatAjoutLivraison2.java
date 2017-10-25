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
private Livraison nouvelleLivraison;
	
	@Override
	public void livraisonClicked(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Livraison livraisonPrecedente) {
		this.livraisonPrecedente = livraisonPrecedente;
		//Pour le moment
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		nouvelleLivraison = new Livraison(intersectionLivraison);
		//Todo: Appelé fenetre popup 
		nouvelleLivraison.setDuree(2100);
		tournee.calculerNouveauxChemins(planDeVille, livraisonPrecedente, nouvelleLivraison);
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		//controleur.etatAjoutLivraison3.actionEntreeEtatAjoutLivraison3(livraisonPrecedente, nouvelleLivraison);
	}
	
	@Override
	public void entrepotClicked(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Intersection entrepot) {
		
	}
	
	protected void actionEntreeEtatAjoutLivraison2(Intersection intersection) {
		this.intersectionLivraison = intersection;
	}

}
