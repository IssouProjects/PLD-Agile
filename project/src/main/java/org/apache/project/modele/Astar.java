package org.apache.project.modele;
import java.util.*;

public class Astar {
	
	Map <Long, noeud> listeOuverte = new HashMap<Long, noeud>();
	Map <Long, noeud> listeFermee = new HashMap<Long, noeud>();
	
	class noeud {
		public double coutOrigine;
		public double distanceDestination;
		public double qualite;
		public Long idAncetre;
		public Intersection intersectionActuel;
		
		
		public noeud(double cout_origine,double distance_destination,double qualite,long id_ancetre,Intersection intersection_Actuel)
		{
			this.coutOrigine = cout_origine;
			this.distanceDestination = distance_destination;
			this.qualite = qualite;
			this.idAncetre = id_ancetre;
			this.intersectionActuel = intersection_Actuel;
		}
		
	
	}
	
	public List<Chemin> PrincipalAstar(List<Intersection> plan_inter, List<Intersection> livraison_inter)
	{
		List<Chemin> ensembleChemins = new ArrayList<>();
		int nombreLivraison = livraison_inter.size();
		for(int i = 0; i < nombreLivraison; i++)
		{
			for(int j = 0; j < nombreLivraison; j++)
			{
				if(i != j)
				{
					//On vide les tables remplis pour les anciens chemins
					listeOuverte.clear();
					listeFermee.clear();
					
					Intersection destination = livraison_inter.get(i);
					Intersection origine = livraison_inter.get(j);

					double distanceMini = distance(destination.getCoordX(), destination.getCoordY(), origine.getCoordX(), origine.getCoordY());
					noeud noeudCourant = new noeud((double)0, distanceMini, distanceMini, (long) 0, origine);
					
					listeOuverte.put(noeudCourant.intersectionActuel.getIdNoeud(), noeudCourant);
					ajouterListeFermee(noeudCourant);
					ajouterNoeudAdjacent(noeudCourant, destination);
					
					//bloque si chemin impossible
					while(noeudCourant.intersectionActuel.getIdNoeud() != destination.getIdNoeud())
					{
						noeudCourant = rechercherMeilleurNoeud();
						ajouterListeFermee(noeudCourant);
						ajouterNoeudAdjacent(noeudCourant, destination);
					}
					
					ensembleChemins.add(renvoieChemin(destination, origine));
				}
			}
		}
		
		
		
		return ensembleChemins;
	}
	
	//Renvoie le chemin a partir de la liste fermee lorsque l on a fini le traitement
	public Chemin renvoieChemin(Intersection fin, Intersection debut)
	{
		Intersection intersectionCourante = fin;
		List<Troncon> listTroncon = new ArrayList<>();
		while(intersectionCourante != debut)
		{
			Iterator <HashMap.Entry<Long, noeud>> it = listeFermee.entrySet().iterator();
			while(it.hasNext())
			{
				HashMap.Entry<Long, noeud> noeudCourant = it.next();
				if(noeudCourant.getValue().intersectionActuel.getIdNoeud() == intersectionCourante.getIdNoeud())
				{
					listTroncon.add(intersectionCourante.getTronconParDestination(intersectionCourante.getIdNoeud()));
					intersectionCourante = obtenirIntersection(noeudCourant.getValue().idAncetre);
					break;
				}
			}
		}
		//TODO: modifier la duree en fonction de la distance totale des troncons
		return new Chemin(debut, fin, 0, listTroncon);
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
		
	// calcul de la distance à vol d'oiseau entre deux points
	public double distance (Long x1, Long y1, Long x2, Long y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	//Verifie si l intersection est deja presente dans la liste etudie
	public boolean dejaPresent (Map <Long, noeud> listeNoeuds, Intersection uneIntersection)
	{
		 return listeNoeuds.containsKey(uneIntersection.getIdNoeud());
		
	}
	
	//Premet d ajouter les intersections adjacentes a celle actuellement etudiee parmi celles potentiellements etudies
	public void ajouterNoeudAdjacent (noeud noeudActuel, Intersection destination)
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
				double distanceVoisingDestination = noeudActuel.distanceDestination + distance(intersectionVoisine.getCoordX(), intersectionVoisine.getCoordY(), destination.getCoordX(), destination.getCoordY());
				double qualiteVoisin = coutVoisinOrigine + distanceVoisingDestination;
				// Si l intersection voisine est deja dans la liste ouverte, on la met a jour que si la qualite est meilleure
				if(dejaPresent(listeOuverte,intersectionVoisine))
				{
					if(qualiteVoisin < listeOuverte.get(intersectionVoisine.getIdNoeud()).qualite)
					{
						listeOuverte.put(intersectionVoisine.getIdNoeud(), new noeud(coutVoisinOrigine, distanceVoisingDestination, qualiteVoisin, intersectionActuel.getIdNoeud(), intersectionVoisine));
					}
				}
				// Si l intersection voisine est absente de la liste ouverte, on l ajoute
				else
				{
					listeOuverte.put(intersectionVoisine.getIdNoeud(), new noeud(coutVoisinOrigine, distanceVoisingDestination, qualiteVoisin, intersectionActuel.getIdNoeud(), intersectionVoisine));
				}
			}
		}
		
	}
	
	// Si la liste ouverte est vide, le chemin est impossible
	public noeud rechercherMeilleurNoeud()
	{
		noeud meilleurNoeud = new noeud(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0, new Intersection((long)0, (long)0, (long)0));
		Iterator<HashMap.Entry<Long, noeud>> it = listeOuverte.entrySet().iterator();
		while(it.hasNext())
		{
			HashMap.Entry<Long, noeud> noeudCourant = it.next();
			if(noeudCourant.getValue().qualite<meilleurNoeud.qualite)
			{
				meilleurNoeud = noeudCourant.getValue();
			}
			
		}
		return meilleurNoeud;
	}
	
	public void ajouterListeFermee(noeud noeudCourant)
	{
		listeFermee.put(noeudCourant.intersectionActuel.getIdNoeud(), noeudCourant);
		listeOuverte.remove(noeudCourant.intersectionActuel.getIdNoeud());
	}
	
	
	
}
