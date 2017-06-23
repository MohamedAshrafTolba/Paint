package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import model.*;

import static model.enums.State.*;

public class ToolBarController {

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider slider;

    private Pane pane;
    private Canvas canvas;
	private MainController mainController;
	private static final int MIN_SIZE = 1;

    private OperationHistory operationHistory = new OperationHistory();

    @FXML
    private void pencilSketching() {
		mainController.setState(PENCIL_SKETCHING);
		mainController.freeSketching();
    }

    @FXML
    private void brushSketching() {
		mainController.setState(BRUSH_SKETCHING);
		mainController.freeSketching();
    }

    @FXML
    private void eraserTool() {
		mainController.setState(ERASE_SKETCHING);
		mainController.freeSketching();
    }

    @FXML
    private void drawRectangle() {
		RectangleShape rectangle = new RectangleShape();
        mainController.setState(RECTANGLE_DRAW);
		configureShape(rectangle);
    }

    @FXML
    private void drawSquare() {
		SquareShape square = new SquareShape();
        mainController.setState(SQUARE_DRAW);
		configureShape(square);
    }

    @FXML
    private void drawEllipse() {
		EllipseShape ellipse = new EllipseShape();
        mainController.setState(ELLIPSE_DRAW);
		configureShape(ellipse);
    }

    @FXML
    private void drawCircle() {
		CircleShape circle = new CircleShape();
        mainController.setState(CIRCLE_DRAW);
		configureShape(circle);
    }

    @FXML
    private void drawLine() {
		LineSegment line = new LineSegment();
        mainController.setState(LINE_DRAW);
		configureShape(line);
    }

    @FXML
    private void drawTriangle() {
		TriangleShape triangle = new TriangleShape();
        mainController.setState(TRIANGLE_DRAW);
		configureShape(triangle);
    }

    @FXML
    private void draggingTool() {
        canvas.toBack();
        mainController.setState(DRAG_MODE);
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
                LineSegment line = new LineSegment();
                line.dataInitialize(operationHistory);
                line.moveShape(component, pane);
            } else if (component instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) component;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.moveShape(component, pane);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.moveShape(component, pane);
                }
            } else if (component instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.moveShape(component, pane);
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.moveShape(component, pane);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.moveShape(component, pane);
                }
            }
        }
    }

    @FXML
    private void resizingTool() {
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
                LineSegment line = new LineSegment();
                line.dataInitialize(operationHistory);
                line.resizeShape(component, pane);
            } else if (component instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.resizeShape(component, pane);
            } else if (component instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) component;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.resizeShape(component, pane);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.resizeShape(component, pane);
                }
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.resizeShape(component, pane);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.resizeShape(component, pane);
                }
            }
        }
    }

    @FXML
    private void undoTool() {
        if (operationHistory.getPrimaryStackSize() >= MIN_SIZE) {
            operationHistory.invokeUndoProcess(pane);
        }
    }

    @FXML
    private void redoTool() {
        if (operationHistory.getSecondaryStackSize() >= MIN_SIZE) {
            operationHistory.invokeRedoProcess(pane);
        }
    }

    @FXML
    private void fillingTool() {
        canvas.toBack();
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
                LineSegment line = new LineSegment();
                line.dataInitialize(operationHistory);
                line.fillShape(pane, colorPicker.getValue(), component);
            } else if (component instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) component;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.fillShape(pane, colorPicker.getValue(), component);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.fillShape(pane, colorPicker.getValue(), component);
                }
            } else if (component instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.fillShape(pane, colorPicker.getValue(), component);
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.fillShape(pane, colorPicker.getValue(), component);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.fillShape(pane, colorPicker.getValue(), component);
                }
            }
        }
    }

    @FXML
    private void deletingTool() {
        canvas.toBack();
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
            LineSegment line = new LineSegment();
            line.dataInitialize(operationHistory);
            line.deleteShape(pane, component);
            } else if (component instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) component;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.deleteShape(pane, component);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.deleteShape(pane, component);
                }
            } else if (component instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.deleteShape(pane, component);
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.deleteShape(pane, component);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.deleteShape(pane, component);
                }
            }
        }
    }

    private void configureShape(Shapes shape) {
        shape.dataInitialize(operationHistory);
        shape.drawShape(pane, colorPicker, slider);
    }

    void initialize(MainController mainController) {
        this.mainController = mainController;
        this.pane = mainController.getPane();
        this.canvas = mainController.getCanvas();
    }

    double getSliderValue() {
        return slider.getValue();
    }

    void setColorPickerValue(Color paint) {
        colorPicker.setValue(paint);
    }

    Color getColorPickerValue() {
        return colorPicker.getValue();
    }
    
    void clearData() {
        operationHistory.clearStacks();
    }
}