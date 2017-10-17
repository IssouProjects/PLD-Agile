package org.apache.project.modele.tsp;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP2 extends TemplateTSP {

	public TSP2()
	{
		
	}
	
	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return new IteratorSeq(nonVus, sommetCrt);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return 0;
	}
}
