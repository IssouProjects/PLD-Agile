package org.apache.modele;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestDeserialisateur {

	@Test(timeout=100)
	public void testChargerPlanDeVille() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
		
		assertEquals(14237, (long)plan.getAllIntersections().get(Long.valueOf(365117235)).getCoordX());
		assertEquals(36361, (long)plan.getAllIntersections().get(Long.valueOf(365117235)).getCoordY());
		
		assertEquals(19639, (long)plan.getAllIntersections().get(Long.valueOf(325772328)).getCoordX());
		assertEquals(37972, (long)plan.getAllIntersections().get(Long.valueOf(325772328)).getCoordY());
		
		//Verification quelques troncons
		assertEquals("Rue Montvert", plan.getAllTroncons().get(105).getNomRue());
	}
	
	@Test(timeout=9000)
	public void testChargerGrandPlanDeVille() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = new File("src/test/java/org/apache/modele/fichiers/planLyonGrand.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
	}
	
	@Test(timeout=1000)
	public void testChargerDemandeDeLivraison()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLmoyen5TW1.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
	}
	
	@Test(timeout=8000)
	public void testChargerGrandeDemandeDeLivraison() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLmoyen5TW1.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
		
		//L'adresse de l'entrepot est bien la bonne
		assertEquals(1682387619, (long)demande.getAdresseEntrepot().getIdNoeud());
		
		//Heure de depart ok
		assertEquals("08:00:00", demande.getHeureDepart().toString());
		
		//Verification des livraisons
		assertEquals(300, demande.getListeLivraison().get(0).getDuree());
		assertEquals(1383027249, (long)demande.getListeLivraison().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals("10:00:00", demande.getListeLivraison().get(0).getPlageHoraire().getDebut().toString());
		assertEquals("12:00:00", demande.getListeLivraison().get(0).getPlageHoraire().getFin().toString());
		
		assertEquals(600, demande.getListeLivraison().get(1).getDuree());
		assertEquals(2129259180, (long)demande.getListeLivraison().get(1).getLieuDeLivraison().getIdNoeud());
		assertNull(demande.getListeLivraison().get(1).getPlageHoraire());
	}
	

	@Test(expected=ExceptionXML.class)
	public void testChargerPlanDeVilleIncorrect()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
			File xml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetitIncorrect.xml");
			PlanDeVille plan = new PlanDeVille();
			Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
	}

	@Test(expected=ExceptionXML.class)
	public void testChargerDemandeDeLivraisonIncorrecte()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
			File xml = new File("src/test/java/org/apache/modele/fichiers/DLmoyen5TW2.xml");
			File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
			PlanDeVille plan = new PlanDeVille();
			Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
			DemandeDeLivraison demande = new DemandeDeLivraison();
			Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
	}

}
