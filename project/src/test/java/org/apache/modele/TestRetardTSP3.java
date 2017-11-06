package org.apache.modele;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.PlanDeVille;
import org.apache.project.modele.Tournee;
import org.apache.project.xml.Deserialisateur;
import org.apache.project.xml.ExceptionXML;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestRetardTSP3 {


	@SuppressWarnings("deprecation")
	@Test(timeout = 1000)
	public void testRetardTSP3() throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		// Creation des objets plan et demande
		File xml = new File("src/test/java/org/apache/modele/fichiers/DLmoyen5TW4.xml");
		File planxml = new File("src/test/java/org/apache/modele/fichiers/planLyonMoyen.xml");
		PlanDeVille plan = new PlanDeVille();
		Deserialisateur.chargerPlanDeVilleFichier(plan, planxml);
		DemandeDeLivraison demande = new DemandeDeLivraison();
		Deserialisateur.chargerDemandeLivraisonFichier(demande, plan, xml);
		
		// Calcul tournee
		Tournee tournee = new Tournee();
		
		tournee.setEntrepot(demande.getEntrepot());
		
		//execution TSP3
		tournee.calculerTournee(plan, demande);
		List<Livraison> livraisons = new ArrayList<Livraison>();
		livraisons = tournee.getLivraisonsOrdonnees();
		
		for(Livraison livraison: livraisons)
		{
			if(livraison.getPlageHoraire() != null)
			{
				//On verifie uniquement si l on arrive avant la fin de la plage horaire
				assertTrue((livraison.getHeureArrivee().getTime() + (long) (livraison.getDuree()*1000)) <= livraison.getPlageHoraire().getFin().getTime());
			}
		}
		
	}

}
