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
    Polygon p;
    double x1;
    double y1;
    double x2;
    double y2;
    double x3;
    double y3;
    double stateX;
    double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth) {

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    double x[] = new double[3];
	    double y[] = new double[3];
	    List<Double> values = new ArrayList<Double>();
	    int count = 0;
	    boolean drawShape = true;
	    Line l = new Line();

	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    if (drawShape) {
			x[count] = mouseEvent.getX();
			y[count] = mouseEvent.getY();
			l.setStroke(pickColor.getValue());
			l.setStrokeWidth(lineWidth.getValue());
		    }
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    if (drawShape) {
			values.add(x[count]);
			values.add(y[count]);
			if (count == 0) {
			    l.setStartX(x[0]);
			    l.setStartY(y[0]);
			    l.setEndX(x[0]);
			    l.setEndY(y[0]);
			    l.setStroke(pickColor.getValue());
			    l.setStrokeWidth(lineWidth.getValue());
			    pane.getChildren().add(l);
			} else if (count == 1) {
			    pane.getChildren().remove(l);
			    l.setStroke(pickColor.getValue());
			    l.setStrokeWidth(lineWidth.getValue());
			    l.setStartX(x[0]);
			    l.setStartY(y[0]);
			    l.setEndX(x[1]);
			    l.setEndY(y[1]);
			    pane.getChildren().add(l);
			} else if (count == 2) {
			    pane.getChildren().remove(l);
			    Polygon triangle = new Polygon();

			    triangle.getPoints().addAll(values);
			    triangle.setFill(Color.TRANSPARENT);
			    triangle.setStroke(pickColor.getValue());
			    triangle.setStrokeWidth(lineWidth.getValue());
			    pane.getChildren().add(triangle);
			    dataPath.shapeDrawn(pane);
			}
			count++;
			if (count == 3) {
			    count = 0;
			    values.clear();
			}

		    }

		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    // Do Nothing...
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
	p = (Polygon) node;
	ObservableList<Double> x;
	x = p.getPoints();
	System.out.println(x);
	x1 = x.get(0);
	y1 = x.get(1);
	x2 = x.get(2);
	y2 = x.get(3);
	x3 = x.get(4);
	y3 = x.get(5);

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
		    
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
		    setCursor(node, mouseEvent.getX(), mouseEvent.getY());
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    setShapeStyleOnDrag(node);
		    ObservableList<Double> x;
		    x = p.getPoints();
		    x1 = x.get(0);
		    y1 = x.get(1);
		    x2 = x.get(2);
		    y2 = x.get(3);
		    x3 = x.get(4);
		    y3 = x.get(5);
		    stateX = mouseEvent.getX();
		    stateY = mouseEvent.getY();
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    // Start
		    if ((stateX > x1 - 10 && stateX < x1 + 10) || (stateY > y1 - 10 && stateY < y1 + 10)) {
			p.getPoints().clear();
			p.getPoints().addAll(mouseEvent.getX(), mouseEvent.getY(), x2, y2, x3, y3);
		    }
		    // Middle
		    if ((stateX > x2 - 10 && stateX < x2 + 10) || (stateY > y2 - 10 && stateY < y2 + 10)) {
			p.getPoints().clear();
			p.getPoints().addAll(x1, y1, mouseEvent.getX(), mouseEvent.getY(), x3, y3);
		    }
		    // End
		    if ((stateX > x3 - 10 && stateX < x3 + 10) || (stateY > y3 - 10 && stateY < y3 + 10)) {
			p.getPoints().clear();
			p.getPoints().addAll(x1, y1, x2, y2, mouseEvent.getX(), mouseEvent.getY());
		    }
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
	node.setOnMouseMoved(mouseHandler);
    }

    @Override
    public void setCursor(Node node, double stateX, double stateY) {
	ObservableList<Double> x;
	x = p.getPoints();
	x1 = x.get(0);
	y1 = x.get(1);
	x2 = x.get(2);
	y2 = x.get(3);
	x3 = x.get(4);
	y3 = x.get(5);

	if ((stateX > x1 - 10 && stateX < x1 + 10) || (stateY > y1 - 10 && stateY < y1 + 10)) {
	    node.setCursor(Cursor.N_RESIZE);
	} else if ((stateX > x2 - 10 && stateX < x2 + 10) || (stateY > y2 - 10 && stateY < y2 + 10)) {
	    node.setCursor(Cursor.N_RESIZE);
	} else if ((stateX > x3 - 10 && stateX < x3 + 10) || (stateY > y3 - 10 && stateY < y3 + 10)) {
	    node.setCursor(Cursor.N_RESIZE);
	} else {
	    node.setCursor(Cursor.DEFAULT);
	}
    }
    
}
