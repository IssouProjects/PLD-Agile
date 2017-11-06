package org.apache.controleur;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Time;

import org.apache.project.controleur.CdeAjouterLivraison;
import org.apache.project.controleur.ListeDeCommandes;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.junit.Before;
import org.junit.Test;

public class TestCdeAjouterLivraison {
	
	private PlanDeVille plan;
	private Tournee tourneeAvantCommande;
	private Tournee tourneeModifiee;
	private Tournee tourneeApresCommande;
	private Livraison nouvelleLivraison;
	private ListeDeCommandes commandes;

	@Before
	public void setUp() throws Exception {
		// Creation d'une tournee
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit5.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
		
		this.plan = plan;
		this.commandes = new ListeDeCommandes();

		// Tournee originale sans modification
		this.tourneeAvantCommande = new Tournee();
		this.tourneeAvantCommande.setEntrepot(demande.getEntrepot());
		this.tourneeAvantCommande.calculerTournee(plan, demande);
		
		// Tournee qui sera manipulee avec les commandes
		this.tourneeModifiee = new Tournee();
		this.tourneeModifiee.setEntrepot(demande.getEntrepot());
		this.tourneeModifiee.calculerTournee(plan, demande);
		
		// Tournee apres execution commande
		this.tourneeApresCommande = new Tournee();
		this.tourneeApresCommande.setEntrepot(demande.getEntrepot());
		this.tourneeApresCommande.calculerTournee(plan, demande);
		
		// Creation d'une nouvelle livraison
		Intersection intersectionNvLivraison = plan.getIntersectionById((long)26155368);
		PlageHoraire plage = new PlageHoraire(new Time(8,0,0), new Time(8,30,0));
		nouvelleLivraison = new Livraison(intersectionNvLivraison, 60, plage);
		
		// Ajout classique de la nouvelle livraison pour comparer apres redo 
		tourneeApresCommande.ajouterNouvelleLivraison(plan, nouvelleLivraison, tourneeModifiee.getLivraison(1));
	}

	@Test
	public void TestUndoRedoCommande() {		
		// Ajout de cette nouvelle livraison a la tournee
		commandes.ajouteCommande(new CdeAjouterLivraison(plan, tourneeModifiee, nouvelleLivraison, tourneeModifiee.getLivraison(1)));
		
		// La commande s'est bien executee
		assertEquals("[De 25321357 à 1860559399, De 1860559399 à 26155368, De 26155368 à 25303807, De 25303807 à 26155540, De 26155540 à 29003879, De 29003879 à 25321357]", tourneeModifiee.getChemins().toString());
		
		// On undo cette action
		commandes.undo();
		
		// On verifie que l'on est bien revenu a l'etat avant l'ajout de la livraison
		assertEquals(tourneeAvantCommande.getChemins().toString(), tourneeModifiee.getChemins().toString());
		assertEquals(tourneeAvantCommande.getDureeTourneeSecondes(), tourneeModifiee.getDureeTourneeSecondes());
		assertEquals(tourneeAvantCommande.getEntrepot().toString(), tourneeModifiee.getEntrepot().toString());
		assertEquals(tourneeAvantCommande.getLivraisonsOrdonnees().toString(), tourneeModifiee.getLivraisonsOrdonnees().toString());
		
		// On refait cette action
		commandes.redo();
		
		// On verifie que l'on est bien revenu a l'etat avec la nouvelle livraison
		assertEquals(tourneeApresCommande.getChemins().toString(), tourneeModifiee.getChemins().toString());
		assertEquals(tourneeApresCommande.getDureeTourneeSecondes(), tourneeModifiee.getDureeTourneeSecondes());
		assertEquals(tourneeApresCommande.getEntrepot().toString(), tourneeModifiee.getEntrepot().toString());
		assertEquals(tourneeApresCommande.getLivraisonsOrdonnees().toString(), tourneeModifiee.getLivraisonsOrdonnees().toString());
	}
}
