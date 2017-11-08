package org.apache.project.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * permet de calculer le plus court chemin entre deux intersections
 */
public class Dijkstra {

	/**
	 * represente le noeud du graphe qui correspond à une intersection avec
	 * l'id de son intersection ancêtre et le cout (distance parcourue)
	 */
	static class noeud {
		public double coutOrigine;
		public Long idAncetre;
		public Intersection intersectionActuel;

		/**
		 * constructeur d'un noeud
		 * @param cout_origine
		 * 				distance parcourue pour arriver au noeud
		 * @param id_ancetre
		 * 				id de l'intersection précedente dans le graphe
		 * @param intersection_Actuel
		 * 				intersection actuel du noeud
		 */
		public noeud(double cout_origine, long id_ancetre, Intersection intersection_Actuel) {
			this.coutOrigine = cout_origine;
			this.idAncetre = id_ancetre;
			this.intersectionActuel = intersection_Actuel;
		}

	}

	static Map<Long, noeud> listeOuverte = new HashMap<Long, noeud>();
	static Map<Long, noeud> listeFermee = new HashMap<Long, noeud>();

	/**
	 * permet d'obtenir une liste de chemins les plus court dans un plan de ville 
	 * à partir d'une demande de livraison
	 * @param plan
	 * 			plan de la ville
	 * @param demande
	 * 			Demande de livraison
	 * @return liste de chemins les plus courts
	 */
	public static List<Chemin> principalDijkstra(PlanDeVille plan, DemandeDeLivraison demande) {

		// Liste des intersections correspondants aux lieux a livrer
		List<Intersection> interLivraisons = new ArrayList<Intersection>();
		// interLivraisons.add(demande.getAdresseEntrepot());

		List<Livraison> demandesLivraisons = demande.getListeLivraison();
		for (Livraison livraison : demandesLivraisons) {
			interLivraisons.add(livraison.getLieuDeLivraison());
		}

		// Liste de toutes les intersections de la ville
		List<Intersection> interPlan = new ArrayList<Intersection>();
		for (Map.Entry<Long, Intersection> entry : plan.getAllIntersections().entrySet()) {
			interPlan.add(entry.getValue());
		}

		return Dijkstra.calculDijkstra(interPlan, interLivraisons);
	}

	/**
	 * permet d'obtenir le chemin le plus court dans un plan de ville entre deux intersections
	 * @param plan
	 * 			plan de la ville
	 * @param depart
	 * 			intersection de départ
	 * @param arrivee
	 * 			intersection d'arrivée
	 * @return chemin le plus court entre depart et arrivée
	 */
	public static Chemin principalDijkstra(PlanDeVille plan, Intersection depart, Intersection arrivee) {

		// Liste des intersections correspondants aux lieux a livrer
		List<Intersection> interLivraisons = new ArrayList<Intersection>();
		interLivraisons.add(depart);
		interLivraisons.add(arrivee);

		// Liste de toutes les intersections de la ville
		List<Intersection> interPlan = new ArrayList<Intersection>();
		for (Map.Entry<Long, Intersection> entry : plan.getAllIntersections().entrySet()) {
			interPlan.add(entry.getValue());
		}

		List<Chemin> chemins = new ArrayList<Chemin>();
		chemins = Dijkstra.calculDijkstra(interPlan, interLivraisons);
		if (chemins == null) {
			return null;
		}
		return chemins.get(0);
	}

	/**
	 * fait tourner l'alogorithme Dijkstra et retourne une liste des plus courts chemins 
	 * à partir de la liste de toutes les intersections du plan et la liste des intersections
	 * des demandes de livraison
	 * @param plan_inter
	 * 				liste de toutes les intersections du plan
	 * @param livraison_inter
	 * 				liste des intersections des demandes de livraison
	 * @return  liste des plus courts chemins
	 */
	public static List<Chemin> calculDijkstra(List<Intersection> plan_inter, List<Intersection> livraison_inter) {
		List<Chemin> ensembleChemins = new ArrayList<Chemin>();
		List<Chemin> resCheminIntermediaire = new ArrayList<Chemin>();
		int nombreLivraison = livraison_inter.size();
		for (int i = 0; i < nombreLivraison; i++) {
			listeOuverte.clear();
			listeFermee.clear();
			resCheminIntermediaire.clear();
			listeOuverte = new HashMap<Long, noeud>();
			listeFermee = new HashMap<Long, noeud>();
			Intersection origine = livraison_inter.get(i);
			noeud noeudCourant = new noeud(0, -1, origine);
			listeOuverte.put(noeudCourant.intersectionActuel.getIdNoeud(), noeudCourant);
			ajouterListeFermee(noeudCourant);
			ajouterNoeudAdjacent(noeudCourant);

			// s arrete lorsque l on a vu tous les points
			while (!listeOuverte.isEmpty()) {

				noeudCourant = rechercherMeilleurNoeud();
				ajouterListeFermee(noeudCourant);
				ajouterNoeudAdjacent(noeudCourant);
			}

			resCheminIntermediaire = ajouteChemin(origine, livraison_inter);
			if (resCheminIntermediaire == null) {
				return null;
			}

			for (int k = 0; k < resCheminIntermediaire.size(); k++) {
				ensembleChemins.add(resCheminIntermediaire.get(k));
			}

		}
		return ensembleChemins;
	}

	/**
	 * Permet de dérouler les chemins de la liste fermée et les mettre
	 * dnas la liste des chemeins les plus courts
	 * @param intersectionOrigine
	 * 					intersection d'origine du chemin
	 * @param LivraisonInter
	 * 				liste des intersections de la demande de livraison	
	 * @return liste de chemins les plus courts
	 */
	public static List<Chemin> ajouteChemin(Intersection intersectionOrigine, List<Intersection> LivraisonInter) // throws
																													// Exception
	{
		// try{
		int nombreDestination = LivraisonInter.size();
		List<Troncon> listTronconInverse = new ArrayList<Troncon>();
		List<Troncon> listTroncon = new ArrayList<Troncon>();
		List<Chemin> resChemin = new ArrayList<Chemin>();
		double distanceLivraison = 0;
		int tailleListe = 0;
		int dureeLivraison = 0;

		for (int i = 0; i < nombreDestination; i++) {
			Intersection intersectionCourante = LivraisonInter.get(i);
			if (!listeFermee.containsKey(intersectionCourante.getIdNoeud())) {
				return null;
			}
			Intersection intersectionDestination = intersectionCourante;
			listTronconInverse = new ArrayList<Troncon>();
			listTroncon = new ArrayList<Troncon>();

			long id_suivant = 0;

			if (intersectionCourante.getIdNoeud() != intersectionOrigine.getIdNoeud()) {
				while (intersectionCourante.getIdNoeud() != intersectionOrigine.getIdNoeud()) {

					Iterator<HashMap.Entry<Long, noeud>> it = listeFermee.entrySet().iterator();
					while (it.hasNext()) {

						HashMap.Entry<Long, noeud> noeudCourant = it.next();
						if (noeudCourant.getValue().intersectionActuel.getIdNoeud() == intersectionCourante
								.getIdNoeud()) {

							id_suivant = intersectionCourante.getIdNoeud();
							intersectionCourante = obtenirIntersection(noeudCourant.getValue().idAncetre);
							listTronconInverse.add(intersectionCourante.getTronconParDestination(id_suivant));
							break;
						}

					}

				}

				tailleListe = listTronconInverse.size();
				distanceLivraison = 0;
				// On inverse la liste des troncons, puisue l on est partie de la fin
				for (int j = tailleListe - 1; j >= 0; j--) {
					listTroncon.add(listTronconInverse.get(j));
					// TODO EXPLAIN THIS TO ME
					// distanceLivraison = distanceLivraison +
					// listTronconInverse.get(j).getLongueur();
				}
				for (Troncon inter : listTroncon) {
					distanceLivraison = distanceLivraison + inter.getLongueur();
				}

				dureeLivraison = (int) (distanceLivraison * 3.6) / 15;
				resChemin.add(new Chemin(intersectionOrigine, intersectionDestination, dureeLivraison, listTroncon));
			}
		}

		return resChemin;
	}

	
	/**
	 * Renvoie une intersection de la liste fermee a partir de son id
	 * @param idIntersection
	 * 				id de l'intersection recherchée
	 * @return l'intersection recherchée
	 */
	public static Intersection obtenirIntersection(Long idIntersection) {
		return listeFermee.get(idIntersection).intersectionActuel;
	
	}

	
	/**
	 * Verifie si l intersection est deja présente dans la liste etudiée 
	 * @param listeNoeuds
	 * 			Liste étudiée
	 * @param uneIntersection
	 * 			Intersection à vérifier
	 * @return
	 */
	public static boolean dejaPresent(Map<Long, noeud> listeNoeuds, Intersection uneIntersection) {
		return listeNoeuds.containsKey(uneIntersection.getIdNoeud());

	} 
	
	/**
	 * Premet d'ajouter les intersections adjacentes a celle actuellement etudiée
	 * parmi celles potentiellements etudiés
	 * @param noeudActuel
	 * 			Intersection actuellement étudiée
	 */
	public static void ajouterNoeudAdjacent(noeud noeudActuel) {
		Intersection intersectionActuel = noeudActuel.intersectionActuel;

		// On utilise un iterateur pour regarder toutes les intersections voisines
		for (Iterator<Troncon> it = intersectionActuel.getTronconsPartants().iterator(); it.hasNext();) {
			Troncon tronconActuel = it.next();
			Intersection intersectionVoisine = tronconActuel.getIntersectionArrivee();

			// Si l intersection voisine est dans la liste fermee, cela ne sert a rien de l
			// etudier plus
			if (!dejaPresent(listeFermee, intersectionVoisine)) {

				double coutVoisinOrigine = noeudActuel.coutOrigine + tronconActuel.getLongueur();
				// Si l intersection voisine est deja dans la liste ouverte, on la met a jour
				// que si la qualite est meilleure
				if (dejaPresent(listeOuverte, intersectionVoisine)) {

					if (coutVoisinOrigine < listeOuverte.get(intersectionVoisine.getIdNoeud()).coutOrigine) {

						listeOuverte.put(intersectionVoisine.getIdNoeud(),
								new noeud(coutVoisinOrigine, intersectionActuel.getIdNoeud(), intersectionVoisine));
					}
				}
				// Si l intersection voisine est absente de la liste ouverte, on l ajoute
				else {
					listeOuverte.put(intersectionVoisine.getIdNoeud(),
							new noeud(coutVoisinOrigine, intersectionActuel.getIdNoeud(), intersectionVoisine));
				}
			}
		}

	}

	/**
	 * Ajoute un noeud à la liste fermée et l'enlève de liste ouverte
	 * @param noeudCourant
	 * 			Noeud à ajouter
	 */
	public static void ajouterListeFermee(noeud noeudCourant) {
		listeFermee.put(noeudCourant.intersectionActuel.getIdNoeud(), noeudCourant);
		listeOuverte.remove(noeudCourant.intersectionActuel.getIdNoeud());
	}

	/**
	 * Cherche le noeud avec le meilleur cout d'origine dans la liste ouverte
	 * @return le meielleur noeud
	 */
	public static noeud rechercherMeilleurNoeud() {
		noeud meilleurNoeud = new noeud(Double.MAX_VALUE, -1, new Intersection((long) -2, (long) -2, (long) -2));
		Iterator<HashMap.Entry<Long, noeud>> it = listeOuverte.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<Long, noeud> noeudCourant = it.next();
			if (noeudCourant.getValue().coutOrigine < meilleurNoeud.coutOrigine) {
				meilleurNoeud = noeudCourant.getValue();
			}

		}
		return meilleurNoeud;
	}

}
