package org.apache.project.controleur;

import org.apache.project.modele.Livraison;
import org.apache.project.vue.FenetrePrincipale;

public class EtatSupprLivraison1 extends EtatDefaut{

Livraison livraisonSelectionee;
	
	@Override
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale, Livraison livraisonSelectionee) {
		this.livraisonSelectionee = livraisonSelectionee;
		
		controleur.etatSupprLivraison2.actionEntreeEtatSupprLivraison2(livraisonSelectionee);
		controleur.setEtatCourant(controleur.etatSupprLivraison2);
	}
}
