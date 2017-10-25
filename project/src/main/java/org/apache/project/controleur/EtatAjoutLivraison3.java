package org.apache.project.controleur;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.vue.FenetrePrincipale;


public class EtatAjoutLivraison3 extends EtatDefaut{
	
	Livraison nouvelleLivraison;
	Livraison livraisonPrecedente;
	List <Chemin> nouveauxChemins;
	
	public void calculerChemins(Controleur controleur, PlanDeVille planDeVille, Tournee tournee, FenetrePrincipale fenetrePrincipale, Integer duree, Time heureDeb, Time heureFin) {
		nouvelleLivraison.setDuree(duree);
		
		if(heureDeb != null && heureFin != null) {
			nouvelleLivraison.setPlageHoraire(new PlageHoraire(heureDeb, heureFin));
		}
		
		int indexPre = tournee.getLivraisonIndex(livraisonPrecedente);
		
		if(tournee.getLivraisonIndex(livraisonPrecedente)==tournee.getLivraisonsSize()-1) {
			nouveauxChemins = tournee.calculerNouveauxChemins(planDeVille, livraisonPrecedente.getLieuDeLivraison(), nouvelleLivraison.getLieuDeLivraison(), tournee.getAdresseEntrepot());
		}else {
			Livraison livraisonSuiv = tournee.getLivraison(indexPre + 1);
			nouveauxChemins = tournee.calculerNouveauxChemins(planDeVille, livraisonPrecedente.getLieuDeLivraison(), nouvelleLivraison.getLieuDeLivraison(), livraisonSuiv.getLieuDeLivraison());
		}
		
		tournee.ajouterLivraison(nouvelleLivraison, indexPre + 1);
		tournee.ajouterChemin(nouveauxChemins.get(0), indexPre);
		tournee.ajouterChemin(nouveauxChemins.get(1), indexPre + 1);
		fenetrePrincipale.clearTournee();
		fenetrePrincipale.afficherTournee(tournee);
		fenetrePrincipale.afficherInfo("Vous êtes libre");
		controleur.setEtatCourant(controleur.etatTourneeCalculee);
	}
	
	protected void actionEntreeEtatAjoutLivraison3(Livraison livraisonPrecedente, Livraison nouvelleLivraison) {
		this.nouvelleLivraison = nouvelleLivraison;
		this.livraisonPrecedente = livraisonPrecedente;
		this.nouveauxChemins = new ArrayList<Chemin>();
	}
}
