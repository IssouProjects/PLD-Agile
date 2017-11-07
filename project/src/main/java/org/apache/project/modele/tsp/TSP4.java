package org.apache.project.modele.tsp;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP4 extends TemplateTSP {

	public TSP4()
	{
		
	}
	
	
	public void chercheSolution(int tpsLimite, int nbSommets, int[][] cout, int[] duree, int[] tempsMini, int[] tempsMax){
		tempsLimiteAtteint = false;
		coutMeilleureSolution = 3600*24;
		meilleureSolution = new Integer[nbSommets];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i=1; i<nbSommets; i++) nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus, 0, cout, duree, System.currentTimeMillis(), tpsLimite, tempsMini, tempsMax);
	}
	
	void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, int[][] cout, int[] duree, long tpsDebut, int tpsLimite, int[] tempsMini, int[] tempsMax){
		 if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 return;
		 }
	    if (nonVus.size() == 0){ // tous les sommets ont ete visites
	    	coutVus += cout[sommetCrt][0];
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
	    		vus.toArray(meilleureSolution);
	    		coutMeilleureSolution = coutVus;
	    	}
	    } else if (coutVus + bound(sommetCrt, nonVus, cout, duree) < coutMeilleureSolution){
	    
	        Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree, tempsMini, coutVus);
	        while (it.hasNext() && tempsLimiteAtteint == false){
	        	Integer prochainSommet = it.next();
				if(coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet] > tempsMax[prochainSommet])
				{
					//Ce point est plus possible, on a depasse la limite max, donc cette branche n est plus possible, puisqu il s agit forcemment du chemin le plus court actuel pour y parvenir
					break;
				} else 
				{
		        	vus.add(prochainSommet);
		        	nonVus.remove(prochainSommet);
		        	branchAndBound(prochainSommet, nonVus, vus, Math.max(tempsMini[prochainSommet], coutVus + cout[sommetCrt][prochainSommet]) + duree[prochainSommet], cout, duree, tpsDebut, tpsLimite, tempsMini, tempsMax);
		        	vus.remove(prochainSommet);
		        	nonVus.add(prochainSommet);
				}
	        }	    
	    }
	}
	
	//Juste pour heritage, a suppr
	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return new IteratorSeq(nonVus, sommetCrt, cout);
	}
	
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree, int[] tempsMini, int coutVus) {
		return new IteratorSeq(nonVus, sommetCrt, cout, tempsMini, coutVus);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {

		int bound = 0;
		
		int valMini1 = cout[sommetCourant][0];
		int valMini2 = Integer.MAX_VALUE;
		
		for(Integer s: nonVus)
		{
			if(cout[sommetCourant][s] < valMini1)
			{
				valMini1 = cout[sommetCourant][s];
			}
			//retour a l entrepot
			if(cout[s][0] < valMini2)
			{
				valMini2 = cout[s][0];	
			}
			for(Integer s2: nonVus)
			{
				if(cout[s][s2] < valMini2 && s != s2)
				{
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
