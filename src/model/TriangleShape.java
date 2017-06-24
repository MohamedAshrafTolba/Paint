package model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class TriangleShape extends PolygonShape {
    
	private Polygon polygon;
    private double xCoordinate1;
    private double yCoordinate1;
    private double xCoordinate2;
    private double yCoordinate2;
    private double xCoordinate3;
    private double yCoordinate3;
    private double stateX;
    private double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker colorPicker, Slider lineWidth) {
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			double x[] = new double[3];
			double y[] = new double[3];
			List<Double> values = new ArrayList<>();
			int count = 0;
			boolean drawShape = true;
			Line line = new Line();
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					if (drawShape) {
						x[count] = mouseEvent.getX();
						y[count] = mouseEvent.getY();
						line.setStroke(colorPicker.getValue());
						line.setStrokeWidth(lineWidth.getValue());
					}
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
					if (drawShape) {
						values.add(x[count]);
						values.add(y[count]);
						if (count == 0) {
							line.setStartX(x[0]);
							line.setStartY(y[0]);
							line.setEndX(x[0]);
							line.setEndY(y[0]);
							line.setStroke(colorPicker.getValue());
							line.setStrokeWidth(lineWidth.getValue());
							pane.getChildren().add(line);
						} else if (count == 1) {
							pane.getChildren().remove(line);
							line.setStroke(colorPicker.getValue());
							line.setStrokeWidth(lineWidth.getValue());
							line.setStartX(x[0]);
							line.setStartY(y[0]);
							line.setEndX(x[1]);
							line.setEndY(y[1]);
							pane.getChildren().add(line);
						} else if (count == 2) {
							pane.getChildren().remove(line);
							Polygon triangle = new Polygon();
							triangle.getPoints().addAll(values);
							triangle.setFill(Color.TRANSPARENT);
							triangle.setStroke(colorPicker.getValue());
							triangle.setStrokeWidth(lineWidth.getValue());
							pane.getChildren().add(triangle);
							operationHistory.shapeDrawn(pane);
						}
						count++;
						if (count == 3) {
							count = 0;
							values.clear();
						}
					}
				}
			}
		};
		pane.setOnMousePressed(mouseHandler);
		pane.setOnMouseReleased(mouseHandler);
		pane.setOnMouseDragged(mouseHandler);
    }

    @Override
    public void resizeShape(Node node, Pane pane) {
		resetMouseEvents(pane);
		polygon = (Polygon) node;
		ObservableList<Double> points;
		points = polygon.getPoints();
		xCoordinate1 = points.get(0);
		yCoordinate1 = points.get(1);
		xCoordinate2 = points.get(2);
		yCoordinate2 = points.get(3);
		xCoordinate3 = points.get(4);
		yCoordinate3 = points.get(5);
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
				setCursor(node, mouseEvent.getX(), mouseEvent.getY());
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				setShapeStyleOnDrag(node);
				ObservableList<Double> points1;
				points1 = polygon.getPoints();
				xCoordinate1 = points1.get(0);
				yCoordinate1 = points1.get(1);
				xCoordinate2 = points1.get(2);
				yCoordinate2 = points1.get(3);
				xCoordinate3 = points1.get(4);
				yCoordinate3 = points1.get(5);
				stateX = mouseEvent.getX();
				stateY = mouseEvent.getY();
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				// Start
				if ((stateX > xCoordinate1 - 10 && stateX < xCoordinate1 + 10)
						|| (stateY > yCoordinate1 - 10 && stateY < yCoordinate1 + 10)) {
				polygon.getPoints().clear();
				polygon.getPoints().addAll(mouseEvent.getX(), mouseEvent.getY(),
						xCoordinate2, yCoordinate2, xCoordinate3, yCoordinate3);
				}
				// Middle
				if ((stateX > xCoordinate2 - 10 && stateX < xCoordinate2 + 10)
						|| (stateY > yCoordinate2 - 10 && stateY < yCoordinate2 + 10)) {
				polygon.getPoints().clear();
				polygon.getPoints().addAll(xCoordinate1, yCoordinate1, mouseEvent.getX(),
						mouseEvent.getY(), xCoordinate3, yCoordinate3);
				}
				// End
				if ((stateX > xCoordinate3 - 10 && stateX < xCoordinate3 + 10)
						|| (stateY > yCoordinate3 - 10 && stateY < yCoordinate3 + 10)) {
				polygon.getPoints().clear();
				polygon.getPoints().addAll(xCoordinate1, yCoordinate1, xCoordinate2,
						yCoordinate2, mouseEvent.getX(), mouseEvent.getY());
				}
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
				setShapeStyleOnRelease(node);
				operationHistory.shapeDrawn(pane);
        	}
		};
		node.setOnMousePressed(mouseHandler);
		node.setOnMouseDragged(mouseHandler);
		node.setOnMouseReleased(mouseHandler);
		node.setOnMouseMoved(mouseHandler);
    }

    @Override
    public void setCursor(Node node, double stateX, double stateY) {
		ObservableList<Double> points;
		points = polygon.getPoints();
		xCoordinate1 = points.get(0);
		yCoordinate1 = points.get(1);
		xCoordinate2 = points.get(2);
		yCoordinate2 = points.get(3);
		xCoordinate3 = points.get(4);
		yCoordinate3 = points.get(5);
		if ((stateX > xCoordinate1 - 10 && stateX < xCoordinate1 + 10)
				|| (stateY > yCoordinate1 - 10 && stateY < yCoordinate1 + 10)) {
			node.setCursor(Cursor.N_RESIZE);
		} else if ((stateX > xCoordinate2 - 10 && stateX < xCoordinate2 + 10)
				|| (stateY > yCoordinate2 - 10 && stateY < yCoordinate2 + 10)) {
			node.setCursor(Cursor.N_RESIZE);
		} else if ((stateX > xCoordinate3 - 10 && stateX < xCoordinate3 + 10)
				|| (stateY > yCoordinate3 - 10 && stateY < yCoordinate3 + 10)) {
			node.setCursor(Cursor.N_RESIZE);
		} else {
			node.setCursor(Cursor.DEFAULT);
		}
    }
}