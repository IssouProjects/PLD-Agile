package org.apache.project.modele;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.project.modele.tsp.TSP2;

public class Tournee extends Observable {
	
	private Intersection adresseEntrepot;
	private Time heureDepart;
	private List<Livraison> livraisonsOrdonnees;
	private List<Chemin> chemins;
	private int dureeTourneeSecondes;

	public Tournee(Intersection adresseEntrepot, Time heureDepart) {
		this.adresseEntrepot=adresseEntrepot;
		this.heureDepart=heureDepart;
	}
	
	public Intersection getAdresseEntrepot() {
		return adresseEntrepot;
	}
	
	public void setAdresseEntrepot(Intersection adresseEntrepot) {
		this.adresseEntrepot = adresseEntrepot;
	}

	public Time getHeureDepart() {
		return heureDepart;
	}

	public void setHeureDepart(Time heureDepart) {
		this.heureDepart = heureDepart;
	}
	
	public void ajouterLivraison(Livraison uneLivraison) {
		livraisonsOrdonnees.add(uneLivraison);
	}
	
	public void ajouterChemin(Chemin chemin) {
		chemins.add(chemin);
	}
	
	public List<Chemin> getChemins(){
		return chemins;
	}
	
	public List<Livraison> getLivraisonsOrdonnees(){
		return livraisonsOrdonnees;
	}
	
	public void calculerTournee(PlanDeVille plan, DemandeDeLivraison demande) {
		
		List<Chemin> graphe = Dijkstra.principalDijkstra(plan, demande);
		
		int nombreLivraison = demande.getListeLivraison().size() + 1;
		long [] conversion = new long [nombreLivraison];
		
		int [] duree = new int [nombreLivraison];
		
		//Ajout entrepot
		conversion[0] = demande.getAdresseEntrepot().getIdNoeud();
		duree[0] = 0;
		
		//Ajout des intersections de livraisons
		for(int i = 1; i < nombreLivraison; i++)
		{
			conversion[i] = demande.getListeLivraison().get(i).getLieuDeLivraison().getIdNoeud();
			duree[i] = demande.getListeLivraison().get(i).getDuree();
		}
		
		int[][] cout = new int [nombreLivraison][nombreLivraison];
		
		int nombreChemin = graphe.size();
		long idDestination = 0;
		long idOrigine = 0;
		int convertDestination = 0;
		int convertOrigine = 0;
		
		for(int i = 0; i < nombreChemin; i++)
		{
			idOrigine = graphe.get(i).getDebut().getIdNoeud();
			idDestination = graphe.get(i).getFin().getIdNoeud();
			
			for(int j = 0; j < nombreLivraison; j++)
			{
				if(conversion[j] == idOrigine)
				{
					convertOrigine = j;
				}
				if(conversion[j] == idDestination)
				{
					convertDestination = j;
				}
			}
			cout[convertOrigine][convertDestination] = graphe.get(i).getDuree();
		}
		TSP2 tspSolut = new TSP2();
		tspSolut.chercheSolution(1000, nombreLivraison, cout, duree);
		
		//Definit les parametres entrepots et la liste des intersections ordonnées
		long idIntersection = 0;
		
		adresseEntrepot = demande.getAdresseEntrepot();
		
		for(int i = 0; i < nombreChemin + 1; i++)
		{
			idIntersection = conversion[tspSolut.getMeilleureSolution(i)];
			
			//Mettre les intersections ordonnees (une a une)
			for(int j = 1; j < nombreLivraison; j++)
			{
				if(idIntersection == demande.getListeLivraison().get(j).getLieuDeLivraison().getIdNoeud())
				{
					livraisonsOrdonnees.add(demande.getListeLivraison().get(j));
					break;
				}
			}
			//Mettre les chemins ordonnees (une a une)
			if(i != nombreChemin + 1)
			{
				for(int j = 1; j < nombreChemin; j++)
				{
					if(graphe.get(j).getDebut().getIdNoeud() == idIntersection)
					{
						chemins.add(graphe.get(j));
					}
				}
			}
		}
		

		
	}
	public int getDureeTourneeSecondes()
	{
		return dureeTourneeSecondes;
	}
}
