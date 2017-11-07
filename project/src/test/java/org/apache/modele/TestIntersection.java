package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestIntersection {

	@Test
	public void TestGetTronconParDestination() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		
		assertNull(plan.getAllIntersections().get(Long.valueOf("26155370")).getTronconParDestination(Long.valueOf("459797860")));
	}

}
