package org.apache.project.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.project.modele.DemandeDeLivraison;
import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlanDeVille;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Deserialisateur {
	
	public static void chargerPlanVille(PlanDeVille plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurXML.getInstance().ouvreFichier(true);
		chargerPlanDeVilleFichier(plan, xml);
	}
	
	public static void chargerPlanDeVilleFichier(PlanDeVille plan, File xml) throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
           construirePlanVille(racine, plan);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}
	
	public static void chargerDemandeLivraison(DemandeDeLivraison demande, PlanDeVille plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurXML.getInstance().ouvreFichier(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
        	construireDemandeLivraison(racine, demande, plan);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}

	// TODO : Gérer les erreurs
    private static void construirePlanVille(Element noeudDOMRacine, PlanDeVille plan) throws ExceptionXML, NumberFormatException{
    	NodeList listeIntersections = noeudDOMRacine.getElementsByTagName("noeud");
    	for(int i = 0; i < listeIntersections.getLength(); i++) {
    		construireIntersection((Element)listeIntersections.item(i), plan);
    	}
    	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
    	for(int i = 0; i < listeTroncons.getLength(); i++) {
    		construireTroncon((Element)listeTroncons.item(i), plan);
    	}
    }
    
    // TODO : Gérer les erreurs
    private static void construireDemandeLivraison(Element noeudDOMRacine, DemandeDeLivraison demande,PlanDeVille plan) throws ExceptionXML, NumberFormatException{
    	Element entrepot = (Element)noeudDOMRacine.getElementsByTagName("entrepot").item(0);
    	// TODO : Formater l'heure convenablement
    	String heureDepart = entrepot.getAttribute("heureDepart");
    	//demande.setHeureDepart(heureDepart);
    	Intersection adresseEntrepot = plan.getIntersectionById(Integer.parseInt(entrepot.getAttribute("adresse")));
    	demande.setAdresseEntrepot(adresseEntrepot);
    	
    	NodeList listeLivraisons = noeudDOMRacine.getElementsByTagName("livraison");
    	for(int i = 0; i<listeLivraisons.getLength(); i++) {
    		construireLivraison((Element)listeLivraisons.item(i), demande, plan);
    	}
    }
    
    // TODO : Gérer les erreurs
    private static void construireIntersection(Element element, PlanDeVille plan) {
    	Long id = Long.parseLong(element.getAttribute("id"));
    	Long x = Long.parseLong(element.getAttribute("x"));
    	Long y = Long.parseLong(element.getAttribute("y"));
    	plan.ajouterIntersection(id, x, y);
    }
    
    // TODO : Gérer les erreurs
    // TODO : les troncons doubles n'existent pas
    private static void construireTroncon(Element element, PlanDeVille plan) {
    	Long destination = Long.parseLong(element.getAttribute("destination"));
    	double longueur = Double.parseDouble(element.getAttribute("longueur"));
    	String nomRue = element.getAttribute("nomRue");
    	Long origine = Long.parseLong(element.getAttribute("origine"));
    	plan.ajouterTroncon(longueur, origine, destination, nomRue);
    }
    
    // TODO : Gérer les erreurs
    // TODO : COMMENT FAIRE LE LIEN
    private static void construireLivraison(Element element, DemandeDeLivraison demande, PlanDeVille plan) {
    	int adresse = Integer.parseInt(element.getAttribute("adresse"));
    	int duree = Integer.parseInt(element.getAttribute("duree"));
    	/*if() {
    		TODO Faire cas o� il y a des plages horaires
    	}*/
    	Livraison uneLivraison= new Livraison(plan.getIntersectionById(adresse), duree);
    	demande.ajouterLivraison(uneLivraison);    	
    }
}
