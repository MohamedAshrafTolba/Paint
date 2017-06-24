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
        mainController.setState(DRAG_MODE);
        canvas.toBack();
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                LineSegment line = new LineSegment();
                line.dataInitialize(operationHistory);
                line.moveShape(node, pane);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.moveShape(node, pane);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.moveShape(node, pane);
                }
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.moveShape(node, pane);
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.moveShape(node, pane);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.moveShape(node, pane);
                }
            }
        }
    }

    @FXML
    private void resizingTool() {
        mainController.setState(RESIZE_MODE);
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                LineSegment line = new LineSegment();
                line.dataInitialize(operationHistory);
                line.resizeShape(node, pane);
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.resizeShape(node, pane);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.resizeShape(node, pane);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.resizeShape(node, pane);
                }
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.resizeShape(node, pane);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.resizeShape(node, pane);
                }
            }
        }
    }

    @FXML
    private void undoTool() {
        mainController.setState(UNDO_MODE);
        if (operationHistory.getPrimaryStackSize() >= MIN_SIZE) {
            operationHistory.undo(pane);
        }
    }

    @FXML
    private void redoTool() {
        mainController.setState(REDO_MODE);
        if (operationHistory.getSecondaryStackSize() >= MIN_SIZE) {
            operationHistory.redo(pane);
        }
    }

    @FXML
    private void fillingTool() {
        mainController.setState(FILL_MODE);
        canvas.toBack();
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                LineSegment line = new LineSegment();
                line.dataInitialize(operationHistory);
                line.fillShape(pane, colorPicker.getValue(), node);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.fillShape(pane, colorPicker.getValue(), node);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.fillShape(pane, colorPicker.getValue(), node);
                }
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.fillShape(pane, colorPicker.getValue(), node);
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.fillShape(pane, colorPicker.getValue(), node);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.fillShape(pane, colorPicker.getValue(), node);
                }
            }
        }
    }

    @FXML
    private void deletingTool() {
        mainController.setState(DELETE_MODE);
        canvas.toBack();
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
            LineSegment line = new LineSegment();
            line.dataInitialize(operationHistory);
            line.deleteShape(pane, node);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape();
                    square.dataInitialize(operationHistory);
                    square.deleteShape(pane, node);
                } else {
                    RectangleShape rectangle = new RectangleShape();
                    rectangle.dataInitialize(operationHistory);
                    rectangle.deleteShape(pane, node);
                }
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape();
                triangle.dataInitialize(operationHistory);
                triangle.deleteShape(pane, node);
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape();
                    circle.dataInitialize(operationHistory);
                    circle.deleteShape(pane, node);
                } else {
                    EllipseShape ellipse = new EllipseShape();
                    ellipse.dataInitialize(operationHistory);
                    ellipse.deleteShape(pane, node);
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