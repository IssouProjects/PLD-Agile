package org.apache.project.modele;
import java.util.*;

public class Astar {
	
	Map <Long, noeud> listeOuverte = new HashMap<>();
	Map <Long, noeud> listeFerme = new HashMap<>();
	
	class noeud {
		public double coutOrigine;
		public double distanceOrigine;
		public double qualite;
		public Long idAncetre;
		public Intersection intersectionActuel;
		
		
		public noeud(double cout_origine,double distance_origine,double qualite,long id_ancetre,Intersection intersection_Actuel)
		{
			this.coutOrigine = cout_origine;
			this.distanceOrigine = distance_origine;
			this.qualite = qualite;
			this.idAncetre = id_ancetre;
			this.intersectionActuel = intersection_Actuel;
		}
		
	
	}
	public List<Chemin> PrincipalAstar(List<Troncon> plan_tronc, List<Intersection> plan_inter, List<Intersection> livraison_inter)
	{
		
		
		
		
		return new ArrayList<>();
	}
	// calcul de la distance à vol d'oiseau entre deux points
	public double distance (int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	public boolean dejaPresent (Map <Long, noeud> listeNoeuds, Intersection uneIntersection)
	{
		 return listeNoeuds.containsKey(uneIntersection.getIdNoeud());
		
	}
	
	
}
