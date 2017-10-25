package org.apache.project.controleur;

import java.sql.Time;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;


public class EtatAjoutLivraison3 extends EtatDefaut{
	
	Livraison nouvelleLivraison;
	Livraison livraisonPrecedente;
	
	public void calculerChemins(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin) {
		nouvelleLivraison.setDuree(duree);
		
		if(heureDeb != null && heureFin != null) {
			nouvelleLivraison.setPlageHoraire(new PlageHoraire(heureDeb, heureFin));
		}				
		tournee.calculerNouveauxChemins(planDeVille, livraisonPrecedente, nouvelleLivraison);
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}
	
	protected void actionEntreeEtatAjoutLivraison3(Livraison livraisonPrecedente, Livraison nouvelleLivraison) {
		this.nouvelleLivraison = nouvelleLivraison;
		this.livraisonPrecedente = livraisonPrecedente;
	}
}
