package org.apache.project.modele.tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 */
public class IteratorSeq implements Iterator<Integer> {

	private Integer[] candidats;
	private int nbCandidats;
	private int tailleMax;

	/**
	 * Crée un iterateur pour itérer sur l'ensemble des sommets de nonVus.
	 * 
	 * @param nonVus
	 */
	public IteratorSeq(Collection<Integer> nonVus) {
		this.candidats = new Integer[nonVus.size()];
		nbCandidats = 0;
		for (Integer s : nonVus) {
			candidats[nbCandidats++] = s;
		}
		tailleMax = nbCandidats;
	}

	/**
	 * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
	 * 
	 * @param nonVus
	 * @param sommetCrt
	 */
	public IteratorSeq(Collection<Integer> nonVus, int sommetCrt, int[][] cout, int[] tempsMini, int coutVus) {
		this.candidats = new Integer[nonVus.size()];
		nbCandidats = 0;

		int miniVal = Integer.MAX_VALUE;
		Integer mini = 0;
		int ancienMiniVal = 0;
		int nbrMemeCout = 0;
		for (int i = 0; i < nonVus.size();) {
			miniVal = Integer.MAX_VALUE;
			for (Integer s2 : nonVus) {
				if ((miniVal >= cout[sommetCrt][s2] && miniVal >= (tempsMini[s2] - coutVus))
						&& ancienMiniVal < Math.max(cout[sommetCrt][s2], (tempsMini[s2] - coutVus))) {
					if (miniVal == cout[sommetCrt][s2] || miniVal == (tempsMini[s2] - coutVus)) {
						nbrMemeCout++;
					} else {
						nbrMemeCout = 1;
					}
					mini = s2;
					miniVal = Math.max(cout[sommetCrt][s2], tempsMini[s2] - coutVus);
				}
			}

			if (nbrMemeCout == 0) {
				nbrMemeCout = 1;
			}
			i += nbrMemeCout;

			if (nbrMemeCout == 1) {
				candidats[nbCandidats++] = mini;
			} else {
				for (Integer s2 : nonVus) {
					if (Math.max(cout[sommetCrt][s2], (tempsMini[s2] - coutVus)) == miniVal) {
						candidats[nbCandidats++] = s2;
					}
				}
			}
			ancienMiniVal = miniVal;
		}
		tailleMax = nbCandidats;

	}

	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	@Override
	public Integer next() {
		nbCandidats--;
		return candidats[tailleMax - nbCandidats - 1];
	}

	@Override
	public void remove() {
	}

}
