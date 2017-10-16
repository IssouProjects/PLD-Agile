package org.apache.project.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.project.modele.Astar.noeud;

public class Dijkstra {
	
	Map <Long, noeud> listeOuverte = new HashMap<>();
	Map <Long, noeud> listeFermee = new HashMap<>();
	
	class noeud {
		public double coutOrigine;
		public Long idAncetre;
		public Intersection intersectionActuel;
		
		
		public noeud(double cout_origine, long id_ancetre, Intersection intersection_Actuel)
		{
			this.coutOrigine = cout_origine;
			this.idAncetre = id_ancetre;
			this.intersectionActuel = intersection_Actuel;
		}
	
	}
	
	public List<Chemin> PrincipalDijkstra(List<Intersection> plan_inter, List<Intersection> livraison_inter)
	{
		List<Chemin> ensembleChemins = new ArrayList<>();
		int nombreLivraison = livraison_inter.size();
		for(int i = 0; i < nombreLivraison; i++)
		{
			listeOuverte.clear();
			listeFermee.clear();
			
			Intersection origine = livraison_inter.get(i);
			
			noeud noeudCourant = new noeud((double)0, (long) -1, origine);
			
			listeOuverte.put(noeudCourant.intersectionActuel.getIdNoeud(), noeudCourant);
			ajouterListeFermee(noeudCourant);
			ajouterNoeudAdjacent(noeudCourant);
			
			//s arrete lorsque l on a vu tous les points
			while(!listeOuverte.isEmpty())
			{
				noeudCourant = rechercherMeilleurNoeud();
				ajouterListeFermee(noeudCourant);
				ajouterNoeudAdjacent(noeudCourant);
			}
			
			ajouteChemin(origine, ensembleChemins, livraison_inter);
						
		}
		
		return ensembleChemins;
	}
	
	//Deroule les chemins a partir de la liste fermée
	public void ajouteChemin(Intersection intersectionOrigine,List<Chemin> ensembleChemins,List<Intersection> LivraisonInter)
	{
		int nombreDestination = LivraisonInter.size();
		List<Troncon> listTronconInverse = new ArrayList<>();
		List<Troncon> listTroncon = new ArrayList<>();
		
		for(int i = 0; i < nombreDestination; i++)
		{
			Intersection intersectionCourante = LivraisonInter.get(i);
			Intersection intersectionDestination = intersectionCourante;
			listTronconInverse.clear();
			listTroncon.clear();
			
			if(intersectionCourante.getIdNoeud() != intersectionOrigine.getIdNoeud())
			{	
				while(intersectionCourante != intersectionOrigine)
				{
					Iterator <HashMap.Entry<Long, noeud>> it = listeFermee.entrySet().iterator();
					while(it.hasNext())
					{
						HashMap.Entry<Long, noeud> noeudCourant = it.next();
						if(noeudCourant.getValue().intersectionActuel.getIdNoeud() == intersectionCourante.getIdNoeud())
						{
							listTronconInverse.add(intersectionCourante.getTronconParDestination(intersectionCourante.getIdNoeud()));
							intersectionCourante = obtenirIntersection(noeudCourant.getValue().idAncetre);
							break;
						}
					}
				}
				
				int tailleListe = listTronconInverse.size();
				double distanceLivraison = 0;
				
				//On inverse la liste des troncons, puisue l on est partie de la fin
				for(int j = tailleListe - 1; j >= 0; j++)
				{
					listTroncon.add(listTronconInverse.get(j));
					distanceLivraison += listTronconInverse.get(j).getLongueur();
				}
				
				int dureeLivraison = (int) (distanceLivraison*36)/1500;
				
				//TODO: modifier la duree en fonction de la distance totale des troncons
				ensembleChemins.add(new Chemin(intersectionOrigine, intersectionDestination, dureeLivraison, listTroncon));
			}
		}
	}
	
	//Renvoie une intersection de la liste fermee a partir de son id
	public Intersection obtenirIntersection(Long idIntersection)
	{
		Iterator <HashMap.Entry<Long, noeud>> it = listeFermee.entrySet().iterator();
		while(it.hasNext())
		{
			HashMap.Entry<Long, noeud> noeudCourant = it.next();
			if(noeudCourant.getValue().intersectionActuel.getIdNoeud() == idIntersection)
			{
				return noeudCourant.getValue().intersectionActuel;
			}
		}
		return null;
	}

		
	//Verifie si l intersection est deja presente dans la liste etudie
	public boolean dejaPresent (Map <Long, noeud> listeNoeuds, Intersection uneIntersection)
	{
		 return listeNoeuds.containsKey(uneIntersection.getIdNoeud());
		
	}
	
	//Premet d ajouter les intersections adjacentes a celle actuellement etudiee parmi celles potentiellements etudies
	public void ajouterNoeudAdjacent (noeud noeudActuel)
	{
		Intersection intersectionActuel = noeudActuel.intersectionActuel;
		
		//On utilise un iterateur pour regarder toutes les intersections voisines
		for(Iterator<Troncon> it = intersectionActuel.getTronconsPartants().iterator();it.hasNext();)
		{
			Troncon tronconActuel= it.next();
			Intersection intersectionVoisine = tronconActuel.getIntersectionArrivee();
			
			//Si l intersection voisine est dans la liste fermee, cela ne sert a rien de l etudier plus
			if(!dejaPresent(listeFermee,intersectionVoisine))
			{
				double coutVoisinOrigine = noeudActuel.coutOrigine + tronconActuel.getLongueur();
				// Si l intersection voisine est deja dans la liste ouverte, on la met a jour que si la qualite est meilleure
				if(dejaPresent(listeOuverte,intersectionVoisine))
				{
					if(coutVoisinOrigine < listeOuverte.get(intersectionVoisine.getIdNoeud()).coutOrigine)
					{
						listeOuverte.put(intersectionVoisine.getIdNoeud(), new noeud(coutVoisinOrigine, intersectionActuel.getIdNoeud(), intersectionVoisine));
					}
				}
				// Si l intersection voisine est absente de la liste ouverte, on l ajoute
				else
				{
					listeOuverte.put(intersectionVoisine.getIdNoeud(), new noeud(coutVoisinOrigine, intersectionActuel.getIdNoeud(), intersectionVoisine));
				}
			}
		}
		
	}

	
	public void ajouterListeFermee(noeud noeudCourant)
	{
		listeFermee.put(noeudCourant.intersectionActuel.getIdNoeud(), noeudCourant);
		listeOuverte.remove(noeudCourant.intersectionActuel.getIdNoeud());
	}
	
	
	public noeud rechercherMeilleurNoeud()
	{
		noeud meilleurNoeud = new noeud(Double.MAX_VALUE, -1, new Intersection((long)-2, (long)-2, (long)-2));
		Iterator<HashMap.Entry<Long, noeud>> it = listeOuverte.entrySet().iterator();
		while(it.hasNext())
		{
			HashMap.Entry<Long, noeud> noeudCourant = it.next();
			if(noeudCourant.getValue().coutOrigine <meilleurNoeud.coutOrigine)
			{
				meilleurNoeud = noeudCourant.getValue();
			}
			
		}
		return meilleurNoeud;
	}
		

	
	
}
