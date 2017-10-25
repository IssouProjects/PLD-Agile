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

	public Tournee() {
		this.adresseEntrepot = null;
		this.heureDepart = null;
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

	public void ajouterLivraison(Livraison livraison) {
		livraisonsOrdonnees.add(livraison);
	}
	
	/**
	 * ajoute une livraison 
	 * @param uneLivraison la livraison à ajouter
	 * @param place l'index dans la liste ordonnée
	 */
	public void ajouterLivraison(Livraison livraison, int index) {
		livraisonsOrdonnees.add(index, livraison);
	}

	public void ajouterChemin(Chemin chemin) {
		chemins.add(chemin);
	}
	
	/**
	 * ajoute un chemin
	 * @param chemin le chemin à ajouter
	 * @param index l'index dans la liste ordonnée
	 */
	public void ajouterChemin(Chemin chemin, int index) {
		chemins.add(index, chemin);
	}

	public List<Chemin> getChemins() {
		return chemins;
	}
	
	public Chemin getChemin(int index) {
		return chemins.get(index);
	}
	
	public void supprimerChemin(int index) {
		chemins.remove(index);
	}
	
	public Livraison getLivraison(int index) {
		return livraisonsOrdonnees.get(index);
	}
	
	private int getLivraisonIndex(Livraison livraison) {
		for(int i = 0; i<livraisonsOrdonnees.size(); i++) {
			if(livraisonsOrdonnees.get(i)==livraison)
				return i;
		}
		return 0;
	}

	public List<Livraison> getLivraisonsOrdonnees() {
		return livraisonsOrdonnees;
	}

	public void calculerTournee(PlanDeVille plan, DemandeDeLivraison demande) {

		List<Chemin> graphe = Dijkstra.principalDijkstra(plan, demande);

		int nombreLivraison = demande.getListeLivraison().size() + 1;
		long[] conversion = new long[nombreLivraison];

		int[] duree = new int[nombreLivraison];

		// Ajout entrepot
		conversion[0] = demande.getAdresseEntrepot().getIdNoeud();
		duree[0] = 0;

		// Ajout des intersections de livraisons
		for (int i = 0; i < nombreLivraison - 1; i++) {
			conversion[i + 1] = demande.getListeLivraison().get(i).getLieuDeLivraison().getIdNoeud();
			duree[i + 1] = demande.getListeLivraison().get(i).getDuree();
		}

		int[][] cout = new int[nombreLivraison][nombreLivraison];

		int nombreChemin = graphe.size();
		long idDestination = 0;
		long idOrigine = 0;
		int convertDestination = 0;
		int convertOrigine = 0;

		// Mise en place de la table de conversion et cout

		for (int i = 0; i < nombreChemin; i++) {
			idOrigine = graphe.get(i).getDebut().getIdNoeud();
			idDestination = graphe.get(i).getFin().getIdNoeud();

			for (int j = 0; j < nombreLivraison; j++) {
				if (conversion[j] == idOrigine) {
					convertOrigine = j;
				}
				if (conversion[j] == idDestination) {
					convertDestination = j;
				}
			}
			cout[convertOrigine][convertDestination] = graphe.get(i).getDuree();
		}
		TSP2 tspSolut = new TSP2();
		tspSolut.chercheSolution(10000, nombreLivraison, cout, duree);

		// Definit les parametres entrepots et la liste des intersections ordonnées
		long idIntersection = 0;
		long idIntersectionSuivante = 0;

		adresseEntrepot = demande.getAdresseEntrepot();

		for (int i = 0; i < nombreLivraison; i++) {
			idIntersection = conversion[tspSolut.getMeilleureSolution(i)];
			if (i != nombreLivraison - 1) {
				idIntersectionSuivante = conversion[tspSolut.getMeilleureSolution(i + 1)];
			} else {
				idIntersectionSuivante = conversion[0];
			}
			// Mettre les intersections ordonnees (une a une)
			// On n ajoute pas a la liste des intersections pour l entrepot
			if (i > 0) {
				for (int j = 0; j < nombreLivraison; j++) {
					Livraison livraisonActuelle = demande.getListeLivraison().get(j);
					if (idIntersection == livraisonActuelle.getLieuDeLivraison().getIdNoeud()) {
						livraisonActuelle.setHeureArrivee(
								PlageHoraire.calculerHeureArrivee(demande.getHeureDepart(), dureeTourneeSecondes));

						if(livraisonActuelle.getPlageHoraire() != null)
						{
							//Ajout dans le temps de livraison le temps d attente
							long avance = livraisonActuelle.getPlageHoraire().getDebut().getTime() - livraisonActuelle.getHeureArrivee().getTime();
							if (avance > 0) {
								dureeTourneeSecondes += (int) Math.ceil(avance / 60000);
							}
						}
						
						livraisonsOrdonnees.add(livraisonActuelle);
						dureeTourneeSecondes += livraisonActuelle.getDuree();
						break;
					}
				}
			}

			// Mettre les chemins ordonnees (une a une)
			for (int j = 0; j < nombreChemin; j++) {
				if (graphe.get(j).getDebut().getIdNoeud() == idIntersection
						&& graphe.get(j).getFin().getIdNoeud() == idIntersectionSuivante) {
					chemins.add(graphe.get(j));
					dureeTourneeSecondes += graphe.get(j).getDuree();
				}
			}
		}
	}
	
	public void calculerNouveauxChemins(PlanDeVille plan, Livraison livraisonPre, Livraison nouvelleLivraison) {
		int indexPre = this.getLivraisonIndex(livraisonPre);
		Chemin chemin1 = Dijkstra.principalDijkstra(plan, livraisonPre.getLieuDeLivraison(), nouvelleLivraison.getLieuDeLivraison());
		Livraison livraisonSuivante = this.getLivraison(indexPre+1);
		Chemin chemin2 = Dijkstra.principalDijkstra(plan, nouvelleLivraison.getLieuDeLivraison(), livraisonSuivante.getLieuDeLivraison());
		
		this.supprimerChemin(indexPre+1);
		this.ajouterLivraison(nouvelleLivraison, indexPre+1);
		this.ajouterChemin(chemin1, indexPre);
		this.ajouterChemin(chemin2, indexPre +1);
		
	}

	public int getDureeTourneeSecondes() {
		return dureeTourneeSecondes;
	}
	
	public void clear() {
		this.adresseEntrepot = null;
		this.heureDepart = null;
		chemins.clear();
		livraisonsOrdonnees.clear();
	}
}
