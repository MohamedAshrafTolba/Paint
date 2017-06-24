package model;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public abstract class Shapes {

    private double xCoordinate;
    private double yCoordinate;

    OperationHistory operationHistory;

    public abstract void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth);

    public abstract void resizeShape(Node node, Pane pane);

    public abstract void setCursor(Node node, double stateX, double stateY);

    public void moveShape(Node node, Pane pane) {
		resetMouseEvents(pane);
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				node.setCursor(Cursor.MOVE);
				setShapeStyleOnDrag(node);
				xCoordinate = mouseEvent.getX();
				yCoordinate = mouseEvent.getY();
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				node.setLayoutX(node.getLayoutX() + mouseEvent.getX() - xCoordinate);
				node.setLayoutY(node.getLayoutY() + mouseEvent.getY() - yCoordinate);
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
				setShapeStyleOnRelease(node);
				operationHistory.shapeDrawn(pane);
			}};
		node.setOnMousePressed(mouseHandler);
		node.setOnMouseDragged(mouseHandler);
		node.setOnMouseReleased(mouseHandler);
    }

    public void fillShape(Pane pane, Color paintColor, Node node) {
		resetMouseEvents(pane);
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
			if (node instanceof Polygon) {
				Polygon polygon = (Polygon) node;
				polygon.setFill(paintColor);
			} else if (node instanceof Rectangle) {
				Rectangle rectangle = (Rectangle) node;
				rectangle.setFill(paintColor);
			} else if (node instanceof Ellipse) {
				Ellipse ellipse = (Ellipse) node;
				ellipse.setFill(paintColor);
			}
			if (!(node instanceof Line)) {
				operationHistory.shapeDrawn(pane);
			}
		}};
		node.setOnMousePressed(mouseHandler);
    }

    public void deleteShape(Pane pane, Node node) {
		resetMouseEvents(pane);
		if (node instanceof Line || node instanceof Polygon || node instanceof Rectangle
			|| node instanceof Ellipse) {
			EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					pane.getChildren().remove(node);
					operationHistory.shapeDrawn(pane);
				}};
			node.setOnMousePressed(mouseHandler);
		}
    }

    void setShapeStyleOnDrag(Node node) {
		if (node instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) node;
			rectangle.setStyle("-fx-stroke-dash-offset: 15;-fx-stroke-dash-array: 12 2 4 2; -fx-stroke: darkblue; ");
		} else if (node instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) node;
			ellipse.setStyle("-fx-stroke-dash-offset: 15;-fx-stroke-dash-array: 12 2 4 2; -fx-stroke: darkblue;");
		} else if (node instanceof Polygon) {
			Polygon polygon = (Polygon) node;
			polygon.setStyle("-fx-stroke-dash-offset: 15;-fx-stroke-dash-array: 12 2 4 2; -fx-stroke: darkblue;");
		} else {
			Line line = (Line) node;
			line.setStyle("-fx-stroke-dash-offset: 15;-fx-stroke-dash-array: 12 2 4 2; -fx-stroke: darkblue;");
		}
    }

    void setShapeStyleOnRelease(Node node) {
		if (node instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) node;
			rectangle.setStyle("-fx-stroke-dash-offset: 0;");
		} else if (node instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) node;
			ellipse.setStyle("-fx-stroke-dash-offset: 0;");
		} else if (node instanceof Polygon) {
			Polygon polygon = (Polygon) node;
			polygon.setStyle("-fx-stroke-dash-offset: 0;");
		} else {
			Line line = (Line) node;
			line.setStyle("-fx-stroke-dash-offset: 0;");
		}
    }

    void resetMouseEvents(Pane pane) {
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            // Do Nothing...
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            // Do Nothing...
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            // Do Nothing...
        }};
		pane.setOnMousePressed(mouseHandler);
		pane.setOnMouseDragged(mouseHandler);
		pane.setOnMouseReleased(mouseHandler);
    }

    public void dataInitialize(OperationHistory cloneData) {
		operationHistory = cloneData;
    }

    void configureCursor(double initialX, double initialY,
						double initialW, double initialH,
						double stateX, double stateY,
						Node node) {
		if (stateX > initialX - 3 && stateX < initialX + 3) {
			node.setCursor(Cursor.E_RESIZE);
		}
		// Left
		else if (stateX > (initialX + initialW - 3) && stateX < (initialX + initialW + 3)) {
			node.setCursor(Cursor.W_RESIZE);
		}
		// Up
		else if (stateY > initialY - 3 && stateY < initialY + 3) {
			node.setCursor(Cursor.N_RESIZE);
		}
		// Down
		else if (stateY > (initialY + initialH - 3) && stateY < (initialY + initialH + 3)) {
			node.setCursor(Cursor.S_RESIZE);
		} else {
			node.setCursor(Cursor.DEFAULT);
		}
	}
}