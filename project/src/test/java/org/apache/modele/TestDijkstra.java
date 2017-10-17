package org.apache.modele;

import java.util.List;
import java.util.ArrayList;

import org.apache.project.modele.*;

import junit.framework.TestCase;

public class TestDijkstra extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testAlgoDijkstra() throws Exception{
		Intersection A = new Intersection((long)1, (long)3, (long)3);
		Intersection B = new Intersection((long)2, (long)1, (long)2);
		Intersection C = new Intersection((long)3, (long)2, (long)2);
		Intersection D = new Intersection((long)4, (long)3, (long)2);
		Intersection E = new Intersection((long)5, (long)2, (long)1);
		Troncon AB = new Troncon((double)50000,A,B,"T1");
		Troncon AC = new Troncon((double)30000,A,C,"T2");
		Troncon AD = new Troncon((double)20000,A,D,"T3");
		Troncon AE = new Troncon((double)100000,A,E,"T6");
		Troncon BE = new Troncon((double)30000,B,E,"T4");
		Troncon CE = new Troncon((double)40000,C,E,"T5");
		Troncon DE = new Troncon((double)70000,D,E,"T7");
		Troncon EA = new Troncon((double)70000,E,A,"T8");
		Troncon CA = new Troncon((double)70000,C,A,"T9");
		A.ajouterTronconPartant(AB);
		A.ajouterTronconPartant(AC);
		A.ajouterTronconPartant(AD);
		A.ajouterTronconPartant(AE);
		B.ajouterTronconPartant(BE);
		C.ajouterTronconPartant(CE);
		D.ajouterTronconPartant(DE);
		E.ajouterTronconPartant(EA);
		C.ajouterTronconPartant(CA);
		ArrayList<Intersection> plan_inter = new ArrayList<Intersection>();
		ArrayList<Intersection> livraison_inter = new ArrayList<Intersection>();
		plan_inter.add(A);
		plan_inter.add(B);
		plan_inter.add(C);
		plan_inter.add(D);
		plan_inter.add(E);
		livraison_inter.add(A);
		livraison_inter.add(C);
		livraison_inter.add(E);
		
		List<Chemin> res = Dijkstra.PrincipalDijkstra(plan_inter, livraison_inter);
		System.out.println(res);
		System.out.println("test 1");
		System.out.println(res.get(0).getTroncons());
		//System.out.println(res.get(0).getTroncons().get(0).getNomRue());
		//System.out.println(res.get(0).getTroncons().get(1).getNomRue());
		System.out.println("test 3");		
		//System.out.println(res.get(1).getTroncons());
		System.out.println("test 3");		
				
		

	}
}
