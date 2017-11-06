package org.apache.modele;

import java.io.File;

import org.apache.project.modele.PlanDeVille;
import org.apache.project.xml.Deserialisateur;
import org.junit.Test;

public class TestAjoutLivraison {

	
	@Test(timeout = 9000)
	public void testAjoutSimple() {
		File xml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, xml);
	}
}
