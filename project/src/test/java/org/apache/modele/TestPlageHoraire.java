package org.apache.modele;

import static org.junit.Assert.*;

import org.apache.project.modele.PlageHoraire;
import org.junit.Test;

public class TestPlageHoraire {

	@Test
	public void TestSecondesEnLocalTime() {
		assertEquals("03:07:14", PlageHoraire.secondesEnLocalTime(11234).toString());
		assertEquals("20:23:29", PlageHoraire.secondesEnLocalTime(73409).toString());
	}

}
