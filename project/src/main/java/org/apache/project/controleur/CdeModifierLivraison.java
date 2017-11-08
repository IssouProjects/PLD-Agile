package org.apache.project.controleur;

import java.sql.Time;

import org.apache.project.modele.Livraison;
import org.apache.project.modele.PlageHoraire;
import org.apache.project.modele.Tournee;

/**
 *
 */
public class CdeModifierLivraison implements Commande {

	private Tournee tournee;
	private Livraison livraison;
	private PlageHoraire nouvellePlage;
	private PlageHoraire anciennePlage;
	private int ancienneDuree;
	private int nouvelleDuree;

	/**
	 * @param livraison
   * @param tournee
	 * @param heureDebut
	 * @param heureFin
	 * @param duree
	 */
	public CdeModifierLivraison(Livraison livraison, Tournee tournee, Time heureDebut, Time heureFin, Integer duree) {
		this.livraison = livraison;
		this.tournee = tournee;
		
		if (heureDebut == null || heureFin == null) {
			this.nouvellePlage = null;
		} else {
			this.nouvellePlage = new PlageHoraire(heureDebut, heureFin);
		}

		if (livraison.getPlageHoraire() == null) {
			this.anciennePlage = null;
		} else {
			this.anciennePlage = new PlageHoraire(livraison.getPlageHoraire().getDebut(),
					livraison.getPlageHoraire().getFin());
		}

		this.ancienneDuree = livraison.getDuree();
		this.nouvelleDuree = duree;
	}

	@Override
	public int doCommande() {
		if (livraison.getPlageHoraire() == null) {
			livraison.setPlageHoraire(nouvellePlage);
		} else {
			if (nouvellePlage == null) {
				livraison.setPlageHoraire(null);
			} else {
				livraison.getPlageHoraire().setDebut(nouvellePlage.getDebut());
				livraison.getPlageHoraire().setFin(nouvellePlage.getFin());
			}
		}
		livraison.setDuree(nouvelleDuree);
		tournee.calculerDureeTotale();
		return 0;
	}

	@Override
	public void undoCommande() {
		if (anciennePlage == null) {
			livraison.setPlageHoraire(null);
		} else {
			if (livraison.getPlageHoraire() == null) {
				livraison.setPlageHoraire(anciennePlage);
			} else {
				livraison.getPlageHoraire().setDebut(anciennePlage.getDebut());
				livraison.getPlageHoraire().setFin(anciennePlage.getFin());
			}
		}
		livraison.setDuree(ancienneDuree);
		tournee.calculerDureeTotale();
	}

}
