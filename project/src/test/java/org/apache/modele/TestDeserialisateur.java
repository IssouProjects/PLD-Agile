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
import static org.junit.Assert.assertEquals;

public class TestDeserialisateur {

	@Test(timeout=1000)
	public void testChargerPlanDeVille() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
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
