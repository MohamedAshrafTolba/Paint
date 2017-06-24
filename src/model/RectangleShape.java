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

public class RectangleShape extends PolygonShape {
	
	private Rectangle rectangle;
	private double initialX;
	private double initialY;
	private double initialW;
	private double initialH;
	private double stateX;
	private double stateY;

	public RectangleShape(OperationHistory operationHistory) {
		super(operationHistory);
	}

	@Override
	public void drawShape(Pane pane, ColorPicker colorPicker, Slider lineWidth) {
		SimpleDoubleProperty rectangleInitialX = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleInitialY = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleX = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleY = new SimpleDoubleProperty();
		Rectangle initialRectangle = getNewRectangle();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			double initialX, x, dx, initialY, y, dy;
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					initialRectangle.widthProperty().bind(rectangleX.subtract(rectangleInitialX));
					initialRectangle.heightProperty().bind(rectangleY.subtract(rectangleInitialY));
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
					if (dx < 0 && dy < 0) {  // 2nd quadrant case
						initialRectangle.setX(mouseEvent.getX());
						initialRectangle.setY(mouseEvent.getY());
						rectangleInitialX.set(mouseEvent.getX());
						rectangleInitialY.set(mouseEvent.getY());
						rectangleX.set(initialX);
						rectangleY.set(initialY);
					}
					else if (dy < 0) {  // 1st quadrant case
						initialRectangle.setX(initialX);
						initialRectangle.setY(mouseEvent.getY());
						rectangleInitialX.set(initialX);
						rectangleInitialY.set(mouseEvent.getY());
						rectangleX.set(mouseEvent.getX());
						rectangleY.set(initialY);
					}
					else if (dx < 0) {  // 3rd quad case
						initialRectangle.setX(mouseEvent.getX());
						initialRectangle.setY(initialY);
						rectangleInitialX.set(mouseEvent.getX());
						rectangleInitialY.set(initialY);
						rectangleX.set(initialX);
						rectangleY.set(mouseEvent.getY());
					} else {  // 4th quad case
						initialRectangle.setX(initialX);
						initialRectangle.setY(initialY);
						rectangleInitialX.set(initialX);
						rectangleInitialY.set(initialY);
						rectangleX.set(mouseEvent.getX());
						rectangleY.set(mouseEvent.getY());
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
					rectangleY.set(0);
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
		Rectangle rectangle = new Rectangle();
		rectangle.setFill(Color.TRANSPARENT);
		return rectangle;
	}

	@Override
	public void resizeShape(Node node, Pane pane) {
		resetMouseEvents(pane);
		rectangle = (Rectangle) node;
		initialX = rectangle.getX();
		initialY = rectangle.getY();
		initialW = rectangle.getWidth();
		initialH = rectangle.getHeight();
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
                setCursor(node, mouseEvent.getX(), mouseEvent.getY());
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                setShapeStyleOnDrag(node);
                initialX = rectangle.getX();
                initialY = rectangle.getY();
                initialW = rectangle.getWidth();
                initialH = rectangle.getHeight();
                stateX = mouseEvent.getX();
                stateY = mouseEvent.getY();
            }
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                // Right
                if (stateX > initialX - 10 && stateX < initialX + 10) {
                    if (initialW + initialX - mouseEvent.getX() > 5) {
                        rectangle.setX(mouseEvent.getX());
                        rectangle.setY(initialY);
                        rectangle.setWidth(initialW + initialX - mouseEvent.getX());
                        rectangle.setHeight(initialH);
                    }
                }
                // Left
                if (stateX > (initialX + initialW - 10) && stateX < (initialX + initialW + 10)) {
                    if (initialW + mouseEvent.getX() - (initialX + initialW) > 5) {
                        rectangle.setX(initialX);
                        rectangle.setY(initialY);
                        rectangle.setWidth(initialW + mouseEvent.getX() - (initialX + initialW));
                        rectangle.setHeight(initialH);
                    }
                }
                // Up
                if (stateY > initialY - 10 && stateY < initialY + 10) {
                    if (initialH + initialY - mouseEvent.getY() > 5) {
                        rectangle.setX(initialX);
                        rectangle.setY(mouseEvent.getY());
                        rectangle.setWidth(initialW);
                        rectangle.setHeight(initialH + initialY - mouseEvent.getY());
                    }
                }
                // Down
                if (stateY > (initialY + initialH - 10) && stateY < (initialY + initialH + 10)) {
                    if (initialH + mouseEvent.getY() - (initialY + initialH) > 5) {
                        rectangle.setX(initialX);
                        rectangle.setY(initialY);
                        rectangle.setWidth(initialW);
                        rectangle.setHeight(initialH + mouseEvent.getY() - (initialY + initialH));
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
		Rectangle rectangle = (Rectangle) node;
		double initialX = rectangle.getX();
		double initialY = rectangle.getY();
		double initialW = rectangle.getWidth();
		double initialH = rectangle.getHeight();
		configureCursor(initialX,initialY,initialW,initialH,stateX,stateY,node);
	}
}