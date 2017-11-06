package org.apache.controleur;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Time;

import org.apache.project.controleur.CdeModifierLivraison;
import org.apache.project.controleur.ListeDeCommandes;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.junit.Before;
import org.junit.Test;

public class TestCdeModifierLivraison {

	@SuppressWarnings("deprecation")
	@Test
	public void TestModifierLivraison() throws Exception {
		// Creation d'une tournee
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		
		DemandeDeLivraison demande1 = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande1, plan, xml);
		
		DemandeDeLivraison demande2 = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande2, plan, xml);
		
		DemandeDeLivraison demande3 = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande3, plan, xml);
		
		ListeDeCommandes commandes = new ListeDeCommandes();

		// Tournee originale sans modification
		Tournee tourneeAvantCommande = new Tournee();
		tourneeAvantCommande.setEntrepot(demande1.getEntrepot());
		tourneeAvantCommande.calculerTournee(plan, demande1);
		
		// Tournee qui sera manipulee avec les commandes
		Tournee tourneeModifiee = new Tournee();
		tourneeModifiee.setEntrepot(demande2.getEntrepot());
		tourneeModifiee.calculerTournee(plan, demande2);
		
		// Tournee apres execution commande
		Tournee tourneeApresCommande = new Tournee();
		tourneeApresCommande.setEntrepot(demande3.getEntrepot());
		tourneeApresCommande.calculerTournee(plan, demande3);
		
		// Modification de la plage horaire de la livraison pour comparer apres redo 
		tourneeApresCommande.getLivraison(2).setPlageHoraire(new PlageHoraire(new Time(10,30,0), new Time(11,0,0)));
		
		// Modification de la livraison avec la commande
		commandes.ajouteCommande(new CdeModifierLivraison(tourneeModifiee.getLivraison(2), new Time(10,30,0), new Time(11,0,0)));
		
		// La commande s'est bien executee
		assertEquals("10:30:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getDebut().toString());
		assertEquals("11:00:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getFin().toString());
		
		// On undo cette action
		commandes.undo();
		
		// On verifie que l'on est bien revenu a l'etat avant la modification de la livraison
		assertNull(tourneeModifiee.getLivraison(2).getPlageHoraire());
		
		// On refait cette action
		commandes.redo();
		
		// On verifie que l'on est bien revenu a l'etat avec la plage horaire
		assertEquals("10:30:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getDebut().toString());
		assertEquals("11:00:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getFin().toString());
	}

}
