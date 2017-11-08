package org.apache.project.modele.tsp;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * TSP permettant d'effectuer un calcul de plus court chemin dans le cas du
 * voyageur de commerce
 */
public class TSP5 extends TemplateTSP {

	private int plageFiniPremier;
	private int indicePlageFiniPremier;

	/**
	 * Methode de construction ne prenant aucun paramètre en entrée, permettant
	 * d'instancier un objet TSP5
	 */
	public TSP5() {

	}

	/**
	 * @param tpsLimite
	 *            Il s'agit du temps d'éxecution limite (en millisecondes) que nous
	 *            permettons à l'algorithme, pouvant être très long
	 * @param nbSommets
	 *            Il s'agit du nombre de sommets à visiter dans le TSP
	 * @param cout
	 *            Il s'agit d'un tableau de taille [nbSommets][nbSommets] comprenant
	 *            le cout pour aller d'un sommet à un autre, le premier indice étant
	 *            celui de départ et le deuxième celui d'arrivée
	 * @param duree
	 *            Il s'agit d'un tableau de taille [nbSommets] stockant un cout que
	 *            l'on passe sur chaque sommet (que l'on retrouve par son indice)
	 * @param tempsMini
	 *            Il s'agit du cout minimum ayant du déjà avoir été stockée afin de
	 *            pouvoir visiter le sommet correspondant à son indice
	 * @param tempsMax
	 *            Il s'agit du cout maximum pouvant avoir été stockée afin de
	 *            pouvoir visiter chaque sommet (identifié par leurs indices)
	 */
	public void chercheSolution(int tpsLimite, int nbSommets, int[][] cout, int[] duree, int[] tempsMini,
			int[] tempsMax) {
		tempsLimiteAtteint = false;
		coutMeilleureSolution = 3600 * 24;
		meilleureSolution = new Integer[nbSommets];
		plageFiniPremier = 3600*24;
		indicePlageFiniPremier = 0;
		duree[0] = 0;
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i = 1; i < nbSommets; i++)
			nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus, 0, cout, duree, System.currentTimeMillis(), tpsLimite, tempsMini, tempsMax);
	}

	/**
	 * 
	 * Methode de resolution par separation et evaluation (branch and bound) du TSP
	 * prenant en compte un système de cout minimal atteint avant chaque sommets, et
	 * de cout maximal atteint avant chaque sommets et un système d'heuristique
	 * cherchant toujours le sommet le plus proche en sommet suivant
	 * 
	 * 
	 * @param sommetCrt
	 *            Il s'agit du sommet sur lequel on se situe actuellement lors de
	 *            l'évaluation de cette branche
	 * @param nonVus
	 *            Il s'agit de la liste des éléments actuellement non vus dans cette
	 *            branche
	 * @param vus
	 *            Il s'agit de la liste des éléments actuellement vus dans cette
	 *            branche
	 * @param coutVus
	 *            Il s'agit du cout de la branche actuellement évalué
	 * @param cout
	 *            Il s'agit d'un tableau à double entré référençant les coups
	 *            minimaux entre chaque sommet
	 * @param duree
	 *            Il s'agit d'un tableau de cout lié au passage sur chaque sommet
	 * @param tpsDebut
	 *            Il s'agit d'un temps système correspondant au premier appel au
	 *            branchAndBound afin de pouvoir savoir cela fait combien de temps
	 *            que le TSP calcule
	 * @param tpsLimite
	 *            Il s'agit d'un temps limite d'execution, qui lorsqu'il est atteint
	 *            met un terme à l'éxecution du TSP
	 * @param tempsMini
	 *            Il s'agit d'un tableau de cout minimal avant de pouvoir aller sur
	 *            chaque sommet
	 * @param tempsMax
	 *            Il s'agit d'un tableau de cout maximal afin de pouvoir aller sur
	 *            chaque sommet
	 */
	private void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus,
			int[][] cout, int[] duree, long tpsDebut, int tpsLimite, int[] tempsMini, int[] tempsMax) {
		if (System.currentTimeMillis() - tpsDebut > tpsLimite) {
			tempsLimiteAtteint = true;
			return;
		}
		if (nonVus.size() == 0) { // tous les sommets ont ete visites
			coutVus += cout[sommetCrt][0];
			if (coutVus < coutMeilleureSolution) { // on a trouve une solution meilleure que meilleureSolution
				vus.toArray(meilleureSolution);
				coutMeilleureSolution = coutVus;
			}

		} else if (coutVus + bound(sommetCrt, nonVus, cout, duree, tempsMax) < coutMeilleureSolution) {
			if (plageFiniPremier > coutVus + cout[sommetCrt][indicePlageFiniPremier] + duree[indicePlageFiniPremier]) {
				Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree, tempsMini, coutVus);
				while (it.hasNext() && tempsLimiteAtteint == false) {
					Integer prochainSommet = it.next();
					if (coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet] > tempsMax[prochainSommet]) {
						// Ce point est plus possible, on a depasse la limite max, donc cette branche n
						// est plus possible, puisqu il s agit forcemment du chemin le plus court actuel
						// pour y parvenir
						break;
					} else {
						vus.add(prochainSommet);
						nonVus.remove(prochainSommet);
						branchAndBound(prochainSommet, nonVus, vus,
								Math.max(tempsMini[prochainSommet], coutVus + cout[sommetCrt][prochainSommet])
										+ duree[prochainSommet],
								cout, duree, tpsDebut, tpsLimite, tempsMini, tempsMax);
						vus.remove(prochainSommet);
						nonVus.add(prochainSommet);
					}
				}
			}
		}
	}

	// Juste pour heritage, et appelle si retard inevitable
	/**
	 * Methode permettant de faire un itérateur sur nonVus et allant à chaque fois
	 * sur le sommet le plus proche possible
	 * 
	 * @param sommetCrt
	 *            il s'agit du sommet actuel du bout de la branche
	 * @param nonVus
	 *            : tableau des sommets restant a visiter
	 * @param cout
	 *            : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets
	 *            et 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @param tempsMini
	 *            : temppsMini[i] = duree minimal avant de visiter le sommet i, avec
	 *            0 <= i <= nbSommets
	 * @param coutVus
	 *            duree actuel lors de l'appel à cette méthode
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	@Override
	protected Iterator<Integer> iterator(ArrayList<Integer> nonVus) {
		return new IteratorSeq(nonVus);
	}

	/**
	 * Methode permettant de faire un itérateur sur nonVus et allant à chaque fois
	 * sur le sommet le plus proche possible
	 * 
	 * @param sommetCrt
	 *            il s'agit du sommet actuel du bout de la branche
	 * @param nonVus
	 *            : tableau des sommets restant a visiter
	 * @param cout
	 *            : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets
	 *            et 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @param tempsMini
	 *            : temppsMini[i] = duree minimal avant de visiter le sommet i, avec
	 *            0 <= i <= nbSommets
	 * @param coutVus
	 *            duree actuel lors de l'appel à cette méthode
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree,
			int[] tempsMini, int coutVus) {
		return new IteratorSeq(nonVus, sommetCrt, cout, tempsMini, coutVus);
	}

	// Pour heritage surtout, et appelle si retard inevitable
	/**
	 * Methode renvoyant une heuristique pour le cas demandé et ne prenant pas en
	 * compte les plages horaires
	 * 
	 * @param sommetCourant
	 *            il s'agit du sommet actuel du bout de la branche
	 * @param nonVus
	 *            : tableau des sommets restant a visiter
	 * @param cout
	 *            : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets
	 *            et 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @return une borne inferieure du cout des permutations commencant par
	 *         sommetCourant, contenant chaque sommet de nonVus exactement une fois
	 *         et terminant par le sommet 0
	 */
	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {

		int bound = 0;

		plageFiniPremier = 3600*24;
		indicePlageFiniPremier = 0;

		int valMini1 = cout[sommetCourant][0];
		int valMini2 = Integer.MAX_VALUE;

		for (Integer s : nonVus) {
			if (cout[sommetCourant][s] < valMini1) {
				valMini1 = cout[sommetCourant][s];
			}
			// retour a l entrepot
			if (cout[s][0] < valMini2) {
				valMini2 = cout[s][0];
			}
			for (Integer s2 : nonVus) {
				if (cout[s][s2] < valMini2 && !s.equals(s2)) {
					valMini2 = cout[s][s2];
				}
			}
			bound += valMini2;
			bound += duree[s];
		}

		bound += valMini1;

		return bound;
	}

	/**
	 * 
	 * Methode renvoyant une heuristique pour le cas demandé et ne prenant pas en
	 * compte les plages horaires
	 * 
	 * @param sommetCourant
	 *            il s'agit du sommet actuel du bout de la branche
	 * @param nonVus
	 *            : tableau des sommets restant a visiter
	 * @param cout
	 *            : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets
	 *            et 0 <= j < nbSommets
	 * @param duree
	 *            : duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @param tempsMax
	 *            tempsMax[i] = duree maximal avant de visiter le sommet i, avec 0
	 *            <= i < nbSommets
	 * @return une borne inferieure du cout des permutations commencant par
	 *         sommetCourant, contenant chaque sommet de nonVus exactement une fois
	 *         et terminant par le sommet 0
	 */
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree, int[] tempsMax) {

		int bound = 0;

		plageFiniPremier = 3600*24;
		indicePlageFiniPremier = 0;

		int valMini1 = cout[sommetCourant][0];
		int valMini2 = Integer.MAX_VALUE;

		for (Integer s : nonVus) {
			if (tempsMax[s] < plageFiniPremier) {
				plageFiniPremier = tempsMax[s];
				indicePlageFiniPremier = s;
			}
			if (cout[sommetCourant][s] < valMini1) {
				valMini1 = cout[sommetCourant][s];
			}
			// retour a l entrepot
			if (cout[s][0] < valMini2) {
				valMini2 = cout[s][0];
			}
			for (Integer s2 : nonVus) {
				if (cout[s][s2] < valMini2 && s != s2) {
					valMini2 = cout[s][s2];
				}
			}
			bound += valMini2;
			bound += duree[s];
		}

		bound += valMini1;

		return bound;
	}
}
