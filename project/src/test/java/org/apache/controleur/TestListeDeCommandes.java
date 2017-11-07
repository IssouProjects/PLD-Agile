package org.apache.controleur;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.controleur.CdeEchangerLivraison;
import org.apache.project.controleur.CdeSupprimerLivraison;
import org.apache.project.controleur.ListeDeCommandes;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestListeDeCommandes {

	@Test
	public void TestCommandes() throws NumberFormatException, ParserConfigurationException, SAXException, IOException, ExceptionXML {
		ListeDeCommandes commandes = new ListeDeCommandes();
		
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.calculerTournee(plan, demande, 10000);
		
		assertNull(commandes.getFirstUndoableCommand());
		assertNull(commandes.getFirstRedoableCommand());
		
		commandes.ajouteCommande(new CdeSupprimerLivraison(plan, tournee, tournee.getLivraison(2)));
		commandes.ajouteCommande(new CdeEchangerLivraison(plan, tournee, tournee.getLivraison(1), 2));
		
		assertNotNull(commandes.getFirstUndoableCommand());
		assertNull(commandes.getFirstRedoableCommand());
		
		commandes.undo();
		
		assertNotNull(commandes.getFirstUndoableCommand());
		assertNotNull(commandes.getFirstRedoableCommand());
		
		commandes.clearCommandes();
		
		assertNull(commandes.getFirstUndoableCommand());
		assertNull(commandes.getFirstRedoableCommand());
	}

}
