package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class LineSegment extends Shapes {
    Line l;
    double intx;
    double inty;
    double finalx;
    double finaly;
    double stateX;
    double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth) {
	SimpleDoubleProperty x = new SimpleDoubleProperty();
	SimpleDoubleProperty y = new SimpleDoubleProperty();
	Line line;
	line = getNewLine();

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    line.endXProperty().bind(x);
		    line.endYProperty().bind(y);
		    pane.getChildren().add(line);

		    line.setStroke(pickColor.getValue());
		    line.setStrokeWidth(lineWidth.getValue());
		    line.setStartX(mouseEvent.getX());
		    line.setStartY(mouseEvent.getY());
		    x.set(mouseEvent.getX());
		    y.set(mouseEvent.getY());

		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    x.set(mouseEvent.getX());
		    y.set(mouseEvent.getY());
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    // Clone the line
		    Line l = getNewLine();
		    l.setStroke(pickColor.getValue());
		    l.setStrokeWidth(lineWidth.getValue());
		    l.setStartX(line.getStartX());
		    l.setStartY(line.getStartY());
		    l.setEndX(line.getEndX());
		    l.setEndY(line.getEndY());
		    pane.getChildren().add(l);
		    
		    // System.out.println(l.getParent());
		    // Hide the previous
		    pane.getChildren().remove(line);
		    line.setStartX(0);
		    line.setStartY(0);
		    x.set(0);
		    y.set(0);
		    dataPath.shapeDrawn(pane);
		}
	    }
	};
	pane.setOnMousePressed(mouseHandler);
	pane.setOnMouseDragged(mouseHandler);
	pane.setOnMouseReleased(mouseHandler);
    }

    public Line getNewLine() {
	Line l = new Line();

	return l;
    }

    @Override
    public void resizeShape(Node node, Pane pane) {
	resetMouseEvents(pane);

	l = (Line) node;
	intx = l.getStartX();
	inty = l.getStartY();
	finalx = l.getEndX();
	finaly = l.getEndY();

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
		    setCursor(node, mouseEvent.getX(), mouseEvent.getY());
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    setShapeStyleOnDrag(node);
		    intx = l.getStartX();
		    inty = l.getStartY();
		    finalx = l.getEndX();
		    finaly = l.getEndY();
		    stateX = mouseEvent.getX();
		    stateY = mouseEvent.getY();
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    // Start
		    if ((stateX > intx - 10 && stateX < intx + 10) || (stateY > inty - 10 && stateY < inty + 10)) {
			l.setStartX(mouseEvent.getX());
			l.setStartY(mouseEvent.getY());
		    }
		    // END
		    if ((stateX > finalx - 10 && stateX < finalx + 10)
			    || (stateY > finaly - 10 && stateY < finaly + 10)) {
			l.setEndX(mouseEvent.getX());
			l.setEndY(mouseEvent.getY());
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
	Line l = (Line) node;
	intx = l.getStartX();
	inty = l.getStartY();
	finalx = l.getEndX();
	finaly = l.getEndY();
	// Right
	if ((stateX > intx - 3 && stateX < intx + 3) || (stateY > inty - 3 && stateY < inty + 3)) {
	    node.setCursor(Cursor.N_RESIZE);
	}
	// LEFT
	else if ((stateX > finalx - 3 && stateX < finalx + 3) || (stateY > finaly - 3 && stateY < finaly + 3)) {
	    node.setCursor(Cursor.N_RESIZE);
	} else {
	    node.setCursor(Cursor.DEFAULT);
	}

    }
    
    
    

}
