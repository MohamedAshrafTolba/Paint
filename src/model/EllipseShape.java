package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class EllipseShape extends Shapes {

    private Ellipse ellipse;
    private double initialX;
    private double initialY;
    private double initialW;
    private double initialH;
    private double stateX;
    private double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth) {
		SimpleDoubleProperty rectangleInitialX = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleInitialY = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleX = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleY = new SimpleDoubleProperty();
		Ellipse initialEllipse = getNewEllipse();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			double initialX, x, dx, initialY, y, dy;
			@Override
			public void handle(MouseEvent mouseEvent) {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				initialEllipse.radiusXProperty().bind(rectangleX.subtract(rectangleInitialX));
				initialEllipse.radiusYProperty().bind(rectangleY.subtract(rectangleInitialY));
				pane.getChildren().add(initialEllipse);
				initialEllipse.setStroke(pickColor.getValue());
				initialEllipse.setStrokeWidth(lineWidth.getValue());
				initialEllipse.setCenterX(mouseEvent.getX());
				initialEllipse.setCenterY(mouseEvent.getY());
				initialX = mouseEvent.getX();
				initialY = mouseEvent.getY();
			} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				x = mouseEvent.getX();
				dx = x - initialX;
				y = mouseEvent.getY();
				dy = y - initialY;
				if (dx < 0 && dy < 0) {  // 2nd quad case
					rectangleInitialX.set(mouseEvent.getX());
					rectangleInitialY.set(mouseEvent.getY());
					rectangleX.set(initialX);
					rectangleY.set(initialY);
				}
				else if (dy < 0) {  // 1st quad case
					rectangleInitialX.set(initialX);
					rectangleInitialY.set(mouseEvent.getY());
					rectangleX.set(mouseEvent.getX());
					rectangleY.set(initialY);
				} else if (dx < 0) {  // 3rd quad case
					rectangleInitialX.set(mouseEvent.getX());
					rectangleInitialY.set(initialY);
					rectangleX.set(initialX);
					rectangleY.set(mouseEvent.getY());
				} else {  // 4th quad case
					rectangleX.set(mouseEvent.getX());
					rectangleY.set(mouseEvent.getY());
					rectangleInitialX.set(initialX);
					rectangleInitialY.set(initialY);
				}
			} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
				Ellipse finalEllipse = getNewEllipse();
				finalEllipse.setStroke(pickColor.getValue());
				finalEllipse.setStrokeWidth(lineWidth.getValue());
				finalEllipse.setCenterX(initialEllipse.getCenterX());
				finalEllipse.setCenterY(initialEllipse.getCenterY());
				finalEllipse.setRadiusX(initialEllipse.getRadiusX());
				finalEllipse.setRadiusY(initialEllipse.getRadiusY());
				pane.getChildren().add(finalEllipse);
				rectangleX.set(0);
				rectangleY.set(0);
				pane.getChildren().remove(initialEllipse);
				operationHistory.shapeDrawn(pane);
			}
		}};
		pane.setOnMousePressed(mouseHandler);
		pane.setOnMouseDragged(mouseHandler);
		pane.setOnMouseReleased(mouseHandler);
    }

    Ellipse getNewEllipse() {
		Ellipse ellipse = new Ellipse();
		ellipse.setFill(Color.TRANSPARENT);
		return ellipse;
    }

    @Override
    public void resizeShape(Node node, Pane pane) {
		resetMouseEvents(pane);
		ellipse = (Ellipse) node;
		initialX = ellipse.getCenterX() - ellipse.getRadiusX();
		initialY = ellipse.getCenterY() - ellipse.getRadiusY();
		initialW = ellipse.getRadiusX() * 2;
		initialH = ellipse.getRadiusY() * 2;
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
				setCursor(node, mouseEvent.getX(), mouseEvent.getY());
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				setShapeStyleOnDrag(node);
				initialX = ellipse.getCenterX() - ellipse.getRadiusX();
				initialY = ellipse.getCenterY() - ellipse.getRadiusY();
				initialW = ellipse.getRadiusX() * 2;
				initialH = ellipse.getRadiusY() * 2;
				stateX = mouseEvent.getX();
				stateY = mouseEvent.getY();
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				// Left
				if (stateX > initialX - 10 && stateX < initialX + 10) {
					if ((initialW / 2) + initialX - mouseEvent.getX() > 5) {
						ellipse.setRadiusX((initialW / 2) + initialX - mouseEvent.getX());
					}
				}
				// Right
				if (stateX > (initialX + initialW - 10) && stateX < (initialX + initialW + 10)) {
					if ((initialW / 2) + mouseEvent.getX() - (initialX + initialW) > 5) {
						ellipse.setRadiusX((initialW / 2) + mouseEvent.getX() - (initialX + initialW));
					}
				}
				// Up
				if (stateY > initialY - 10 && stateY < initialY + 10) {
					if ((initialH / 2) + initialY - mouseEvent.getY() > 5) {
						ellipse.setRadiusY((initialH / 2) + initialY - mouseEvent.getY());
					}
				}
				// Down
				if (stateY > (initialY + initialH - 10) && stateY < (initialY + initialH + 10)) {
					if ((initialH / 2) + mouseEvent.getY() - (initialY + initialH) > 5) {
						ellipse.setRadiusY((initialH / 2) + mouseEvent.getY() - (initialY + initialH));
					}
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
		Ellipse ellipse = (Ellipse) node;
		double initialX = ellipse.getCenterX() - ellipse.getRadiusX();
		double initialY = ellipse.getCenterY() - ellipse.getRadiusY();
		double initialW = ellipse.getRadiusX() * 2;
		double initialH = ellipse.getRadiusY() * 2;
		configureCursor(initialX,initialY,initialW,initialH,stateX,stateY,node);
    }
}