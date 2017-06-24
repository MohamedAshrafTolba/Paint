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

    private Ellipse ellipse;
    private double initialX;
    private double initialY;
    private double initialW;
    private double initialH;
    private double stateX;
    private double stateY;

	public CircleShape(OperationHistory operationHistory) {
		super(operationHistory);
	}

	@Override
    public void drawShape(Pane pane, ColorPicker colorPicker, Slider lineWidth) {
		SimpleDoubleProperty rectangleInitialX = new SimpleDoubleProperty();
		SimpleDoubleProperty rectangleX = new SimpleDoubleProperty();
		Ellipse initialEllipse = getNewEllipse();
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			double initialX, x, dx, initialY, y, dy;
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					initialEllipse.radiusXProperty().bind(rectangleX.subtract(rectangleInitialX));
					initialEllipse.radiusYProperty().bind(rectangleX.subtract(rectangleInitialX));
					pane.getChildren().add(initialEllipse);
					initialEllipse.setStroke(colorPicker.getValue());
					initialEllipse.setStrokeWidth(lineWidth.getValue());
					initialEllipse.setCenterX(mouseEvent.getX());
					initialEllipse.setCenterY(mouseEvent.getY());
					initialX = mouseEvent.getX();
					initialY = mouseEvent.getY();
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					mouseEvent.consume();
					x = mouseEvent.getX();
					dx = x - initialX;
					y = mouseEvent.getY();
					dy = y - initialY;
					if (dx < 0 && dy < 0) {  // 2nd quad case
						rectangleInitialX.set(mouseEvent.getX());
						rectangleX.set(initialX);
					} else if (dy < 0) {  // 1st quad case
						rectangleInitialX.set(initialX);
						rectangleX.set(mouseEvent.getX());
					} else if (dx < 0) {  // 3rd quad case
						rectangleInitialX.set(mouseEvent.getX());
						rectangleX.set(initialX);
					} else {  // 4th quad case
						rectangleX.set(mouseEvent.getX());
						rectangleInitialX.set(initialX);
					}
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
					Ellipse finalEllipse = getNewEllipse();
					finalEllipse.setStroke(colorPicker.getValue());
					finalEllipse.setStrokeWidth(lineWidth.getValue());
					finalEllipse.setCenterX(initialEllipse.getCenterX());
					finalEllipse.setCenterY(initialEllipse.getCenterY());
					finalEllipse.setRadiusX(initialEllipse.getRadiusX());
					finalEllipse.setRadiusY(initialEllipse.getRadiusY());
					pane.getChildren().add(finalEllipse);
					rectangleX.set(0.0);
					rectangleInitialX.set(0.0);
					pane.getChildren().remove(initialEllipse);
					operationHistory.shapeDrawn(pane);
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
		ellipse = (Ellipse) node;
		initialX = ellipse.getCenterX() - ellipse.getRadiusX();
		initialY = ellipse.getCenterY() - ellipse.getRadiusY();
		initialW = ellipse.getRadiusX() * 2;
		initialH = ellipse.getRadiusY() * 2;
		EventHandler<MouseEvent> mouseHandler = mouseEvent -> {
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED) {
				setCursor(ellipse, mouseEvent.getX(), mouseEvent.getY());
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
				setShapeStyleOnDrag(ellipse);
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
						ellipse.setRadiusY((initialW / 2) + initialX - mouseEvent.getX());
					}
				}
				// Right
				if (stateX > (initialX + initialW - 10) && stateX < (initialX + initialW + 10)) {
					if ((initialW / 2) + mouseEvent.getX() - (initialX + initialW) > 5) {
						ellipse.setRadiusX((initialW / 2) + mouseEvent.getX() - (initialX + initialW));
						ellipse.setRadiusY((initialW / 2) + mouseEvent.getX() - (initialX + initialW));
					}
				}
				// Up
				if (stateY > initialY - 10 && stateY < initialY + 10) {
					if ((initialH / 2) + initialY - mouseEvent.getY() > 5) {
						ellipse.setRadiusY((initialH / 2) + initialY - mouseEvent.getY());
						ellipse.setRadiusX((initialH / 2) + initialY - mouseEvent.getY());
					}
				}
				// Down
				if (stateY > (initialY + initialH - 10) && stateY < (initialY + initialH + 10)) {
					if ((initialH / 2) + mouseEvent.getY() - (initialY + initialH) > 5) {
						ellipse.setRadiusY((initialH / 2) + mouseEvent.getY() - (initialY + initialH));
						ellipse.setRadiusX((initialH / 2) + mouseEvent.getY() - (initialY + initialH));
					}
				}
			}
			if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
				setShapeStyleOnRelease(ellipse);
				operationHistory.shapeDrawn(pane);
			}
		};
		ellipse.setOnMousePressed(mouseHandler);
		ellipse.setOnMouseDragged(mouseHandler);
		ellipse.setOnMouseReleased(mouseHandler);
		ellipse.setOnMouseMoved(mouseHandler);
	}

    @Override
    public void setCursor(Node node, double stateX, double stateY) {
		double initialX = ellipse.getCenterX() - ellipse.getRadiusX();
		double initialY = ellipse.getCenterY() - ellipse.getRadiusY();
		double initialW = ellipse.getRadiusX() * 2;
		double initialH = ellipse.getRadiusY() * 2;
		// Left Cursor Case
		if (stateX > initialX - 3 && stateX < initialX + 3) {
			this.ellipse.setCursor(Cursor.E_RESIZE);
		}
		// Right Cursor Case
		else if (stateX > (initialX + initialW - 3) && stateX < (initialX + initialW + 3)) {
			this.ellipse.setCursor(Cursor.W_RESIZE);
		}
		// Up Cursor Case
		else if (stateY > initialY - 3 && stateY < initialY + 3) {
			this.ellipse.setCursor(Cursor.N_RESIZE);
		}
		// Down Cursor Case
		else if (stateY > (initialY + initialH - 3) && stateY < (initialY + initialH + 3)) {
			this.ellipse.setCursor(Cursor.S_RESIZE);
		} else {
			this.ellipse.setCursor(Cursor.DEFAULT);
		}
    }
}