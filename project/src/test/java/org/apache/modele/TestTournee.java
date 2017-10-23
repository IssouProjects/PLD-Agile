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
		assertEquals(1860559399, (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		
		//Test duree tournee
		assertEquals(3221, tournee.getDureeTourneeSecondes());
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
		/*
		assertEquals(517370427, (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(21674814, (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(315381991, (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(245032683, (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		//D'autres lieux, mais impossible de tous les tester
		//assertEquals(Long.valueOf("3280925503"), 
		System.out.println((Long)tournee.getLivraisonsOrdonnees().get(16).getLieuDeLivraison().getIdNoeud());
		//assertEquals(Long.valueOf("4136115322"), 
		System.out.println((Long)tournee.getLivraisonsOrdonnees().get(17).getLieuDeLivraison().getIdNoeud());
		//assertEquals(Long.valueOf("3840413118"), 
		System.out.println((Long)tournee.getLivraisonsOrdonnees().get(18).getLieuDeLivraison().getIdNoeud());
		
		//Duree de la tournee ok
		assertEquals(15593, tournee.getDureeTourneeSecondes());
		*/
	}
}
