package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Entrepot;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestDemandeDeLivraison {

	@Test
	public void TestClear() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		assertFalse(demande.getListeLivraison().isEmpty());

		demande.clear();

		assertTrue(demande.getListeLivraison().isEmpty());
	}

	@Test
	public void TestAjoutLivraisons() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une demande
		DemandeDeLivraison demande = new DemandeDeLivraison();

		Intersection A = new Intersection((long) 1, (long) 3, (long) 3);
		Intersection B = new Intersection((long) 2, (long) 1, (long) 2);
		Intersection C = new Intersection((long) 3, (long) 2, (long) 2);

		@SuppressWarnings("deprecation")
		Entrepot entrepot = new Entrepot(A, new Time(8, 0, 0));
		Livraison lB = new Livraison(B);
		Livraison lC = new Livraison(C);

		demande.setEntrepot(entrepot);
		demande.ajouterLivraison(lB);
		demande.ajouterLivraison(lC);

		List<Livraison> livraisons = new ArrayList<Livraison>();
		livraisons = demande.getListeLivraison();

		assertEquals(entrepot, livraisons.get(0));
		assertEquals(lB, livraisons.get(1));
		assertEquals(lC, livraisons.get(2));
	}
}
