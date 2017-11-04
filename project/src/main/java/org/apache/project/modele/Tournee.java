package org.apache.project.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.project.modele.tsp.TSP2;

/**
 * La classe <tt>Tournee</tt> représente une tournée constituée d'un ensemble de
 * livraisons.
 */
public class Tournee extends Observable {

	private Entrepot entrepot;
	private List<Livraison> livraisonsOrdonnees;
	private List<Chemin> chemins;
	private int dureeTourneeSecondes;

	/**
	 * Construit une tournée vide, c'est à dire sans aucune livraison, entrepôt et
	 * heure de départ.
	 */
	public Tournee() {
		this.entrepot = null;
		chemins = new ArrayList<Chemin>();
		livraisonsOrdonnees = new ArrayList<Livraison>();
	}

	public Entrepot getEntrepot() {
		return entrepot;
	}

	public void setEntrepot(Entrepot entrepot) {
		this.entrepot = entrepot;
	}

	public void ajouterListeLivraison(Livraison livraison) {
		livraisonsOrdonnees.add(livraison);
	}

	public int getDureeTourneeSecondes() {
		return dureeTourneeSecondes;
	}

	/**
	 * Ajoute une livraison à une place (donnée en paramètre) de la tournée.
	 * 
	 * @param livraison
	 *            la livraison à ajouter
	 * @param index
	 *            l'index dans la liste ordonnée
	 */
	public void ajouterListeLivraison(Livraison livraison, int index) {
		livraisonsOrdonnees.add(index, livraison);
	}
	
	/**
	 * Retire la livraison a l'index donne
	 * @param index
	 * 			 l'index
	 */
	public void retireListeLivraison(int index) {
		livraisonsOrdonnees.remove(index);
	}

	public void ajouterChemin(Chemin chemin) {
		chemins.add(chemin);
	}

	/**
	 * Ajoute un chemin à un endroit (donné en paramètre) de la tournée.
	 * 
	 * @param chemin
	 *            le chemin à ajouter
	 * @param index
	 *            l'index dans la liste ordonnée
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

	public List<Livraison> getLivraisonsOrdonnees() {
		return livraisonsOrdonnees;
	}

	public int getLivraisonsSize() {
		return livraisonsOrdonnees.size();
	}

	/**
	 * La méthode calcule l'ordre et les horaires de passages des livraisons dans
	 * une tournée à partir de la demande de livraisons, ainsi que du plan de la
	 * ville où ont lieu lesdites livraisons.
	 * Retourne vrai si le temps limite a été atteint
	 * 
	 * @param plan
	 *            plan de la ville/agglomération où se déroule la tournée.
	 * @param demande
	 *            demande de livraison à partir de laquelle on construit la tournée.
	 * @param tempsLimite
	 * 			  le temps laissé à l'algorithme pour trouver une solution en millisecondes
	 */
	public boolean calculerTournee(PlanDeVille plan, DemandeDeLivraison demande, int tempsLimite) {

		List<Chemin> graphe = Dijkstra.principalDijkstra(plan, demande);

		int nombreLivraison = demande.getListeLivraison().size();
		long[] conversion = new long[nombreLivraison];

		int[] duree = new int[nombreLivraison];

		// Ajout des intersections de livraisons
		for (int i = 0; i < nombreLivraison; i++) {
			conversion[i] = demande.getListeLivraison().get(i).getLieuDeLivraison().getIdNoeud();
			duree[i] = demande.getListeLivraison().get(i).getDuree();
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
		tspSolut.chercheSolution(tempsLimite, nombreLivraison, cout, duree);
		boolean tempsLimiteAtteint = tspSolut.getTempsLimiteAtteint();
		
		// Definit les parametres entrepots et la liste des intersections ordonnées
		long idIntersection = 0;
		long idIntersectionSuivante = 0;

		// adresseEntrepot = demande.getAdresseEntrepot();

		for (int i = 0; i < nombreLivraison; i++) {
			idIntersection = conversion[tspSolut.getMeilleureSolution(i)];
			if (i != nombreLivraison - 1) {
				idIntersectionSuivante = conversion[tspSolut.getMeilleureSolution(i + 1)];
			} else {
				idIntersectionSuivante = conversion[0];
			}
			// Mettre les intersections ordonnees (une a une)
			// On n ajoute pas a la liste des intersections pour l entrepot
			for (int j = 0; j < nombreLivraison; j++) {
				Livraison livraisonActuelle = demande.getListeLivraison().get(j);
				if (idIntersection == livraisonActuelle.getLieuDeLivraison().getIdNoeud()) {
					livraisonActuelle.setHeureArrivee(PlageHoraire
							.calculerHeureArrivee(demande.getEntrepot().getHeureDepart(), dureeTourneeSecondes));

					if (livraisonActuelle.getPlageHoraire() != null) {
						// Ajout dans le temps de livraison le temps d attente
						long avance = livraisonActuelle.getPlageHoraire().getDebut().getTime()
								- livraisonActuelle.getHeureArrivee().getTime();
						if (avance > 0) {
							dureeTourneeSecondes += (int) Math.ceil(avance / 1000);
						}
					}

					livraisonsOrdonnees.add(livraisonActuelle);
					dureeTourneeSecondes += livraisonActuelle.getDuree();
					break;
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
		
		return tempsLimiteAtteint;
	}

	/**
	 * Calcul le nouveau chemin pour aller de l'intersectionPre a l'intersectionSuiv
	 * @param plan 
	 * 				plan de la ville/agglomeration où se déroule la tournee.
	 * @param intersectionPre
	 * 						Intersection d'origine
	 * @param intersectionSuiv
	 * 						Intersection d'arrivee
	 * @return
	 */
	public Chemin calculerNouveauChemin(PlanDeVille plan, Intersection intersectionPre,
			 Intersection intersectionSuiv) {

		return Dijkstra.principalDijkstra(plan, intersectionPre, intersectionSuiv);
	}

	/**
	 * La méthode réinitialise tous les attributs d'une tournée.
	 */
	public void clear() {
		this.entrepot = null;
		chemins.clear();
		livraisonsOrdonnees.clear();
		this.dureeTourneeSecondes = 0;
	}

	/**
	 * La méthode calcule la durée totale de la tournée en secondes. Elle est à
	 * appeler à chaque fois qu'on crée/modifie un élement de la tournée afin de ne
	 * pas avoir une durée totale incorrecte.
	 */
	public void calculerDureeTotale() {

		dureeTourneeSecondes = 0;

		int nombreChemins = chemins.size();

		for (int i = 0; i < nombreChemins; i++) {

			// Mettre les intersections ordonnees (une a une)
			Livraison livraisonActuelle = livraisonsOrdonnees.get(i);

				livraisonActuelle.setHeureArrivee(
						PlageHoraire.calculerHeureArrivee(entrepot.getHeureDepart(), dureeTourneeSecondes));

			if (livraisonActuelle.getPlageHoraire() != null) {
				// Ajout dans le temps de livraison le temps d attente
				long avance = livraisonActuelle.getPlageHoraire().getDebut().getTime()
						- livraisonActuelle.getHeureArrivee().getTime();
				if (avance > 0) {
					dureeTourneeSecondes += (int) Math.ceil(avance / 1000);
				}
			}

			dureeTourneeSecondes += livraisonActuelle.getDuree();
			
			// Mettre a jour les heures pour chaque chemin

			dureeTourneeSecondes += chemins.get(i).getDuree();
		}
	}
	
	public void ajouterNouvelleLivraison(PlanDeVille planDeVille, Livraison nouvelleLivraison, Livraison livraisonPrecedente) {
		
		int indexPre = this.livraisonsOrdonnees.indexOf(livraisonPrecedente);
		Livraison livraisonSuiv = this.getLivraison(indexPre + 1);
		
		Chemin chemin1 = this.calculerNouveauChemin(planDeVille, livraisonPrecedente.getLieuDeLivraison(),
				nouvelleLivraison.getLieuDeLivraison());
		Chemin chemin2 = this.calculerNouveauChemin(planDeVille, nouvelleLivraison.getLieuDeLivraison(),
				livraisonSuiv.getLieuDeLivraison());
		
		this.ajouterListeLivraison(nouvelleLivraison, indexPre + 1);
		this.supprimerChemin(indexPre);
		this.ajouterChemin(chemin1, indexPre);
		this.ajouterChemin(chemin2, indexPre + 1);
		
		this.calculerDureeTotale();
	}
	
	public void supprimerLivraison(PlanDeVille planDeVille, int indexLivSuppr) {
		Livraison livraisonPre = this.getLivraison(indexLivSuppr - 1);
		Livraison livraisonSuiv = this.getLivraison(indexLivSuppr + 1);
		Chemin chemin = this.calculerNouveauChemin(planDeVille, livraisonPre.getLieuDeLivraison(), livraisonSuiv.getLieuDeLivraison());
		
		this.retireListeLivraison(indexLivSuppr);
		this.supprimerChemin(indexLivSuppr-1);
		this.supprimerChemin(indexLivSuppr-1);
	
		this.ajouterChemin(chemin, indexLivSuppr-1);
		
		this.calculerDureeTotale();
	}
	
	

}
