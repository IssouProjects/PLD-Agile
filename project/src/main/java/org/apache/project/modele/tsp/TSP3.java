package org.apache.project.modele.tsp;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */
public class TSP3 extends TemplateTSP {

	/**
	 * 
	 */
	public TSP3() {

	}

	/**
	 * @param tpsLimite
	 * @param nbSommets
	 * @param cout
	 * @param duree
	 * @param tempsMini
	 * @param tempsMax
	 */
	public void chercheSolution(int tpsLimite, int nbSommets, int[][] cout, int[] duree, int[] tempsMini,
			int[] tempsMax) {
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Integer.MAX_VALUE;
		meilleureSolution = new Integer[nbSommets];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i = 1; i < nbSommets; i++)
			nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus, 0, cout, duree, System.currentTimeMillis(), tpsLimite, tempsMini, tempsMax);
	}

	/**
	 * @param sommetCrt
	 * @param nonVus
	 * @param vus
	 * @param coutVus
	 * @param cout
	 * @param duree
	 * @param tpsDebut
	 * @param tpsLimite
	 * @param tempsMini
	 * @param tempsMax
	 */
	void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, int[][] cout,
			int[] duree, long tpsDebut, int tpsLimite, int[] tempsMini, int[] tempsMax) {
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
		} else if (coutVus + bound(sommetCrt, nonVus, cout, duree) < coutMeilleureSolution) {
			Iterator<Integer> it = iterator(nonVus);
			while (it.hasNext()) {
				Integer prochainSommet = it.next();
				if (coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet] > tempsMax[prochainSommet]) {
					// Ce point est plus possible, on a depasse la limite max, donc cette branche n
					// est plus possible, puisqu il s agit forcemment du chemin le plus court actuel
					// pour y parvenir
					break;
				} else if (coutVus + cout[sommetCrt][prochainSommet]
						+ duree[prochainSommet] < tempsMini[prochainSommet]) {
					vus.add(prochainSommet);
					nonVus.remove(prochainSommet);
					branchAndBound(prochainSommet, nonVus, vus, tempsMini[prochainSommet] + duree[prochainSommet], cout,
							duree, tpsDebut, tpsLimite, tempsMini, tempsMax);
					vus.remove(prochainSommet);
					nonVus.add(prochainSommet);
				} else {
					vus.add(prochainSommet);
					nonVus.remove(prochainSommet);
					branchAndBound(prochainSommet, nonVus, vus,
							coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet], cout, duree, tpsDebut,
							tpsLimite, tempsMini, tempsMax);
					vus.remove(prochainSommet);
					nonVus.add(prochainSommet);
				}
			}
		}
	}

	@Override
	protected Iterator<Integer> iterator(ArrayList<Integer> nonVus) {
		return new IteratorSeq(nonVus);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return 0;
	}
}
