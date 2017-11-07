package org.apache.modele;

import static org.junit.Assert.*;

import java.sql.Time;

import org.apache.project.modele.PlageHoraire;
import org.junit.Test;

public class TestPlageHoraire {

	@SuppressWarnings("deprecation")
	@Test
	public void TestCalculerHeureArrivee() {
		assertEquals("01:03:19", PlageHoraire.calculerHeureArrivee(new Time(23,59,59), 200).toString());
	}
	
	@Test
	public void TestAfficherMillisecondesEnHeuresEtMinutes() {
		assertEquals("80h 18min", PlageHoraire.afficherMillisecondesEnHeuresEtMinutes(289093981));
	}
	
	@Test
	public void TestSecondesEnLocalTime() {
		assertEquals("03:07:14", PlageHoraire.secondesEnLocalTime(11234).toString());
		assertEquals("20:23:29", PlageHoraire.secondesEnLocalTime(73409).toString());
		assertEquals("04:46:39", PlageHoraire.secondesEnLocalTime(99999).toString());
	}

}
