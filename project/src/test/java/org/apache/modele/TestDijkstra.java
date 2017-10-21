package org.apache.modele;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.project.modele.*;
import org.junit.Test;

public class TestDijkstra {

	@Test(timeout=1000)
	public void testAlgoDijkstra() throws Exception {
		Intersection A = new Intersection((long) 1, (long) 3, (long) 3);
		Intersection B = new Intersection((long) 2, (long) 1, (long) 2);
		Intersection C = new Intersection((long) 3, (long) 2, (long) 2);
		Intersection D = new Intersection((long) 4, (long) 3, (long) 2);
		Intersection E = new Intersection((long) 5, (long) 2, (long) 1);
		Troncon AB = new Troncon(50000, A, B, "T1");
		Troncon AC = new Troncon(30000, A, C, "T2");
		Troncon AD = new Troncon(20000, A, D, "T3");
		Troncon AE = new Troncon(100000, A, E, "T6");
		Troncon BE = new Troncon(30000, B, E, "T4");
		Troncon CE = new Troncon(40000, C, E, "T5");
		Troncon DE = new Troncon(70000, D, E, "T7");
		Troncon EA = new Troncon(70000, E, A, "T8");
		Troncon CA = new Troncon(70000, C, A, "T9");
		Troncon EC = new Troncon(100000, E, C, "T10");
		A.ajouterTronconPartant(AB);
		A.ajouterTronconPartant(AC);
		A.ajouterTronconPartant(AD);
		A.ajouterTronconPartant(AE);
		B.ajouterTronconPartant(BE);
		C.ajouterTronconPartant(CE);
		D.ajouterTronconPartant(DE);
		E.ajouterTronconPartant(EA);
		E.ajouterTronconPartant(EC);
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

		List<Chemin> output = Dijkstra.calculDijkstra(plan_inter, livraison_inter);
		
		//Taille de output ok
		assertEquals(output.size(), 6);
		
		//Verification chemins
		assertEquals(1, output.get(0).getTroncons().size());
		assertEquals("T2", output.get(0).getTroncons().get(0).getNomRue());
		assertEquals("T2", output.get(1).getTroncons().get(0).getNomRue());
		assertEquals("T5", output.get(1).getTroncons().get(1).getNomRue());
		assertEquals("T9", output.get(2).getTroncons().get(0).getNomRue());
		assertEquals("T5", output.get(3).getTroncons().get(0).getNomRue());
		assertEquals("T8", output.get(4).getTroncons().get(0).getNomRue());
		assertEquals("T10", output.get(5).getTroncons().get(0).getNomRue());
		
	}
}
