package org.apache.modele;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class TestDeserialisateur extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testChargerPlanDeVille() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = new File("src/test/java/org/apache/modele/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
	}
	
	public void testChargerPlanDeVilleIncorrect() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		try {
			File xml = new File("src/test/java/org/apache/modele/planLyonPetitIncorrect.xml");
			PlanDeVille plan = new PlanDeVille();
			Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
	        fail("Une exception devrait etre renvoyee car ce fichier a un noeud invalide");
	    } catch (Exception e) {
	        assertEquals(e.getMessage(), "Document non conforme");
	    }
	}

}