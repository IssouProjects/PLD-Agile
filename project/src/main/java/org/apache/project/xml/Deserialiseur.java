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
import org.apache.project.modele.Troncon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Deserialiseur {
	
	public static void chargerPlanVille(PlanDeVille plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurXML.getInstance().ouvreFichier(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
           construirePlanVille(racine, plan);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}
	
	public static void chargerDemandeLivraison(DemandeDeLivraison demande) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurXML.getInstance().ouvreFichier(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
        	construireDemandeLivraison(racine, demande);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}

	// TODO : Gérer les erreurs
	// TODO : ajouterIntersection(Intersection) et ajouterTroncon(Troncon) dans PlanDeVille
    private static void construirePlanVille(Element noeudDOMRacine, PlanDeVille plan) throws ExceptionXML, NumberFormatException{
    	NodeList listeIntersections = noeudDOMRacine.getElementsByTagName("noeud");
    	for(int i = 0; i < listeIntersections.getLength(); i++) {
    		Intersection nouvIntersection = construireIntersection((Element)listeIntersections.item(i));
    		plan.ajouterIntersection(nouvIntersection);
    	}
    	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
    	for(int i = 0; i < listeTroncons.getLength(); i++) {
    		Troncon nouvTroncon = construireTroncon((Element)listeTroncons.item(i));
    		plan.ajouterTroncon(nouvTroncon);
    	}
    }
    
    // TODO : Gérer les erreurs
    // TODO : ajouterLivraison(Livraison) dans DemandeDeLivraison
    private static void construireDemandeLivraison(Element noeudDOMRacine, DemandeDeLivraison demande) throws ExceptionXML, NumberFormatException{
    	Element entrepot = (Element)noeudDOMRacine.getElementsByTagName("entrepot").item(0);
    	// TODO : Formater l'heure convenablement
    	String heureDepart = entrepot.getAttribute("heureDepart");
    	NodeList listeLivraisons = noeudDOMRacine.getElementsByTagName("livraison");
    	for(int i = 0; i<listeLivraisons.getLength(); i++) {
    		Livraison nouvLivraison = construireLivraison((Element)listeLivraisons.item(i));
    		demande.ajouterLivraison(nouvLivraison);
    	}
    }
    
    // TODO : Implémenter le constructeur Intersection
    // TODO : Gérer les erreurs
    private static Intersection construireIntersection(Element element) {
    	int id = Integer.parseInt(element.getAttribute("id"));
    	int x = Integer.parseInt(element.getAttribute("x"));
    	int y = Integer.parseInt(element.getAttribute("y"));
    	return new Intersection(id, x, y);
    }
    
    // TODO : Implémenter le constructeur Intersection
    // TODO : Gérer les erreurs
    private static Troncon construireTroncon(Element element) {
    	int dest = Integer.parseInt(element.getAttribute("dest"));
    	double longueur = Double.parseDouble(element.getAttribute("longueur"));
    	String nomRue = element.getAttribute("nomRue");
    	int origine = Integer.parseInt("origine");
    	return new Troncon(origine, dest, nomRue, longueur);
    }
    
    // TODO : Implémenter le constructeur Livraison (celui ci-dessous, sans les init des attributs qui manquent)
    // TODO : Gérer les erreurs
    private static Livraison construireLivraison(Element element) {
    	int adresse = Integer.parseInt(element.getAttribute("adresse"));
    	int duree = Integer.parseInt(element.getAttribute("duree"));
    	return new Livraison(adresse, duree);
    }
}
