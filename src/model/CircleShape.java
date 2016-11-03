package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;

public class CircleShape extends EllipseShape {

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
	SimpleDoubleProperty rectX = new SimpleDoubleProperty();

	Ellipse ellipse = getNewEllipse();

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    double intx, x, dx, inty, y, dy;

	    @Override
	    public void handle(MouseEvent mouseEvent) {

		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    mouseEvent.consume();
		    ellipse.radiusXProperty().bind(rectX.subtract(rectinitX));
		    ellipse.radiusYProperty().bind(rectX.subtract(rectinitX));
		    pane.getChildren().add(ellipse);

		    ellipse.setStroke(pickColor.getValue());
		    ellipse.setStrokeWidth(lineWidth.getValue());
		    ellipse.setCenterX(mouseEvent.getX());
		    ellipse.setCenterY(mouseEvent.getY());
		    intx = mouseEvent.getX();
		    inty = mouseEvent.getY();
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    mouseEvent.consume();
		    x = mouseEvent.getX();
		    dx = x - intx;
		    y = mouseEvent.getY();
		    dy = y - inty;

		    // 2nd quad case
		    if (dx < 0 && dy < 0) {
			rectinitX.set(mouseEvent.getX());
			rectX.set(intx);
		    }
		    // 1st quad case
		    else if (dy < 0) {
			rectinitX.set(intx);
			rectX.set(mouseEvent.getX());
		    }
		    // 3rd quad case
		    else if (dx < 0) {
			rectinitX.set(mouseEvent.getX());
			rectX.set(intx);
		    }
		    // 4th quad case
		    else {
			rectX.set(mouseEvent.getX());
			rectinitX.set(intx);
		    }
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    mouseEvent.consume();
		    // Clone the rectangle
		    Ellipse e = getNewEllipse();
		    e.setStroke(pickColor.getValue());
		    e.setStrokeWidth(lineWidth.getValue());
		    e.setCenterX(ellipse.getCenterX());
		    e.setCenterY(ellipse.getCenterY());
		    e.setRadiusX(ellipse.getRadiusX());
		    e.setRadiusY(ellipse.getRadiusY());
		    pane.getChildren().add(e);

		    rectX.set(0.0);
		    rectinitX.set(0.0);
		    pane.getChildren().remove(ellipse);
		    dataPath.shapeDrawn(pane);
		}
	    }

	};

	pane.setOnMousePressed(mouseHandler);
	pane.setOnMouseDragged(mouseHandler);
	pane.setOnMouseReleased(mouseHandler);

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
		// if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
		// mouseEvent.consume();
		//
		// }
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
		    setCursor(e, mouseEvent.getX(), mouseEvent.getY());
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    setShapeStyleOnDrag(e);
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
			    e.setRadiusY((intw / 2) + intx - mouseEvent.getX());
			}
		    }
		    // RIGHT
		    if (stateX > (intx + intw - 10) && stateX < (intx + intw + 10)) {
			if ((intw / 2) + mouseEvent.getX() - (intx + intw) > 5) {
			    e.setRadiusX((intw / 2) + mouseEvent.getX() - (intx + intw));
			    e.setRadiusY((intw / 2) + mouseEvent.getX() - (intx + intw));
			}

		    }
		    // UP
		    if (stateY > inty - 10 && stateY < inty + 10) {
			if ((inth / 2) + inty - mouseEvent.getY() > 5) {
			    e.setRadiusY((inth / 2) + inty - mouseEvent.getY());
			    e.setRadiusX((inth / 2) + inty - mouseEvent.getY());
			}
		    }
		    // down
		    if (stateY > (inty + inth - 10) && stateY < (inty + inth + 10)) {
			if ((inth / 2) + mouseEvent.getY() - (inty + inth) > 5) {
			    e.setRadiusY((inth / 2) + mouseEvent.getY() - (inty + inth));
			    e.setRadiusX((inth / 2) + mouseEvent.getY() - (inty + inth));
			}
		    }
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    setShapeStyleOnRelease(e);
		    dataPath.shapeDrawn(pane);
		}
	    }
	};
	e.setOnMousePressed(mouseHandler);
	e.setOnMouseDragged(mouseHandler);
	e.setOnMouseReleased(mouseHandler);
	e.setOnMouseMoved(mouseHandler);

    }

    @Override

    public void setCursor(Node node, double stateX, double stateY) {
	double intx = e.getCenterX() - e.getRadiusX();
	double inty = e.getCenterY() - e.getRadiusY();
	double intw = e.getRadiusX() * 2;
	double inth = e.getRadiusY() * 2;

	// Left Cursor Case
	if (stateX > intx - 3 && stateX < intx + 3) {
	    this.e.setCursor(Cursor.E_RESIZE);
	}
	// Right Cursor Case
	else if (stateX > (intx + intw - 3) && stateX < (intx + intw + 3)) {
	    this.e.setCursor(Cursor.W_RESIZE);
	}
	// Up Cursor Case
	else if (stateY > inty - 3 && stateY < inty + 3) {
	    this.e.setCursor(Cursor.N_RESIZE);
	}
	// Down Cursor Case
	else if (stateY > (inty + inth - 3) && stateY < (inty + inth + 3)) {
	    this.e.setCursor(Cursor.S_RESIZE);
	} else {
	    this.e.setCursor(Cursor.DEFAULT);
	}

    }

}
