package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SquareShape extends RectangleShape {
    private Rectangle finalRectangle;
    private double initialX;
    private double initialY;
    private double initialW;
    private double initialH;
    private double stateX;
    private double stateY;

    @Override
    public void drawShape(Pane pane, ColorPicker colorPicker, Slider lineWidth) {
		SimpleDoubleProperty rectangleInitialX = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleX = new SimpleDoubleProperty();
		Rectangle initialRectangle = getNewRectangle();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			double initialX, x, dx, initialY, y, dy;
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					initialRectangle.widthProperty().bind(rectangleX.subtract(rectangleInitialX));
					initialRectangle.heightProperty().bind(rectangleX.subtract(rectangleInitialX));
					pane.getChildren().add(initialRectangle);
					initialRectangle.setStroke(colorPicker.getValue());
					initialRectangle.setStrokeWidth(lineWidth.getValue());
					initialRectangle.setX(mouseEvent.getX());
					initialRectangle.setY(mouseEvent.getY());
					initialX = mouseEvent.getX();
					initialY = mouseEvent.getY();
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					x = mouseEvent.getX();
					dx = x - initialX;
					y = mouseEvent.getY();
					dy = y - initialY;
					if (dx < 0 && dy < 0) {  // 2nd quad case
						initialRectangle.setX(mouseEvent.getX());
						initialRectangle.setY(mouseEvent.getY());
						if (-1.0 * dx > -1.0 * dy) {
							initialRectangle.setY(mouseEvent.getY() - (-1.0 * dx - -1.0 * dy));
							rectangleX.set(initialX);
							rectangleInitialX.set(mouseEvent.getX());
						} else {
							initialRectangle.setX(mouseEvent.getX() + (-1.0 * dx - -1.0 * dy));
							rectangleX.set(initialY);
							rectangleInitialX.set(mouseEvent.getY());
						}
					} else if (dy < 0) {  // 1st quad case
						initialRectangle.setX(initialX);
						initialRectangle.setY(mouseEvent.getY());
						rectangleX.set(initialY);
						rectangleInitialX.set(mouseEvent.getY());
					} else if (dx < 0) {  // 3rd quad case
						initialRectangle.setX(mouseEvent.getX());
						initialRectangle.setY(initialY);
						rectangleX.set(initialX);
						rectangleInitialX.set(mouseEvent.getX());
					} else {  // 4th quad case
						initialRectangle.setX(initialX);
						initialRectangle.setY(initialY);
						rectangleX.set(mouseEvent.getX());
						rectangleInitialX.set(initialX);
					}
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
					Rectangle finalRectangle = getNewRectangle();
					finalRectangle.setStroke(colorPicker.getValue());
					finalRectangle.setStrokeWidth(lineWidth.getValue());
					finalRectangle.setX(initialRectangle.getX());
					finalRectangle.setY(initialRectangle.getY());
					finalRectangle.setWidth(initialRectangle.getWidth());
					finalRectangle.setHeight(initialRectangle.getHeight());
					pane.getChildren().add(finalRectangle);
					rectangleX.set(0);
					pane.getChildren().remove(initialRectangle);
					operationHistory.shapeDrawn(pane);
				}
			}
		};
		pane.setOnMousePressed(mouseHandler);
		pane.setOnMouseDragged(mouseHandler);
		pane.setOnMouseReleased(mouseHandler);
	}

    private Rectangle getNewRectangle() {
		Rectangle finalRectangle = new Rectangle();
		finalRectangle.setFill(Color.TRANSPARENT);
		return finalRectangle;
    }

    @Override
    public void resizeShape(Node node, Pane pane) {
		resetMouseEvents(pane);
		finalRectangle = (Rectangle) node;
		initialX = finalRectangle.getX();
		initialY = finalRectangle.getY();
		initialW = finalRectangle.getWidth();
		initialH = finalRectangle.getHeight();
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
				setCursor(node, mouseEvent.getX(), mouseEvent.getY());
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				setShapeStyleOnDrag(node);
				initialX = finalRectangle.getX();
				initialY = finalRectangle.getY();
				initialW = finalRectangle.getWidth();
				initialH = finalRectangle.getHeight();
				stateX = mouseEvent.getX();
				stateY = mouseEvent.getY();
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				// Right
				if (stateX > initialX - 10 && stateX < initialX + 10) {
				if (initialW + initialX - mouseEvent.getX() > 5) {
					finalRectangle.setX(mouseEvent.getX());
					finalRectangle.setY(initialY);
					finalRectangle.setWidth(initialW + initialX - mouseEvent.getX());
					finalRectangle.setHeight(initialW + initialX - mouseEvent.getX());
				}
				}
				// Left
				if (stateX > (initialX + initialW - 10) && stateX < (initialX + initialW + 10)) {
					if (initialW + mouseEvent.getX() - (initialX + initialW) > 5) {
						finalRectangle.setX(initialX);
						finalRectangle.setY(initialY);
						finalRectangle.setWidth(initialW + mouseEvent.getX() - (initialX + initialW));
						finalRectangle.setHeight(initialW + mouseEvent.getX() - (initialX + initialW));
					}
				}
				// Up
				if (stateY > initialY - 10 && stateY < initialY + 10) {
					if (initialH + initialY - mouseEvent.getY() > 5) {
						finalRectangle.setX(initialX);
						finalRectangle.setY(mouseEvent.getY());
						finalRectangle.setHeight(initialH + initialY - mouseEvent.getY());
						finalRectangle.setWidth(initialH + initialY - mouseEvent.getY());
					}
				}
				// Down
				if (stateY > (initialY + initialH - 10) && stateY < (initialY + initialH + 10)) {
					if (initialH + mouseEvent.getY() - (initialY + initialH) > 5) {
						finalRectangle.setX(initialX);
						finalRectangle.setY(initialY);
						finalRectangle.setHeight(initialH + mouseEvent.getY() - (initialY + initialH));
						finalRectangle.setWidth(initialH + mouseEvent.getY() - (initialY + initialH));

					}
				}
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
				setShapeStyleOnRelease(node);
				operationHistory.shapeDrawn(pane);
		}};
		node.setOnMousePressed(mouseHandler);
		node.setOnMouseDragged(mouseHandler);
		node.setOnMouseReleased(mouseHandler);
		node.setOnMouseMoved(mouseHandler);
    }

    @Override
    public void setCursor(Node node, double stateX, double stateY) {
		super.setCursor(node, stateX, stateY);
    }
}