package org.apache.project.modele.tsp;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(ArrayList<Integer> nonVus) {
		return new IteratorSeq(nonVus);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return 0;
	}
}
