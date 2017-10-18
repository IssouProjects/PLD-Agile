package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class TestTournee extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculerTournee() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		//Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
		
		//Calcul tournee
		Tournee tournee = new Tournee(demande.getAdresseEntrepot(), demande.getHeureDepart());
		tournee.calculerTournee(plan, demande);
		
		//Verification de l'ordre et des intersection a livrer
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud(), 1860559399);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud(), 25303807);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud(), 26155540);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud(), 29003879);
	}

}
