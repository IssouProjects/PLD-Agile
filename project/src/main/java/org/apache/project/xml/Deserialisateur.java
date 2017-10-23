package org.apache.project.xml;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Deserialisateur {

	public static void chargerPlanVille(PlanDeVille plan)
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = OuvreurXML.getInstance().ouvreFichier(true);
		chargerPlanDeVilleFichier(plan, xml);
	}

	public static void chargerPlanDeVilleFichier(PlanDeVille plan, File xml)
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
			
		if(!estUnFichierXML(xml)) {
			throw new ExceptionXML("Document non xml");
		}
		
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = docBuilder.parse(xml);
		Element racine = document.getDocumentElement();
		if (racine.getNodeName().equals("reseau")) {
			construirePlanVille(racine, plan);
		} else
			throw new ExceptionXML("Document non conforme");
	}

	public static void chargerDemandeLivraison(DemandeDeLivraison demande, PlanDeVille plan)
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		File xml = OuvreurXML.getInstance().ouvreFichier(true);
		chargerDemandeLivraisonFichier(demande, plan, xml);
	}

	public static void chargerDemandeLivraisonFichier(DemandeDeLivraison demande, PlanDeVille plan, File xml)
			throws ParserConfigurationException, SAXException, IOException, NumberFormatException, ExceptionXML {
		
		if(!estUnFichierXML(xml)) {
			throw new ExceptionXML("Document non xml");
		}
		
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = docBuilder.parse(xml);
		Element racine = document.getDocumentElement();
		if (racine.getNodeName().equals("demandeDeLivraisons")) {
			construireDemandeLivraison(racine, demande, plan);
		} else
			throw new ExceptionXML("Document non conforme");
	}

	// TODO : Gérer les erreurs
	private static void construirePlanVille(Element noeudDOMRacine, PlanDeVille plan)
			throws ExceptionXML, NumberFormatException {
		NodeList listeIntersections = noeudDOMRacine.getElementsByTagName("noeud");
		for (int i = 0; i < listeIntersections.getLength(); i++) {
			construireIntersection((Element) listeIntersections.item(i), plan);
		}
		NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
		for (int i = 0; i < listeTroncons.getLength(); i++) {
			construireTroncon((Element) listeTroncons.item(i), plan);
		}
	}

	// TODO : Gérer les erreurs
	private static void construireDemandeLivraison(Element noeudDOMRacine, DemandeDeLivraison demande, PlanDeVille plan)
			throws ExceptionXML, NumberFormatException {

		Element entrepot = (Element) noeudDOMRacine.getElementsByTagName("entrepot").item(0);
		String heureDepart = entrepot.getAttribute("heureDepart");
		demande.setHeureDepart(getTimeFromString(heureDepart));
		Intersection adresseEntrepot = plan.getIntersectionById(Long.parseLong(entrepot.getAttribute("adresse")));
		demande.setAdresseEntrepot(adresseEntrepot);

		NodeList listeLivraisons = noeudDOMRacine.getElementsByTagName("livraison");
		for (int i = 0; i < listeLivraisons.getLength(); i++) {
			construireLivraison((Element) listeLivraisons.item(i), demande, plan);
		}
	}

	// TODO : Gérer les erreurs
	private static void construireIntersection(Element element, PlanDeVille plan) throws ExceptionXML {
		if(element.getAttribute("id") == "" || element.getAttribute("x") == "" || element.getAttribute("y") == "") {
			throw new ExceptionXML("Document mal forme");
		}
		
		Long id = Long.parseLong(element.getAttribute("id"));
		Long x = Long.parseLong(element.getAttribute("x"));
		Long y = Long.parseLong(element.getAttribute("y"));
		plan.ajouterIntersection(id, x, y);
	}

	// TODO : Gérer les erreurs
	// TODO : les troncons doubles n'existent pas
	private static void construireTroncon(Element element, PlanDeVille plan) throws ExceptionXML {
		
		if(element.getAttribute("destination") == "" || element.getAttribute("longueur") == "" || element.getAttribute("origine") == "") {
			throw new ExceptionXML("Document mal forme");
		}
		
		Long destination = Long.parseLong(element.getAttribute("destination"));
		double longueur = Double.parseDouble(element.getAttribute("longueur"));
		String nomRue = element.getAttribute("nomRue");
		Long origine = Long.parseLong(element.getAttribute("origine"));
		plan.ajouterTroncon(longueur, origine, destination, nomRue);
	}

	// TODO : Gérer les erreurs
	// TODO : Gerer cas erreur y a un debut mais pas de fin de plage horaire
	private static void construireLivraison(Element element, DemandeDeLivraison demande, PlanDeVille plan) {
		Long adresse = Long.parseLong(element.getAttribute("adresse"));
		int duree = Integer.parseInt(element.getAttribute("duree"));
		Livraison uneLivraison = new Livraison(plan.getIntersectionById(adresse), duree);
		String debutPlage = element.getAttribute("debutPlage");
		if (debutPlage != null && !debutPlage.isEmpty()) {
			Time debut = getTimeFromString(element.getAttribute("debutPlage"));
			Time fin = getTimeFromString(element.getAttribute("finPlage"));
			PlageHoraire ph = new PlageHoraire(debut, fin);
			uneLivraison.setPlageHoraire(ph);
		}
		demande.ajouterLivraison(uneLivraison);
	}
	
	private static boolean estUnFichierXML(File xml) {
		String fileName = xml.getName();
		String extension = "";
		
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}

		return "xml".equals(extension);
	}

	/**
	 * Transforme une heure au format "hour:min:sec" en un objet Time
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Time getTimeFromString(String time) {
		List<String> hmsListStr = Arrays.asList(time.split(":"));
		int hours = Integer.parseInt(hmsListStr.get(0));
		int minutes = Integer.parseInt(hmsListStr.get(1));
		int seconds = Integer.parseInt(hmsListStr.get(2));
		return new Time(hours, minutes, seconds);
	}
}
