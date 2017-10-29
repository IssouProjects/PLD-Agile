package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestAjoutLivraison {

	@Test(timeout = 1000)
	public void testAjoutSimple() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.setHeureDepart(demande.getHeureDepart());
		tournee.calculerTournee(plan, demande);
		
		// Copie des chemins initiaux
		List <Chemin> anciensChemins = new ArrayList<Chemin>();
		for(Chemin c : tournee.getChemins()){
			anciensChemins.add(c);
		}
		
		// On recupère la livraison suivante et la livraison précédente
		Livraison livraisonPre = tournee.getLivraison(0);
		Livraison livraisonSuiv = tournee.getLivraison(1);
		
		Intersection intersectionLiv = plan.getIntersection((long) 251171098);
		Livraison livraisonNouv = new Livraison (intersectionLiv, 600);
		
		// Calcule des nouveaux chemins
		List <Chemin> nouveauxChemins = new ArrayList<Chemin>();
		nouveauxChemins = tournee.calculerNouveauxChemins(plan, livraisonPre.getLieuDeLivraison(), livraisonNouv.getLieuDeLivraison(), livraisonSuiv.getLieuDeLivraison());
		
		// Mise à jour de tournee
		tournee.ajouterLivraison(livraisonNouv, 2);
		tournee.supprimerChemin(1);
		tournee.ajouterChemin(nouveauxChemins.get(0), 1);
		tournee.ajouterChemin(nouveauxChemins.get(1), 2);
		
		//Verification de l'ordre et des intersection a livrer
		assertEquals(25321357, (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(251171098, (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long)tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long)tournee.getLivraisonsOrdonnees().get(5).getLieuDeLivraison().getIdNoeud());
		
		//Verification de l'ordre des tronçons
		List <Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();
		assertEquals(anciensChemins.get(0),chemins.get(0));
		assertEquals(nouveauxChemins.get(0),chemins.get(1));
		assertEquals(nouveauxChemins.get(1),chemins.get(2));
		assertEquals(anciensChemins.get(2),chemins.get(3));
		assertEquals(anciensChemins.get(3),chemins.get(4));
		assertEquals(anciensChemins.get(4),chemins.get(5));
		
		//Verification de la mise a jour des horaires
		int ancienneDuree = tournee.getDureeTourneeSecondes();
		
		tournee.miseAJourHeureDuree();
		
		int nouvelleDuree = tournee.getDureeTourneeSecondes();
		
		assertTrue(ancienneDuree != nouvelleDuree); //La duree a bien ete changee
		assertEquals(3043, nouvelleDuree);
	}
	
	@Test(timeout = 1000)
	public void testAjoutApresEntrepot() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.setHeureDepart(demande.getHeureDepart());
		tournee.calculerTournee(plan, demande);
		
		// Copie des chemins initiaux
		List <Chemin> anciensChemins = new ArrayList<Chemin>();
		for(Chemin c : tournee.getChemins()){
			anciensChemins.add(c);
		}
		
		// On recupère la livraison suivante et la livraison précédente
		Livraison livraisonSuiv = tournee.getLivraison(0);
		
		Intersection intersectionLiv = plan.getIntersection((long) 251171098);
		Livraison livraisonNouv = new Livraison (intersectionLiv, 600);
		
		// Calcule des nouveaux chemins
		List <Chemin> nouveauxChemins = new ArrayList<Chemin>();
		nouveauxChemins = tournee.calculerNouveauxChemins(plan, tournee.getEntrepot().getLieuDeLivraison(), livraisonNouv.getLieuDeLivraison(), livraisonSuiv.getLieuDeLivraison());
		
		// Mise à jour de tournee
		tournee.ajouterLivraison(livraisonNouv, 1);
		tournee.supprimerChemin(0);
		tournee.ajouterChemin(nouveauxChemins.get(0), 0);
		tournee.ajouterChemin(nouveauxChemins.get(1), 1);
		
		//Verification de l'ordre et des intersection a livrer
		assertEquals(25321357, (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(251171098, (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long)tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long)tournee.getLivraisonsOrdonnees().get(5).getLieuDeLivraison().getIdNoeud());
		
		//Verification de l'ordre des tronçons
		List <Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();
		assertEquals(nouveauxChemins.get(0),chemins.get(0));
		assertEquals(nouveauxChemins.get(1),chemins.get(1));
		assertEquals(anciensChemins.get(1),chemins.get(2));
		assertEquals(anciensChemins.get(2),chemins.get(3));
		assertEquals(anciensChemins.get(3),chemins.get(4));
		assertEquals(anciensChemins.get(4),chemins.get(5));
	}
	
	@Test(timeout = 1000)
	public void testAjoutAvantEntrepot() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.setHeureDepart(demande.getHeureDepart());
		tournee.calculerTournee(plan, demande);
		
		// Copie des chemins initiaux
		List <Chemin> anciensChemins = new ArrayList<Chemin>();
		for(Chemin c : tournee.getChemins()){
			anciensChemins.add(c);
		}
		
		// On recupère la livraison suivante et la livraison précédente
		Livraison livraisonPre = tournee.getLivraison(3);
				
		Intersection intersectionLiv = plan.getIntersection((long) 251171098);
		Livraison livraisonNouv = new Livraison (intersectionLiv, 600);
		
		// Calcule des nouveaux chemins
		List <Chemin> nouveauxChemins = new ArrayList<Chemin>();
		nouveauxChemins = tournee.calculerNouveauxChemins(plan, livraisonPre.getLieuDeLivraison(), livraisonNouv.getLieuDeLivraison(), tournee.getEntrepot().getLieuDeLivraison());
		
		// Mise à jour de tournee
		tournee.ajouterLivraison(livraisonNouv, 5);
		tournee.supprimerChemin(4);
		tournee.ajouterChemin(nouveauxChemins.get(0), 4);
		tournee.ajouterChemin(nouveauxChemins.get(1), 5);
		
		//Verification de l'ordre et des intersection a livrer
		assertEquals(25321357, (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long)tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());
		assertEquals(251171098, (long)tournee.getLivraisonsOrdonnees().get(5).getLieuDeLivraison().getIdNoeud());
		
		//Verification de l'ordre des tronçons
		List <Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();
		assertEquals(anciensChemins.get(0),chemins.get(0));
		assertEquals(anciensChemins.get(1),chemins.get(1));
		assertEquals(anciensChemins.get(2),chemins.get(2));
		assertEquals(anciensChemins.get(3),chemins.get(3));
		assertEquals(nouveauxChemins.get(0),chemins.get(4));
		assertEquals(nouveauxChemins.get(1),chemins.get(5));
	}
}
