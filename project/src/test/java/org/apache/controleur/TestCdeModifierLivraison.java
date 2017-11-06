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

	private PlanDeVille plan;
	private Tournee tourneeAvantCommande;
	private Tournee tourneeModifiee;
	private Tournee tourneeApresCommande;
	private ListeDeCommandes commandes;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
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
		
		this.plan = plan;
		this.commandes = new ListeDeCommandes();

		// Tournee originale sans modification
		this.tourneeAvantCommande = new Tournee();
		this.tourneeAvantCommande.setEntrepot(demande1.getEntrepot());
		this.tourneeAvantCommande.calculerTournee(plan, demande1);
		
		// Tournee qui sera manipulee avec les commandes
		this.tourneeModifiee = new Tournee();
		this.tourneeModifiee.setEntrepot(demande2.getEntrepot());
		this.tourneeModifiee.calculerTournee(plan, demande2);
		
		// Tournee apres execution commande
		this.tourneeApresCommande = new Tournee();
		this.tourneeApresCommande.setEntrepot(demande3.getEntrepot());
		this.tourneeApresCommande.calculerTournee(plan, demande3);
		
		// Ajout classique de la nouvelle livraison pour comparer apres redo 
		tourneeApresCommande.getLivraison(2).setPlageHoraire(new PlageHoraire(new Time(10,30,0), new Time(11,0,0)));
	}

	@Test
	public void TestUndoRedoCommande() {	
		// Ajout de cette nouvelle livraison a la tournee
		commandes.ajouteCommande(new CdeModifierLivraison(tourneeModifiee.getLivraison(2), new Time(10,30,0), new Time(11,0,0)));
		
		// La commande s'est bien executee
		assertEquals("10:30:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getDebut().toString());
		assertEquals("11:00:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getFin().toString());
		
		// On undo cette action
		commandes.undo();
		
		// On verifie que l'on est bien revenu a l'etat avant l'ajout de la livraison
		assertNull(tourneeModifiee.getLivraison(2).getPlageHoraire());
		
		// On refait cette action
		commandes.redo();
		
		// On verifie que l'on est bien revenu a l'etat avec la nouvelle livraison
		assertEquals("10:30:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getDebut().toString());
		assertEquals("11:00:00", tourneeModifiee.getLivraison(2).getPlageHoraire().getFin().toString());
	}

}
