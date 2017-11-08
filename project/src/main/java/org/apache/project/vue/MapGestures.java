package org.apache.project.vue;

import java.util.LinkedList;
import java.util.List;

import org.apache.project.modele.Intersection;
import org.apache.project.modele.Livraison;
import org.apache.project.modele.Troncon;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 */
public class MapGestures {
	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .001d;
	private static final double CLIC_COLLISION_RADIUS = 2000;

	public enum SelectionMode {
		Troncon, Intersection
	}

	private SelectionMode selectionMode = SelectionMode.Intersection;

	private DragContext sceneDragContext = new DragContext();

	MapDisplay map;
	EcouteurDeMap edm = null;

	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

			// right mouse button => panning
			if (!event.isPrimaryButtonDown())
				return;

			// CLICK TARGET HANDLING

			// first, we look if the user clicked EXACTLY on a node

			boolean actionDone = false;
			if (event.getTarget() instanceof Node) {
				Node exactTarget = (Node) event.getTarget();
				handleClickedNode(exactTarget);
			}

			// If it is not the case, we look at the nearest nodes

			if (!actionDone) {

				List<Node> targets = null;
				targets = getNearestCircles(event);
				targets.addAll(getNearestLines(event));

				// we extract 3 candidates for the click
				Node livraison = null;
				Node intersection = null;
				Node troncon = null;

				for (Node target : targets) {
					Object obj = target.getUserData();
					if (obj instanceof Livraison) {
						livraison = target;
					} else if (obj instanceof Intersection) {
						intersection = target;
					} else if (obj instanceof Troncon) {
						troncon = target;
					}
				}

				switch (selectionMode) {
				case Intersection:
					if (livraison != null)
						handleClickedNode(livraison);
					else if (intersection != null)
						handleClickedNode(intersection);
					break;
				case Troncon:
					if (livraison != null)
						handleClickedNode(livraison);
					else if (troncon != null)
						handleClickedNode(troncon);
					break;
				}

			}

			// DRAGGING HANDLING
			sceneDragContext.mouseAnchorX = event.getSceneX();
			sceneDragContext.mouseAnchorY = event.getSceneY();

			sceneDragContext.translateAnchorX = map.getTranslateX();
			sceneDragContext.translateAnchorY = map.getTranslateY();
		}

	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

			if (!event.isPrimaryButtonDown())
				return;

			map.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
			map.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

			event.consume();
		}
	};

	/**
	 * Mouse wheel handler: zoom to pivot point
	 */
	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {

			double delta = 1.2;

			double scale = map.getScale(); // currently we only use Y, same value is used for X
			double oldScale = scale;

			if (event.getDeltaY() < 0)
				scale /= delta;
			else
				scale *= delta;

			scale = clamp(scale, MIN_SCALE, MAX_SCALE);

			double f = (scale / oldScale) - 1;

			double dx = (event.getSceneX()
					- (map.getBoundsInParent().getWidth() / 2 + map.getBoundsInParent().getMinX()));
			double dy = (event.getSceneY()
					- (map.getBoundsInParent().getHeight() / 2 + map.getBoundsInParent().getMinY()));

			map.setScale(scale);

			// note: pivot value must be untransformed, i. e. without scaling
			map.setPivot(f * dx, f * dy);

			event.consume();

		}

	};

	/**
	 * @param canvas
	 */
	public MapGestures(MapDisplay canvas) {
		this.map = canvas;
	}

	/**
	 * @return
	 */
	public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	/**
	 * @return
	 */
	public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	/**
	 * @return
	 */
	public EventHandler<ScrollEvent> getOnScrollEventHandler() {
		return onScrollEventHandler;
	}

	/**
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static double clamp(double value, double min, double max) {

		if (Double.compare(value, min) < 0)
			return min;

		if (Double.compare(value, max) > 0)
			return max;

		return value;
	}

	/**
	 * @param edm
	 */
	public void setEcouteurDeMap(EcouteurDeMap edm) {
		this.edm = edm;
	}

	/**
	 * @param target
	 * @return
	 */
	private boolean handleClickedNode(Node target) {
		Object obj = target.getUserData();
		if (obj instanceof Livraison) {
			edm.onLivraisonClicked((Livraison) obj);
			return true;
		} else if (obj instanceof Intersection) {
			edm.onIntersectionClicked((Intersection) obj);
			return true;
		} else if (obj instanceof Troncon) {
			edm.onTronconClicked((Troncon) obj);
			return true;
		}
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	private List<Node> getNearestCircles(MouseEvent event) {

		// simple bruteforce test on all nodes
		if (map.getChildren().isEmpty())
			return null;

		List<Node> nodes = map.getChildren();
		List<Node> closestNodes = new LinkedList<Node>();

		// we translate mouse coordinate into map coordinates
		double coordX = ((event.getSceneX() - (map.localToScene(map.getBoundsInLocal()).getMinX())) * 1d
				/ map.getScale());
		double coordY = ((event.getSceneY() - (map.localToScene(map.getBoundsInLocal()).getMinY())) * 1d
				/ map.getScale());

		Circle circle = new Circle();
		circle.setCenterX(coordX);
		circle.setCenterY(coordY);
		circle.setRadius(CLIC_COLLISION_RADIUS);
		map.getChildren().add(circle);

		for (Node node : nodes) {
			if (node.getBoundsInLocal().intersects(circle.getBoundsInLocal()) && node instanceof Circle) {
				closestNodes.add(node);
			}
		}

		Circle closestCircle = null;
		List<Node> closestCircles = new LinkedList<Node>();

		double distance = Double.MAX_VALUE;
		double newDist = 0.0d;

		for (Node node : closestNodes) {

			// closest circle: we get circle with the center which is the closest to our
			// coordinates
			if (node instanceof Circle && node != circle) {
				Circle test = (Circle) node;
				newDist = ((test.getCenterX() - coordX) * (test.getCenterX() - coordX)
						+ (test.getCenterY() - coordY) * (test.getCenterY() - coordY));
				if (newDist < distance) {
					distance = newDist;
					closestCircle = test;
				} else if (newDist == distance) {
					closestCircles.add(test);
				}
			}
		}

		if (closestCircle != null) {
			closestCircles.add(closestCircle);
		}

		map.getChildren().remove(circle);

		return closestCircles;
	}

	/**
	 * @param event
	 * @return
	 */
	private List<Node> getNearestLines(MouseEvent event) {
		// simple bruteforce test on all nodes
		if (map.getChildren().isEmpty())
			return null;

		List<Node> nodes = map.getChildren();

		List<Node> closestNodes = new LinkedList<Node>();

		// we translate mouse coordinate into map coordinates
		double coordX = ((event.getSceneX() - (map.localToScene(map.getBoundsInLocal()).getMinX())) * 1d
				/ map.getScale());
		double coordY = ((event.getSceneY() - (map.localToScene(map.getBoundsInLocal()).getMinY())) * 1d
				/ map.getScale());

		Circle circle = new Circle();
		circle.setCenterX(coordX);
		circle.setCenterY(coordY);
		circle.setRadius(CLIC_COLLISION_RADIUS);
		map.getChildren().add(circle);

		for (Node node : nodes) {
			if (node.getBoundsInLocal().intersects(circle.getBoundsInLocal()) && node instanceof Line) {
				closestNodes.add(node);
			}
		}

		Line closestLine = null;
		List<Node> closestLines = new LinkedList<Node>();

		double lineDistance = Double.MAX_VALUE;
		double newLineDist = 0.0d;

		for (Node node : closestNodes) {

			// closest line: detailed explanation here:
			// mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
			// + weird trigonometry trick to eliminate lines
			Line test = (Line) node;

			double CA2 = (test.getStartX() - coordX) * (test.getStartX() - coordX)
					+ (test.getStartY() - coordY) * (test.getStartY() - coordY);
			double AB2 = (test.getStartX() - test.getEndX()) * (test.getStartX() - test.getEndX())
					+ (test.getStartY() - test.getEndY()) * (test.getStartY() - test.getEndY());
			double BC2 = (coordX - test.getEndX()) * (coordX - test.getEndX())
					+ (coordY - test.getEndY()) * (coordY - test.getEndY());

			double firstTest = (CA2 + AB2 - BC2) / (2 * Math.sqrt(CA2 * AB2));
			double secondTest = (BC2 + AB2 - CA2) / (2 * Math.sqrt(BC2 * AB2));

			if (firstTest > 0 && secondTest > 0) {
				newLineDist = Math
						.abs((test.getEndX() - test.getStartX()) * (test.getStartY() - coordY)
								- (test.getStartX() - coordX) * (test.getEndY() - test.getStartY()))
						/ Math.sqrt((test.getEndX() - test.getStartX()) * (test.getEndX() - test.getStartX())
								+ (test.getEndY() - test.getStartY()) * (test.getEndY() - test.getStartY()));
				if (newLineDist < lineDistance) {
					lineDistance = newLineDist;
					closestLine = test;
				} else if (newLineDist == lineDistance) {
					closestLines.add(test);
				}
			}
		}

		if (closestLine != null) {
			closestLines.add(closestLine);
		}

		map.getChildren().remove(circle);

		return closestLines;
	}

	/**
	 * @param mode
	 */
	public void setSelectionMode(SelectionMode mode) {
		selectionMode = mode;
	}
}

/**
 *
 */
class DragContext {

	double mouseAnchorX;
	double mouseAnchorY;

	double translateAnchorX;
	double translateAnchorY;

}
