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

    OperationHistory dataPath;

    public abstract void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth);

    public abstract void resizeShape(Node node, Pane pane);

    public abstract void setCursor(Node node, double stateX, double stateY);

    public void moveShape(Node node, Pane pane) {
	resetMouseEvents(pane);

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {

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
		    dataPath.shapeDrawn(pane);
		}

	    }
	};

	node.setOnMousePressed(mouseHandler);
	node.setOnMouseDragged(mouseHandler);
	node.setOnMouseReleased(mouseHandler);

    }

    public void fillShape(Pane pane, Color paintColor, Node component) {

	resetMouseEvents(pane);

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
		// Checking the type of the component in order be filled
		// with the chosen color
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    if (component instanceof Polygon) {
			Polygon p = (Polygon) component;
			p.setFill(paintColor);
		    } else if (component instanceof Rectangle) {
			Rectangle r = (Rectangle) component;
			r.setFill(paintColor);
		    } else if (component instanceof Ellipse) {
			Ellipse e = (Ellipse) component;
			e.setFill(paintColor);
		    }
		    if (!(component instanceof Line)) {
			dataPath.shapeDrawn(pane);
		    }

		}

	    }
	};
	component.setOnMousePressed(mouseHandler);
    }

    public void deleteShape(Pane pane, Node component) {
	resetMouseEvents(pane);

	if (component instanceof Line || component instanceof Polygon || component instanceof Rectangle
		|| component instanceof Ellipse) {
	    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent mouseEvent) {
		    if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
			pane.getChildren().remove(component);
			dataPath.shapeDrawn(pane);
		    }
		}
	    };
	    component.setOnMousePressed(mouseHandler);

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
	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    // Do Nothing...
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    // Do Nothing...
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    // Do Nothing...
		}
	    }
	};
	pane.setOnMousePressed(mouseHandler);
	pane.setOnMouseDragged(mouseHandler);
	pane.setOnMouseReleased(mouseHandler);
    }

    public void dataInitialize(OperationHistory cloneData) {
	dataPath = cloneData;
    }

}
