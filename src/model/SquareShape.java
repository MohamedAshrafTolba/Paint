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
import javafx.scene.shape.Rectangle;

public class SquareShape extends PolygonShape {
    Rectangle r;
    double intx;
    double inty;
    double intw;
    double inth;
    double stateX;
    double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth) {
	SimpleDoubleProperty rectinitX = new SimpleDoubleProperty();
	SimpleDoubleProperty rectX = new SimpleDoubleProperty();
	Rectangle rect = getNewRectangle();

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    double intx, x, dx, inty, y, dy;

	    @Override
	    public void handle(MouseEvent mouseEvent) {

		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    rect.widthProperty().bind(rectX.subtract(rectinitX));
		    rect.heightProperty().bind(rectX.subtract(rectinitX));
		    pane.getChildren().add(rect);
		    rect.setStroke(pickColor.getValue());
		    rect.setStrokeWidth(lineWidth.getValue());
		    rect.setX(mouseEvent.getX());
		    rect.setY(mouseEvent.getY());
		    intx = mouseEvent.getX();
		    inty = mouseEvent.getY();
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    x = mouseEvent.getX();
		    dx = x - intx;
		    y = mouseEvent.getY();
		    dy = y - inty;

		    // 2nd quad case
		    if (dx < 0 && dy < 0) {
			rect.setX(mouseEvent.getX());
			rect.setY(mouseEvent.getY());
			if (-dx > -dy) {
			    rect.setY(mouseEvent.getY() - (-dx - -dy));
			    rectX.set(intx);
			    rectinitX.set(mouseEvent.getX());
			} else {
			    rect.setX(mouseEvent.getX() + (-dx - -dy));
			    rectX.set(inty);
			    rectinitX.set(mouseEvent.getY());
			}

		    }
		    // 1st quad case
		    else if (dy < 0) {
			rect.setX(intx);
			rect.setY(mouseEvent.getY());
			rectX.set(inty);
			rectinitX.set(mouseEvent.getY());
		    }
		    // 3rd quad case
		    else if (dx < 0) {
			rect.setX(mouseEvent.getX());
			rect.setY(inty);
			rectX.set(intx);
			rectinitX.set(mouseEvent.getX());
		    }
		    // 4th quad case
		    else {
			rect.setX(intx);
			rect.setY(inty);
			rectX.set(mouseEvent.getX());
			rectinitX.set(intx);
		    }
		} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
		    // Clone the rectangle
		    Rectangle r = getNewRectangle();
		    r.setStroke(pickColor.getValue());
		    r.setStrokeWidth(lineWidth.getValue());
		    // r = getNewRectangle();
		    r.setX(rect.getX());
		    r.setY(rect.getY());
		    r.setWidth(rect.getWidth());
		    r.setHeight(rect.getHeight());
		    pane.getChildren().add(r);
		    rectX.set(0);
		    pane.getChildren().remove(rect);
		    dataPath.shapeDrawn(pane);

		    
		}
	    }

	};

	pane.setOnMousePressed(mouseHandler);
	pane.setOnMouseDragged(mouseHandler);
	pane.setOnMouseReleased(mouseHandler);
    }

    public Rectangle getNewRectangle() {
	Rectangle r = new Rectangle();
	r.setFill(Color.TRANSPARENT);

	return r;
    }

    @Override
    public void resizeShape(Node node, Pane pane) {
	resetMouseEvents(pane);
	r = (Rectangle) node;
	intx = r.getX();
	inty = r.getY();
	intw = r.getWidth();
	inth = r.getHeight();

	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
		    setCursor(node, mouseEvent.getX(), mouseEvent.getY());
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
		    setShapeStyleOnDrag(node);
		    intx = r.getX();
		    inty = r.getY();
		    intw = r.getWidth();
		    inth = r.getHeight();
		    stateX = mouseEvent.getX();
		    stateY = mouseEvent.getY();
		}
		if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
		    // Right
		    if (stateX > intx - 10 && stateX < intx + 10) {
			if (intw + intx - mouseEvent.getX() > 5) {
			    r.setX(mouseEvent.getX());
			    r.setY(inty);
			    r.setWidth(intw + intx - mouseEvent.getX());
			    r.setHeight(intw + intx - mouseEvent.getX());
			}
		    }
		    // LEFT
		    if (stateX > (intx + intw - 10) && stateX < (intx + intw + 10)) {
			if (intw + mouseEvent.getX() - (intx + intw) > 5) {
			    r.setX(intx);
			    r.setY(inty);
			    r.setWidth(intw + mouseEvent.getX() - (intx + intw));
			    r.setHeight(intw + mouseEvent.getX() - (intx + intw));
			}

		    }
		    // UP
		    if (stateY > inty - 10 && stateY < inty + 10) {
			if (inth + inty - mouseEvent.getY() > 5) {
			    r.setX(intx);
			    r.setY(mouseEvent.getY());
			    r.setHeight(inth + inty - mouseEvent.getY());
			    r.setWidth(inth + inty - mouseEvent.getY());
			}
		    }
		    // down
		    if (stateY > (inty + inth - 10) && stateY < (inty + inth + 10)) {
			if (inth + mouseEvent.getY() - (inty + inth) > 5) {
			    r.setX(intx);
			    r.setY(inty);
			    r.setHeight(inth + mouseEvent.getY() - (inty + inth));
			    r.setWidth(inth + mouseEvent.getY() - (inty + inth));

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
	Rectangle r = (Rectangle) node;
	double intx = r.getX();
	double inty = r.getY();
	double intw = r.getWidth();
	double inth = r.getHeight();
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
	    System.out.println("4");
	} else {
	    node.setCursor(Cursor.DEFAULT);
	}
    }
    
    
    
}
