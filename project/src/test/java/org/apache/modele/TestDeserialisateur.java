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
		
		assertEquals((long)plan.getAllIntersections().get(Long.valueOf(365117235)).getCoordX(), 14237);
		assertEquals((long)plan.getAllIntersections().get(Long.valueOf(365117235)).getCoordY(), 36361);
		
		assertEquals((long)plan.getAllIntersections().get(Long.valueOf(325772328)).getCoordX(), 19639);
		assertEquals((long)plan.getAllIntersections().get(Long.valueOf(325772328)).getCoordY(), 37972);
		
		//Verification quelques troncons
		assertEquals(plan.getAllTroncons().get(105).getNomRue(), "Rue Montvert");
	}
	
	@Test(timeout=5000)
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
		assertEquals((long)demande.getAdresseEntrepot().getIdNoeud(), 1682387619);
		
		//Heure de depart ok
		assertEquals(demande.getHeureDepart().toString(), "08:00:00");
		
		//Verification des livraisons
		assertEquals(demande.getListeLivraison().get(1).getDuree(), 600);
		assertEquals((long)demande.getListeLivraison().get(1).getLieuDeLivraison().getIdNoeud(), 2129259180);
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
