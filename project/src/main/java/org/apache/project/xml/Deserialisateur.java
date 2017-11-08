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
import org.apache.project.modele.Entrepot;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.PlanDeVille;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 */
public class Deserialisateur {

	/**
	 * @param plan
	 * @param xml
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */
	public static void chargerPlanDeVilleFichier(PlanDeVille plan, File xml)
			throws ParserConfigurationException, SAXException, IOException, ExceptionXML {

		if (!estUnFichierXML(xml)) {
			throw new ExceptionXML("Document non xml");
		}

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = docBuilder.parse(xml);
		Element racine = document.getDocumentElement();
		if (racine.getNodeName().equals("reseau")) {
			construirePlanVille(racine, plan);
		} else {
			throw new ExceptionXML("Document non conforme");
		}
	}

	/**
	 * @param demande
	 * @param plan
	 * @param xml
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws ExceptionXML
	 */
	public static void chargerDemandeLivraisonFichier(DemandeDeLivraison demande, PlanDeVille plan, File xml)
			throws ParserConfigurationException, SAXException, IOException, NumberFormatException, ExceptionXML {

		if (!estUnFichierXML(xml)) {
			throw new ExceptionXML("Document non xml");
		}

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = docBuilder.parse(xml);
		Element racine = document.getDocumentElement();
		if (racine.getNodeName().equals("demandeDeLivraisons")) {
			construireDemandeLivraison(racine, demande, plan);
		} else {
			throw new ExceptionXML("Document non conforme.");
		}
	}

	/**
	 * @param noeudDOMRacine
	 * @param plan
	 * @throws ExceptionXML
	 * @throws NumberFormatException
	 */
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

	/**
	 * @param noeudDOMRacine
	 * @param demande
	 * @param plan
	 * @throws ExceptionXML
	 * @throws NumberFormatException
	 */
	private static void construireDemandeLivraison(Element noeudDOMRacine, DemandeDeLivraison demande, PlanDeVille plan)
			throws ExceptionXML, NumberFormatException {

		Element entrepot = (Element) noeudDOMRacine.getElementsByTagName("entrepot").item(0);

		if (entrepot.getAttribute("heureDepart").equals("")
				|| !entrepot.getAttribute("heureDepart")
						.matches("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$")
				|| entrepot.getAttribute("adresse").equals("")) {
			throw new ExceptionXML("Document mal forme");
		}

		String heureDepart = entrepot.getAttribute("heureDepart");
		Intersection adresseEntrepot = plan.getIntersectionById(Long.parseLong(entrepot.getAttribute("adresse")));
		Entrepot objEntrepot = new Entrepot(adresseEntrepot, getTimeFromString(heureDepart));
		demande.setEntrepot(objEntrepot);

		NodeList listeLivraisons = noeudDOMRacine.getElementsByTagName("livraison");
		for (int i = 0; i < listeLivraisons.getLength(); i++) {
			construireLivraison((Element) listeLivraisons.item(i), demande, plan);
		}
	}

	/**
	 * @param element
	 * @param plan
	 * @throws ExceptionXML
	 */
	private static void construireIntersection(Element element, PlanDeVille plan) throws ExceptionXML {
		if (element.getAttribute("id") == "" || element.getAttribute("x").equals("")
				|| element.getAttribute("y").equals("") || !element.getAttribute("id").matches("[0-9]+")
				|| !element.getAttribute("x").matches("[0-9]+") || !element.getAttribute("y").matches("[0-9]+")) {
			throw new ExceptionXML("Document mal forme");
		}

		Long id = Long.parseLong(element.getAttribute("id"));
		Long x = Long.parseLong(element.getAttribute("x"));
		Long y = Long.parseLong(element.getAttribute("y"));
		plan.ajouterIntersection(id, x, y);
	}

	// TODO : les troncons doubles n'existent pas
	/**
	 * @param element
	 * @param plan
	 * @throws ExceptionXML
	 */
	private static void construireTroncon(Element element, PlanDeVille plan) throws ExceptionXML {

		if (element.getAttribute("destination").equals("") || element.getAttribute("longueur").equals("")
				|| element.getAttribute("origine").equals("")
				|| !element.getAttribute("destination").matches("^[0-9]+$")
				|| !element.getAttribute("origine").matches("^[0-9]+$")
				|| !element.getAttribute("longueur").matches("^[+-]?([0-9]*[.])?[0-9]+$")) {
			throw new ExceptionXML("Document mal forme");
		}

		Long destination = Long.parseLong(element.getAttribute("destination"));
		double longueur = Double.parseDouble(element.getAttribute("longueur"));
		String nomRue = element.getAttribute("nomRue");
		Long origine = Long.parseLong(element.getAttribute("origine"));
		if (plan.ajouterTroncon(longueur, origine, destination, nomRue) == false)
			throw new ExceptionXML("Document mal forme");
	}

	/**
	 * @param element
	 * @param demande
	 * @param plan
	 * @throws ExceptionXML
	 *             exception lancée si le document n'est pas correctement formé.
	 */
	@SuppressWarnings("deprecation")
	private static void construireLivraison(Element element, DemandeDeLivraison demande, PlanDeVille plan)
			throws ExceptionXML {

		if (element.getAttribute("adresse").equals("") || element.getAttribute("duree").equals("")
				|| !element.getAttribute("duree").matches("[0-9]+")) {
			throw new ExceptionXML("Document mal forme");
		}

		if (plan.getIntersectionById(Long.valueOf(element.getAttribute("adresse"))) == null) {
			throw new ExceptionXML("Le plan et la demande de livraison ne correspondent pas");
		}

		Long adresse = Long.parseLong(element.getAttribute("adresse"));
		int duree = Integer.parseInt(element.getAttribute("duree"));
		Livraison uneLivraison = new Livraison(plan.getIntersectionById(adresse), duree);
		String debutPlage = element.getAttribute("debutPlage");

		if (debutPlage != null && !debutPlage.isEmpty()) {

			if (element.getAttribute("debutPlage").equals("") || element.getAttribute("finPlage").equals("")
					|| !element.getAttribute("debutPlage")
							.matches("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$")
					|| !element.getAttribute("finPlage")
							.matches("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$")) {
				throw new ExceptionXML("Document mal forme");
			}

			Time debut = getTimeFromString(element.getAttribute("debutPlage"));
			Time fin = getTimeFromString(element.getAttribute("finPlage"));

			int tempsDebut = debut.getHours() * 36000 + debut.getMinutes() * 60 + debut.getSeconds();
			int tempsFin = fin.getHours() * 36000 + fin.getMinutes() * 60 + fin.getSeconds();

			if ((tempsDebut + duree) > tempsFin) {
				throw new ExceptionXML("Document mal forme");
			}

			PlageHoraire ph = new PlageHoraire(debut, fin);
			uneLivraison.setPlageHoraire(ph);
		}
		demande.ajouterLivraison(uneLivraison);
	}

	/**
	 * @param xml
	 * @return
	 */
	private static boolean estUnFichierXML(File xml) {
		String fileName = xml.getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
			extension = fileName.substring(i + 1);
		}

		return "xml".equals(extension);
	}

	// TODO put in Util.java class?
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
