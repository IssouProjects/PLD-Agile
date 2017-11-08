package org.apache.project.vue;

import org.apache.project.vue.MapGestures.SelectionMode;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Représente l'affichage de la carte dans l'interface du logiciel Contient
 * l'objet
 * <tt>MapDisplay<tt> de visualisation de la carte, ainsi qu'un objet <tt>MapGestures</tt>
 * pour la gestion des événement de la souris liés au plan.
 *
 */
public class MapContainer extends Pane {

	private MapDisplay mapDisplay;

	MapGestures mapGestures;
	Rectangle clipRectangle;

	/**
	 * Crée un objet <tt>MapContainer</tt> à partir des dimensions de la zone du
	 * plan dans l'interface
	 * 
	 * @param height
	 *            Hauteur de la zone de visualisation de la carte
	 * @param width
	 *            Largeur de la zone de visualisation de la carte
	 */
	public MapContainer(int height, int width) {
		setPrefSize(height, width);
		setStyle("-fx-background-color: #b2b2b2;");

		// used to define the area in which the map is Displayed
		clipRectangle = new Rectangle(200, 200);
		// it must always has the same size as the map container
		clipRectangle.widthProperty().bind(widthProperty());
		clipRectangle.heightProperty().bind(heightProperty());
		this.setClip(clipRectangle);

		mapDisplay = new MapDisplay(height, width);

		getChildren().add(mapDisplay);
		fitMapInView();

		// we add user controls for the map: zoom in with the scrollwheel, pan with the
		// mouse
		mapGestures = new MapGestures(mapDisplay);
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, mapGestures.getOnMousePressedEventHandler());
		this.addEventFilter(MouseEvent.MOUSE_DRAGGED, mapGestures.getOnMouseDraggedEventHandler());
		this.addEventFilter(ScrollEvent.ANY, mapGestures.getOnScrollEventHandler());

		mapGestures.setSelectionMode(SelectionMode.Troncon);

	}

	/**
	 * Renvoie l'objet <tt>MapDisplay</tt> de la visualisation de la carte
	 * 
	 * @return La <tt>MapDisplay</tt> du plan
	 */
	public MapDisplay getMapDisplay() {
		return mapDisplay;
	}

	/**
	 * Permet de centrer la carte sur l'interface.
	 */
	public void fitMapInView() {

		// find new scale

		double deltaX = 0.0d;
		double deltaY = 0.0d;

		if (getHeight() < getWidth()) {
			mapDisplay.setScale(getHeight() / mapDisplay.getPrefHeight());
			deltaX = (getWidth() - mapDisplay.getPrefWidth() * mapDisplay.getScale()) / 2.0d;
		} else {
			mapDisplay.setScale(getWidth() / mapDisplay.getPrefWidth());
			deltaY = (getHeight() - mapDisplay.getPrefHeight() * mapDisplay.getScale()) / 2.0d;
		}

		// resetting its coordinates
		mapDisplay.setTranslateX(0.0);
		mapDisplay.setTranslateY(0.0);

		mapDisplay.setTranslateX(-mapDisplay.getBoundsInParent().getMinX() + deltaX);
		mapDisplay.setTranslateY(-mapDisplay.getBoundsInParent().getMinY() + deltaY);
	}

	/**
	 * Modifie l'objet <tt>EcouteurDeMap</tt> associé au plan
	 * 
	 * @param edm
	 *            Nouvelle instance de <tt>EcouteurDeMap</tt>
	 */
	public void setEcouteurDeMap(EcouteurDeMap edm) {
		mapGestures.setEcouteurDeMap(edm);
	}

	/**
	 * Modifie le mode de sélection des éléments du plan (Intersection ou Troncon)
	 * 
	 * @param mode
	 *            Nouveau mode de sélection (Intersection ou Troncon)
	 */
	public void setSelectionMode(SelectionMode mode) {
		switch (mode) {
		case Intersection:
			mapGestures.setSelectionMode(mode);
			break;
		case Troncon:
			mapGestures.setSelectionMode(mode);
			break;
		}
	}
}
