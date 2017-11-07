package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.Chemin;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.*;
import org.xml.sax.SAXException;

public class TestTournee {

	@SuppressWarnings("deprecation")
	@Test(timeout = 1000)
	public void testCalculerTourneePerso()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit4.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();

		tournee.setEntrepot(demande.getEntrepot());

		assertEquals(0, (long)tournee.getEntrepot().getLieuDeLivraison().getIdNoeud());
			
		tournee.calculerTournee(plan, demande, 10000);
		List<Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();

		// Tests 100% sur si distance en m et duree en s
		assertEquals(480, chemins.get(0).getDuree());

		assertEquals(new Time(8, 8, 0), tournee.getLivraisonsOrdonnees().get(1).getHeureArrivee());
		assertEquals(480, chemins.get(1).getDuree());

		assertEquals(new Time(8, 16, 0), tournee.getLivraisonsOrdonnees().get(2).getHeureArrivee());
		assertEquals(3600, chemins.get(2).getDuree());

		assertEquals(new Time(9, 16, 0), tournee.getLivraisonsOrdonnees().get(3).getHeureArrivee());
		assertEquals(2760, chemins.get(3).getDuree());

		assertEquals(7320, tournee.getDureeTourneeSecondes());
	}

	@SuppressWarnings("deprecation")
	@Test(timeout = 1000)
	public void testCalculerTourneeHorairePerso()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit4TW1.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();

		tournee.setEntrepot(demande.getEntrepot());
		assertEquals(0, (long)tournee.getEntrepot().getLieuDeLivraison().getIdNoeud());
			
		tournee.calculerTournee(plan, demande, 10000);
		List<Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();

		// Tests 100% sur si distance en m et duree en s
		assertEquals(480, chemins.get(0).getDuree());

		// Doit attendre 10h pour livrer
		assertEquals(new Time(8, 8, 0), tournee.getLivraisonsOrdonnees().get(1).getHeureArrivee());
		assertEquals(480, chemins.get(1).getDuree());

		assertEquals(new Time(10, 8, 0), tournee.getLivraisonsOrdonnees().get(2).getHeureArrivee());
		assertEquals(3600, chemins.get(2).getDuree());

		assertEquals(new Time(11, 8, 0), tournee.getLivraisonsOrdonnees().get(3).getHeureArrivee());
		assertEquals(2760, chemins.get(3).getDuree());

		// 7320 + 3720 (112 * 60)
		assertEquals(14040, tournee.getDureeTourneeSecondes());
	}
	
	//TODO A REFAIRE
	/*
	@Test
	public void testNomRue()throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLpetit4.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planPetit.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
				
		// Calcul tournee
		Tournee tournee = new Tournee();
				
		tournee.setEntrepot(demande.getEntrepot());
		
		tournee.calculerTournee(plan, demande, 10000);
		List<Chemin> chemins = new ArrayList<Chemin>();
		chemins = tournee.getChemins();
		
		assertEquals("Rue du Docteur Bonhomme", chemins.get(0).getListeRues().get(0));
		assertEquals("Rue Maryse Bastié", chemins.get(0).getListeRues().get(1));
		
		assertEquals("Avenue des Frères Lumière", chemins.get(1).getListeRues().get(0));
		
		assertEquals("Rue Maryse Bastié", chemins.get(2).getListeRues().get(0));
		assertEquals("Cours Albert Thomas", chemins.get(2).getListeRues().get(1));
		assertEquals("Rue Charles Richard", chemins.get(2).getListeRues().get(2));
		
		assertEquals("Place du Château", chemins.get(3).getListeRues().get(0));
		assertEquals("", chemins.get(3).getListeRues().get(1));
		assertEquals("Cours Albert Thomas", chemins.get(3).getListeRues().get(2));
		assertEquals("", chemins.get(3).getListeRues().get(3));
	}*/
	
	@Test(timeout = 1000)
	public void testCalculerTourneeDonnee()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
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

		// Verification de l'ordre et des intersection a livrer
		assertEquals(25321357, (long) tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long) tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long) tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long) tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long) tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());

		// Test duree tournee
		// WARNING: Test de non regression -> cette valeur n'est pas 100% sûr
		assertEquals(4118, tournee.getDureeTourneeSecondes());

		// Verification que l'ajout de l'entrepot en derniere position n'impacte pas la
		// duree
		tournee.ajouterListeLivraison(demande.getEntrepot());
		assertEquals(25321357, (long) tournee.getLivraisonsOrdonnees().get(5).getLieuDeLivraison().getIdNoeud());

		tournee.calculerDureeTotale();
		// WARNING: Test de non regression -> cette valeur n'est pas 100% sûr
		assertEquals(4118, tournee.getDureeTourneeSecondes());
	}

	@Test(timeout = 30000)
	public void testCalculerGrandeTournee()
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLgrand20.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonGrand.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);

		// Calcul tournee
		Tournee tournee = new Tournee();
		tournee.setEntrepot(demande.getEntrepot());
		tournee.calculerTournee(plan, demande, 10000);
		/*
		 * assertEquals(517370427,
		 * (long)tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud
		 * ()); assertEquals(21674814,
		 * (long)tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud
		 * ()); assertEquals(315381991,
		 * (long)tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud
		 * ()); assertEquals(245032683,
		 * (long)tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud
		 * ()); //D'autres lieux, mais impossible de tous les tester
		 * //assertEquals(Long.valueOf("3280925503"),
		 * System.out.println((Long)tournee.getLivraisonsOrdonnees().get(16).
		 * getLieuDeLivraison().getIdNoeud());
		 * //assertEquals(Long.valueOf("4136115322"),
		 * System.out.println((Long)tournee.getLivraisonsOrdonnees().get(17).
		 * getLieuDeLivraison().getIdNoeud());
		 * //assertEquals(Long.valueOf("3840413118"),
		 * System.out.println((Long)tournee.getLivraisonsOrdonnees().get(18).
		 * getLieuDeLivraison().getIdNoeud());
		 * 
		 * //Duree de la tournee ok assertEquals(15593,
		 * tournee.getDureeTourneeSecondes());
		 */
	}

	@Test
	public void TestClear() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une tournee
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

		assertNotNull(tournee.getEntrepot());
		assertNotNull(tournee.getEntrepot().getHeureDepart());
		assertFalse(tournee.getChemins().isEmpty());
		assertFalse(tournee.getLivraisonsOrdonnees().isEmpty());
		assertNotEquals(0, tournee.getDureeTourneeSecondes());

		tournee.clear();

		assertNull(tournee.getEntrepot());
		assertTrue(tournee.getChemins().isEmpty());
		assertTrue(tournee.getLivraisonsOrdonnees().isEmpty());
		assertEquals(0, tournee.getDureeTourneeSecondes());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestAjouterNouvelleLivraison()
			throws NumberFormatException, ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une tournee
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
		
		int ancienneDureeTournee = tournee.getDureeTourneeSecondes();
		Livraison ancienneLivraison = tournee.getLivraison(3);
		String ancienneHeurePassageLivraison = tournee.getLivraison(3).getHeureArrivee().toString();

		// Creation d'une nouvelle livraison
		Intersection intersectionNvLivraison = plan.getIntersectionById((long) 26155368);

		PlageHoraire plage = new PlageHoraire(new Time(8, 0, 0), new Time(8, 30, 0));

		Livraison nouvelleLivraison = new Livraison(intersectionNvLivraison, 60, plage);

		// Ajout de cette nouvelle livraison a la tournee
		tournee.ajouterNouvelleLivraison(plan, nouvelleLivraison, tournee.getLivraison(1));

		// Verification des informations de la nouvelle livraison dans la tournee
		assertEquals(60, tournee.getLivraison(2).getDuree());
		assertEquals((long) 26155368, (long) tournee.getLivraison(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(plage, tournee.getLivraison(2).getPlageHoraire());

		assertEquals((long) 25321357, (long) tournee.getEntrepot().getLieuDeLivraison().getIdNoeud()); // Entrepot
																										// inchange

		assertEquals(6, tournee.getLivraisonsSize()); // Nombre de livraisons ok

		assertEquals(4437, tournee.getDureeTourneeSecondes());
		assertTrue(tournee.getDureeTourneeSecondes() > ancienneDureeTournee);

		// Verification de l'heure de passage
		assertEquals("08:26:04", tournee.getLivraison(2).getHeureArrivee().toString());

		// Verification de la repercussion de la nouvelle livraison sur les heures de
		// passages des suivantes
		assertEquals(ancienneLivraison, tournee.getLivraison(4)); // L'ancienne livraison a ete decalee de 1
		assertTrue(tournee.getLivraison(4).getHeureArrivee().toString() != ancienneHeurePassageLivraison);
		assertEquals("08:35:08", tournee.getLivraison(4).getHeureArrivee().toString());

		// Verification de l'itineraire emprunte
		// De la livraison precdente a la nouvelle livraison...
		assertEquals("ID: 1860559399 X:20437 Y:23772", tournee.getChemin(1).getDebut().toString());
		assertEquals("ID: 26155368 X:23009 Y:33904", tournee.getChemin(1).getFin().toString());

		// On verifie que ca passe par la nouvelle livraison
		assertEquals((long) tournee.getLivraison(2).getLieuDeLivraison().getIdNoeud(),
				(long) tournee.getChemin(1).getFin().getIdNoeud());
		assertEquals((long) tournee.getLivraison(2).getLieuDeLivraison().getIdNoeud(),
				(long) tournee.getChemin(2).getDebut().getIdNoeud());

		// Et de la nouvelle livraison a la suivante
		assertEquals("ID: 26155368 X:23009 Y:33904", tournee.getChemin(2).getDebut().toString());
		assertEquals("ID: 25303807 X:19933 Y:32576", tournee.getChemin(2).getFin().toString());

		assertEquals("[Départ de l'entrepôt: 08:00, Livraison 1\n" +
		          	"	Heure d'arrivée: 08:05\n" +
		        	"	Pas de plage horaire\n" +
				"	Durée sur place: 15min, Livraison 2\n" +
		        	"	Heure d'arrivée: 08:26\n" +
		        	"	Plage horaire: 08:00 - 08:30\n" + 
				"	Durée sur place: 1min, Livraison 3\n" +
		        	"	Heure d'arrivée: 08:29\n" +
		        	"	Pas de plage horaire\n" +
				"	Durée sur place: 5min, Livraison 4\n" +
		        	"	Heure d'arrivée: 08:35\n" +
		        	"	Pas de plage horaire\n" +
				"	Durée sur place: 15min, Livraison 5\n" +
		        	"	Heure d'arrivée: 08:52\n" + 
		        	"	Pas de plage horaire\n" + 
				"	Durée sur place: 15min]", tournee.getLivraisonsOrdonnees().toString());
	}

	@Test
	public void TestSupprimerLivraison() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une tournee
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
		
		// Etat de la tournee avant suppression
		int dureeAncienneTournee = tournee.getDureeTourneeSecondes();

		// Suppression tournee
		tournee.supprimerLivraison(plan, 2);

		assertEquals(4, tournee.getLivraisonsSize());

		assertTrue(tournee.getDureeTourneeSecondes() < dureeAncienneTournee);
		assertEquals(3818, tournee.getDureeTourneeSecondes());

		assertEquals("[Départ de l'entrepôt: 08:00, Livraison 1\n"
				+ "	Heure d'arrivée: 08:05\n" 
				+ "	Pas de plage horaire\n"
				+ "	Durée sur place: 15min, Livraison 2\n"
				+ "	Heure d'arrivée: 08:24\n" 
				+ "	Pas de plage horaire\n"
				+ "	Durée sur place: 15min, Livraison 3\n"
				+ "	Heure d'arrivée: 08:41\n"
				+ "	Pas de plage horaire\n"
				+ "	Durée sur place: 15min]", tournee.getLivraisonsOrdonnees().toString());

		assertEquals(
				"[De 25321357 à 1860559399, De 1860559399 à 26155540, De 26155540 à 29003879, De 29003879 à 25321357]",
				tournee.getChemins().toString());
	}

	@Test
	public void TestDeplacerLivraison() throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		// Creation d'une tournee
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
		
		// Verification de l'ordre et des intersection a livrer
		assertEquals(25321357, (long) tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long) tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long) tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long) tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long) tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());

		// On deplace la 2e livraison en 1ere position
		tournee.deplacerLivraison(plan, tournee.getLivraison(2), 1);

		// Verification
		assertEquals(25321357, (long) tournee.getLivraisonsOrdonnees().get(0).getLieuDeLivraison().getIdNoeud());
		assertEquals(25303807, (long) tournee.getLivraisonsOrdonnees().get(1).getLieuDeLivraison().getIdNoeud());
		assertEquals(1860559399, (long) tournee.getLivraisonsOrdonnees().get(2).getLieuDeLivraison().getIdNoeud());
		assertEquals(26155540, (long) tournee.getLivraisonsOrdonnees().get(3).getLieuDeLivraison().getIdNoeud());
		assertEquals(29003879, (long) tournee.getLivraisonsOrdonnees().get(4).getLieuDeLivraison().getIdNoeud());
	}

}
