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
		chemins = new ArrayList<Chemin>();
		livraisonsOrdonnees = new ArrayList<Livraison>();
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
		for(int i = 0; i < nombreLivraison - 1; i++)
		{
			conversion[i+1] = demande.getListeLivraison().get(i).getLieuDeLivraison().getIdNoeud();
			duree[i+1] = demande.getListeLivraison().get(i).getDuree();
		}
		
		int[][] cout = new int [nombreLivraison][nombreLivraison];
		
		int nombreChemin = graphe.size();
		long idDestination = 0;
		long idOrigine = 0;
		int convertDestination = 0;
		int convertOrigine = 0;
		
		//Mise en place de la table de conversion et cout
		
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
		long idIntersectionSuivante = 0;
		
		adresseEntrepot = demande.getAdresseEntrepot();
		
		System.out.println("ultcaca" + nombreChemin);
		
		for(int i = 0; i < nombreLivraison; i++)
		{
			System.out.println(i);
			System.out.println("test:" + tspSolut.getMeilleureSolution(i));
			System.out.println("test2:" + conversion[tspSolut.getMeilleureSolution(i)]);
			idIntersection = conversion[tspSolut.getMeilleureSolution(i)];
			if(i != nombreLivraison - 1)
			{
				idIntersectionSuivante = conversion[tspSolut.getMeilleureSolution(i+1)];
			} else {
				idIntersectionSuivante = conversion[0];
			}
			//Mettre les intersections ordonnees (une a une)
			//On n ajoute pas a la liste des intersections pour l entrepot
			if(i > 0)
			{
				for(int j = 0; j < nombreLivraison; j++)
				{
					if(idIntersection == demande.getListeLivraison().get(j).getLieuDeLivraison().getIdNoeud())
					{
						livraisonsOrdonnees.add(demande.getListeLivraison().get(j));
						break;
					}
				}
			}

			//Mettre les chemins ordonnees (une a une)
			for(int j = 0; j < nombreChemin; j++)
			{
				if(graphe.get(j).getDebut().getIdNoeud() == idIntersection && graphe.get(j).getFin().getIdNoeud() == idIntersectionSuivante)
				{
					System.out.println("caca" + graphe.get(j).getDebut().getIdNoeud());
					System.out.println(graphe.get(j));
					chemins.add(graphe.get(j));
				}
			}
		}
		

		
	}
	public int getDureeTourneeSecondes()
	{
		return dureeTourneeSecondes;
	}
}
