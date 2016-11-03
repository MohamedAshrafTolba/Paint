package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class EllipseShape extends Shapes {

    private Ellipse e;
    private double intx;
    private double inty;
    private double intw;
    private double inth;
    private double stateX;
    private double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth) {
	SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectinitY = new SimpleDoubleProperty();
	SimpleDoubleProperty rectX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectY = new SimpleDoubleProperty();

	Ellipse ellipse = getNewEllipse();
	ellipse.radiusXProperty().bind(rectX.subtract(rectinitX));
	ellipse.radiusYProperty().bind(rectY.subtract(rectinitY));
	pane.getChildren().add(ellipse);

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    double intx, x, dx, inty, y, dy;

	    @Override
	    public void handle(MouseEvent mouseEvent) {

		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    ellipse.radiusXProperty().bind(rectX.subtract(rectinitX));
		    ellipse.radiusYProperty().bind(rectY.subtract(rectinitY));
		    pane.getChildren().add(ellipse);
		    ellipse.setStroke(pickColor.getValue());
		    ellipse.setStrokeWidth(lineWidth.getValue());
		    ellipse.setCenterX(mouseEvent.getX());
		    ellipse.setCenterY(mouseEvent.getY());
		    intx = mouseEvent.getX();
		    inty = mouseEvent.getY();
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    x = mouseEvent.getX();
		    dx = x - intx;
		    y = mouseEvent.getY();
		    dy = y - inty;

		    // 2nd quad case
		    if (dx < 0 && dy < 0) {
			rectinitX.set(mouseEvent.getX());
			rectinitY.set(mouseEvent.getY());
			rectX.set(intx);
			rectY.set(inty);
		    }
		    // 1st quad case
		    else if (dy < 0) {
			rectinitX.set(intx);
			rectinitY.set(mouseEvent.getY());
			rectX.set(mouseEvent.getX());
			rectY.set(inty);
		    }
		    // 3rd quad case
		    else if (dx < 0) {
			rectinitX.set(mouseEvent.getX());
			rectinitY.set(inty);
			rectX.set(intx);
			rectY.set(mouseEvent.getY());
		    }
		    // 4th quad case
		    else {
			rectX.set(mouseEvent.getX());
			rectY.set(mouseEvent.getY());
			rectinitX.set(intx);
			rectinitY.set(inty);
		    }
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    // Clone the rectangle
		    Ellipse e = getNewEllipse();
		    e.setStroke(pickColor.getValue());
		    e.setStrokeWidth(lineWidth.getValue());
		    e.setCenterX(ellipse.getCenterX());
		    e.setCenterY(ellipse.getCenterY());
		    e.setRadiusX(ellipse.getRadiusX());
		    e.setRadiusY(ellipse.getRadiusY());
		    pane.getChildren().add(e);
		 // Hide the rectangle
		    rectX.set(0);
		    rectY.set(0);
		    pane.getChildren().remove(ellipse);
		    dataPath.shapeDrawn(pane);
		    
		}
	    }

	};

	pane.setOnMousePressed(mouseHandler);
	pane.setOnMouseDragged(mouseHandler);
	pane.setOnMouseReleased(mouseHandler);

	// System.out.println(size);
	// if (size < pane.getChildren().size()) {
	// dataPath.shapeDrawn(cloneEllipse);
	// }
    }

    public Ellipse getNewEllipse() {
	Ellipse e = new Ellipse();
	e.setFill(Color.TRANSPARENT);
	return e;
    }

    @Override
    public void resizeShape(Node node, Pane pane) {

	resetMouseEvents(pane);
	e = (Ellipse) node;
	intx = e.getCenterX() - e.getRadiusX();
	inty = e.getCenterY() - e.getRadiusY();
	intw = e.getRadiusX() * 2;
	inth = e.getRadiusY() * 2;

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
		    setCursor(node, mouseEvent.getX(), mouseEvent.getY());
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    setShapeStyleOnDrag(node);
		    intx = e.getCenterX() - e.getRadiusX();
		    inty = e.getCenterY() - e.getRadiusY();
		    intw = e.getRadiusX() * 2;
		    inth = e.getRadiusY() * 2;
		    stateX = mouseEvent.getX();
		    stateY = mouseEvent.getY();
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    // LEFT
		    if (stateX > intx - 10 && stateX < intx + 10) {
			if ((intw / 2) + intx - mouseEvent.getX() > 5) {
			    e.setRadiusX((intw / 2) + intx - mouseEvent.getX());
			}
		    }
		    // RIGHT
		    if (stateX > (intx + intw - 10) && stateX < (intx + intw + 10)) {
			if ((intw / 2) + mouseEvent.getX() - (intx + intw) > 5) {
			    e.setRadiusX((intw / 2) + mouseEvent.getX() - (intx + intw));
			}

		    }
		    // UP
		    if (stateY > inty - 10 && stateY < inty + 10) {
			if ((inth / 2) + inty - mouseEvent.getY() > 5) {
			    e.setRadiusY((inth / 2) + inty - mouseEvent.getY());
			}
		    }
		    // down
		    if (stateY > (inty + inth - 10) && stateY < (inty + inth + 10)) {
			if ((inth / 2) + mouseEvent.getY() - (inty + inth) > 5) {
			    e.setRadiusY((inth / 2) + mouseEvent.getY() - (inty + inth));
			}
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
	Ellipse e = (Ellipse) node;
	double intx = e.getCenterX() - e.getRadiusX();
	double inty = e.getCenterY() - e.getRadiusY();
	double intw = e.getRadiusX() * 2;
	double inth = e.getRadiusY() * 2;
	// Right
	if (stateX > intx - 3 && stateX < intx + 3) {
	    node.setCursor(Cursor.E_RESIZE);
	}
	// LEFT
	else if (stateX > (intx + intw - 3) && stateX < (intx + intw + 3)) {
	    node.setCursor(Cursor.W_RESIZE);
	}
	// UP
	else if (stateY > inty - 3 && stateY < inty + 3) {
	    node.setCursor(Cursor.N_RESIZE);
	}
	// down
	else if (stateY > (inty + inth - 3) && stateY < (inty + inth + 3)) {
	    node.setCursor(Cursor.S_RESIZE);
	} else {
	    node.setCursor(Cursor.DEFAULT);
	}

    }
    
    

}
