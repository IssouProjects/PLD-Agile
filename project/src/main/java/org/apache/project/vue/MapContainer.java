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
	
	private MapDisplay map;
	
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
		
		map = new MapDisplay(height,width);
		
		// example of a street
        Line line = new Line(10,200,100,10);
        line.setStrokeWidth(3);
        line.setStroke(Color.WHITE);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        map.getChildren().add(line);
        
        getChildren().add(map);
        
        // we add user controls for the map: zoom in with the scrollwheel, pan with the mouse
        MapGestures mapGestures = new MapGestures(map);
        scene.addEventFilter( MouseEvent.MOUSE_PRESSED, mapGestures.getOnMousePressedEventHandler());
        scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, mapGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter( ScrollEvent.ANY, mapGestures.getOnScrollEventHandler());

	}
	
	public void resetMapZoom() {
		map.setScale(1.0d);
		map.setScale(1.0d);
	}

	public MapDisplay getMap() {
		return map;
	}
	
	public void resetMapPosition(){
		map.setTranslateX(0d);
		map.setTranslateY(0d);
	}
}
