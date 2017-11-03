package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.*;
import org.xml.sax.SAXException;

public class TestTournee {

	@SuppressWarnings("deprecation")
	@Test(timeout = 1000)
	public void testCalculerTourneePerso() throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit4.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
		
		// Calcul tournee
		Tournee tournee = new Tournee();
		
		tournee.setEntrepot(demande.getEntrepot());
		assertEquals(0, (long)tournee.getEntrepot().getLieuDeLivraison().getIdNoeud());
			
		tournee.calculerTournee(plan, demande);
		List<Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();
		
		// Tests 100% sur si distance en m et duree en s
		assertEquals(480, chemins.get(0).getDuree());
		
		assertEquals(new Time (8,8,0),tournee.getLivraisonsOrdonnees().get(1).getHeureArrivee());
		assertEquals(480, chemins.get(1).getDuree());
		
		assertEquals(new Time (8,16,0),tournee.getLivraisonsOrdonnees().get(2).getHeureArrivee());
		assertEquals(3600, chemins.get(2).getDuree());
		
		assertEquals(new Time (9,16,0),tournee.getLivraisonsOrdonnees().get(3).getHeureArrivee());
		assertEquals(2760, chemins.get(3).getDuree());
		
		assertEquals(7320, tournee.getDureeTourneeSecondes());
	}
	
	@Test(timeout = 1000)
	public void testCalculerTourneeHorairePerso() throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit4TW1.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
		
		// Calcul tournee
		Tournee tournee = new Tournee();
		
		tournee.setEntrepot(demande.getEntrepot());
		assertEquals(0, (long)tournee.getEntrepot().getLieuDeLivraison().getIdNoeud());
			
		tournee.calculerTournee(plan, demande);
		List<Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();
		
		// Tests 100% sur si distance en m et duree en s
		assertEquals(480, chemins.get(0).getDuree());
		
		//Doit attendre 10h pour livrer
		assertEquals(new Time (8,8,0),tournee.getLivraisonsOrdonnees().get(1).getHeureArrivee());
		assertEquals(480, chemins.get(1).getDuree());
		
		assertEquals(new Time (10,8,0),tournee.getLivraisonsOrdonnees().get(2).getHeureArrivee());
		assertEquals(3600, chemins.get(2).getDuree());
		
		assertEquals(new Time (11,8,0),tournee.getLivraisonsOrdonnees().get(3).getHeureArrivee());
		assertEquals(2760, chemins.get(3).getDuree());
		
		//7320 + 3720 (112 * 60)
		assertEquals(14040, tournee.getDureeTourneeSecondes());
	}
	
	
	@Test(timeout = 1000)
	public void testCalculerTourneeDonnee() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
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
		tournee.calculerTournee(plan, demande);
		

		// Verification de l'ordre et des intersection a livrer
		assertEquals(25321357, (long) tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long) tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long) tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long) tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long) tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());
		


		// Test duree tournee
		// WARNING: Test de non regression -> cette valeur n'est pas 100% sûr
		assertEquals(4118, tournee.getDureeTourneeSecondes());
		
		// Verification que l'ajout de l'entrepot en derniere position n'impacte pas la duree
		tournee.ajouterListeLivraison(demande.getEntrepot());
		assertEquals(25321357, (long) tournee.getLivraisonsOrdonnees().get(5).getLieuDeLivraison().getIdNoeud());
		
		tournee.calculerDureeTotale();
		// WARNING: Test de non regression -> cette valeur n'est pas 100% sûr
		assertEquals(4118, tournee.getDureeTourneeSecondes());
	}

	/*@Test(timeout = 30000)
	public void testCalculerGrandeTournee()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLgrand20.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonGrand.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.calculerTournee(plan, demande);
		
		  assertEquals(517370427,
		  (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud
		  ()); assertEquals(21674814,
		  (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud
		  ()); assertEquals(315381991,
		  (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud
		  ()); assertEquals(245032683,
		  (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud
		  ()); //D'autres lieux, mais impossible de tous les tester
		  //assertEquals(Long.valueOf("3280925503"),
		  System.out.println((Long)tournee.getLivraisonsOrdonnees().get(16).
		  getLieuDeLivraison().getIdNoeud());
		  //assertEquals(Long.valueOf("4136115322"),
		  System.out.println((Long)tournee.getLivraisonsOrdonnees().get(17).
		  getLieuDeLivraison().getIdNoeud());
		  //assertEquals(Long.valueOf("3840413118"),
		  System.out.println((Long)tournee.getLivraisonsOrdonnees().get(18).
		  getLieuDeLivraison().getIdNoeud());
		  
		  //Duree de la tournee ok assertEquals(15593,
		  tournee.getDureeTourneeSecondes());
		 
	}*/

	@Test
	public void TestClear() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une tournee
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.calculerTournee(plan, demande);

		assertNotNull(tournee.getEntrepot());
		assertNotNull(tournee.getEntrepot().getHeureDepart());
		assertFalse(tournee.getChemins().isEmpty());
		assertFalse(tournee.getLivraisonsOrdonnees().isEmpty());
		assertNotEquals(0, tournee.getDureeTourneeSecondes());

		tournee.clear();

		assertNull(tournee.getEntrepot());
		assertTrue(tournee.getChemins().isEmpty());
		assertTrue(tournee.getLivraisonsOrdonnees().isEmpty());
		assertEquals(0, tournee.getDureeTourneeSecondes());
	}
}
