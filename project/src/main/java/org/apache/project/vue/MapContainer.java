package org.apache.project.vue;

import org.apache.project.vue.MapGestures.SelectionMode;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class MapContainer extends Pane {

	private MapDisplay mapDisplay;
	
	MapGestures mapGestures;
	Rectangle clipRectangle;

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

	public MapDisplay getMapDisplay() {
		return mapDisplay;
	}

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
	
	public void setEcouteurDeMap(EcouteurDeMap edm) {
		mapGestures.setEcouteurDeMap(edm);
	}
	
	public void setSelectionMode(SelectionMode mode) {
		switch(mode) {
		case Intersection:
			mapGestures.setSelectionMode(mode);
			break;
		case Troncon:
			mapGestures.setSelectionMode(mode);
			break;
		}
	}
}
