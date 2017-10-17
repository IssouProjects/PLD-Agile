package org.apache.project.vue;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class MapContainer extends Pane{
	
	private MapDisplay mapDisplay;
	
	Rectangle clipRectangle;
	
	public MapContainer(int height, int width, Scene scene){
		setPrefSize(height, width);
		setStyle("-fx-background-color: #dfdfdf;");
		
		// used to define the area in which the map is Displayed
		clipRectangle = new Rectangle(200, 200);
		// it must always has the same size as the map container
		clipRectangle.widthProperty().bind(widthProperty()); 
		clipRectangle.heightProperty().bind(heightProperty());
		this.setClip(clipRectangle);
		
		mapDisplay = new MapDisplay(height,width);
        
        getChildren().add(mapDisplay);
        
        // we add user controls for the map: zoom in with the scrollwheel, pan with the mouse
        MapGestures mapGestures = new MapGestures(mapDisplay);
        scene.addEventFilter( MouseEvent.MOUSE_PRESSED, mapGestures.getOnMousePressedEventHandler());
        scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, mapGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter( ScrollEvent.ANY, mapGestures.getOnScrollEventHandler());

	}
	
	public void resetMapZoom() {
		mapDisplay.setScale(1.0d);
		mapDisplay.setScale(1.0d);
	}

	public MapDisplay getMapDisplay() {
		return mapDisplay;
	}
	
	public void resetMapPosition(){
		mapDisplay.setTranslateX(0d);
		mapDisplay.setTranslateY(0d);
	}
}
