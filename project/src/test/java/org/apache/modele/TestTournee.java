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
import org.junit.*;
import org.xml.sax.SAXException;

public class TestTournee {

	@Test(timeout = 1000)
	public void testCalculerTournee() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee(demande.getAdresseEntrepot(), demande.getHeureDepart());
		tournee.calculerTournee(plan, demande);
		
		//Verification de l'ordre et des intersection a livrer
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud(), 1860559399);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud(), 25303807);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud(), 26155540);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud(), 29003879);
		
		//Test duree tournee
		assertEquals(tournee.getDureeTourneeSecondes(), 3198);
	}
	
	@Test(timeout=30000)
	public void testCalculerGrandeTournee() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLgrand20.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonGrand.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee(demande.getAdresseEntrepot(), demande.getHeureDepart());
		tournee.calculerTournee(plan, demande);
		
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud(), 517370427);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud(), 21674814);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud(), 315381991);
		assertEquals((long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud(), 245032683);
		//D'autres lieux, mais impossible de tous les tester
		assertEquals((Long)tournee.getLivraisonsOrdonnees().get(15).getLieuDeLivraison().getIdNoeud(), Long.valueOf("3280925503"));
		assertEquals((Long)tournee.getLivraisonsOrdonnees().get(16).getLieuDeLivraison().getIdNoeud(), Long.valueOf("3840413118"));
		assertEquals((Long)tournee.getLivraisonsOrdonnees().get(17).getLieuDeLivraison().getIdNoeud(), Long.valueOf("2203886701"));
		assertEquals((Long)tournee.getLivraisonsOrdonnees().get(18).getLieuDeLivraison().getIdNoeud(), Long.valueOf("1941822283"));
	}
}
