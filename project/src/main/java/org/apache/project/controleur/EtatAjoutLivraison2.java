package org.apache.project.controleur;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;

public class EtatAjoutLivraison2 extends EtatDefaut {

	private Intersection intersectionLivraison;
	private Livraison nouvelleLivraison;

	@Override
	public void livraisonClicked(Controleur controleur, FenetrePrincipale fenetrePrincipale,
			PlanDeVille plan, Tournee tournee, Livraison livraisonPrecedente, ListeDeCommandes commandes) {
		
		fenetrePrincipale.highlightLivraison(livraisonPrecedente);
		fenetrePrincipale.getListDisplay().disableAddHint();
		controleur.setEtatCourant(controleur.etatAjoutLivraison3);
		nouvelleLivraison = new Livraison(intersectionLivraison);
		fenetrePrincipale.afficherFenetreAjouterLivraison(nouvelleLivraison);
		controleur.etatAjoutLivraison3.actionEntreeEtatAjoutLivraison3(livraisonPrecedente, nouvelleLivraison);
	}

	@Override
	public void intersectionClicked(Controleur controleur, PlanDeVille planDeVille,
			DemandeDeLivraison demandeDeLivraison, Tournee tournee, FenetrePrincipale fenetrePrincipale,
			Intersection intersection) {

		fenetrePrincipale.highlightIntersection(intersection);
		
		if (intersection != null) {
			controleur.setEtatCourant(controleur.etatAjoutLivraison2);
			controleur.etatAjoutLivraison2.actionEntreeEtatAjoutLivraison2(intersection);
			fenetrePrincipale.afficherInfo("Veuillez cliquer sur une livraison ou choisir une autre intersection");
		} else {
			fenetrePrincipale.afficherPopupError("Veuillez cliquer sur une intersection valide");
		}
	}

	@Override
	public void annuler(Controleur controleur, FenetrePrincipale fenetrePrincipale) {
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
		fenetrePrincipale.getListDisplay().disableAddHint();
		fenetrePrincipale.afficherInfo("Ajout annulé, vous êtes libre");
	}

	protected void actionEntreeEtatAjoutLivraison2(Intersection intersection) {
		this.intersectionLivraison = intersection;
	}

}
