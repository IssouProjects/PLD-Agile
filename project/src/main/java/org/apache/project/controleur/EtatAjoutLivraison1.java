package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatAjoutLivraison1 extends EtatDefaut {

	@Override
	public void intersectionClicked(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale,
			Intersection intersection) {
		
		fenetrePrincipale.highlightIntersection(intersection);
		
		if(intersection != null) {
			controleur.setEtatCourant(controleur.etatAjoutLivraison2);
			controleur.etatAjoutLivraison2.actionEntreeEtatAjoutLivraison2(intersection);
			fenetrePrincipale.afficherInfo("Veuilliez cliquer sur une livraison ou choisir une autre intersection");
		} else {
			fenetrePrincipale.afficherPopupError("Veuilliez cliquer sur une intersection valide");
		}
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.afficherInfo("Ajout annulé, vous êtes libre");
	}
}
