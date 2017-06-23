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

    void instantiate(MainController mainController) {
		this.mainController = mainController;
    }

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
		rectangle.dataInitialize(operationHistory);
		pane = mainController.getPane();
		mainController.setState(RECTANGLE_DRAW);
		rectangle.drawShape(pane, colorPicker, slider);
    }

    @FXML
    private void drawSquare() {
		SquareShape square = new SquareShape();
		square.dataInitialize(operationHistory);
		pane = mainController.getPane();
		mainController.setState(SQUARE_DRAW);
		square.drawShape(pane, colorPicker, slider);
    }

    @FXML
    private void drawEllipse() {
		EllipseShape ellipse = new EllipseShape();
		ellipse.dataInitialize(operationHistory);
		pane = mainController.getPane();
		mainController.setState(ELLIPSE_DRAW);
		ellipse.drawShape(pane, colorPicker, slider);
    }

    @FXML
    private void drawCircle() {
		CircleShape circle = new CircleShape();
		circle.dataInitialize(operationHistory);
		pane = mainController.getPane();
		mainController.setState(CIRCLE_DRAW);
		circle.drawShape(pane, colorPicker, slider);
    }

    @FXML
    private void drawLine() {
		LineSegment line = new LineSegment();
		line.dataInitialize(operationHistory);
		pane = mainController.getPane();
		mainController.setState(LINE_DRAW);
		line.drawShape(pane, colorPicker, slider);
    }

    @FXML
    private void drawTriangle() {
		TriangleShape triangle = new TriangleShape();
		triangle.dataInitialize(operationHistory);
		pane = mainController.getPane();
		mainController.setState(TRIANGLE_DRAW);
		triangle.drawShape(pane, colorPicker, slider);
    }

    @FXML
    private void draggingTool() {
        pane = mainController.getPane();
        canvas = mainController.getCanvas();
        canvas.toBack();
        mainController.setState(DRAG_MODE);
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
                LineSegment newLine = new LineSegment();
                newLine.dataInitialize(operationHistory);
                newLine.moveShape(component, pane);
            } else if (component instanceof Rectangle) {
                Rectangle r = (Rectangle) component;
                if (r.getWidth() == r.getHeight()) {
                    SquareShape newSquare = new SquareShape();
                    newSquare.dataInitialize(operationHistory);
                    newSquare.moveShape(component, pane);
                } else {
                    RectangleShape newRectangle = new RectangleShape();
                    newRectangle.dataInitialize(operationHistory);
                    newRectangle.moveShape(component, pane);
                }
            } else if (component instanceof Polygon) {
                TriangleShape newTriangle = new TriangleShape();
                newTriangle.dataInitialize(operationHistory);
                newTriangle.moveShape(component, pane);
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape newCircle = new CircleShape();
                    newCircle.dataInitialize(operationHistory);
                    newCircle.moveShape(component, pane);
                } else {
                    EllipseShape newEllipse = new EllipseShape();
                    newEllipse.dataInitialize(operationHistory);
                    newEllipse.moveShape(component, pane);
                }
            }
        }
    }

    @FXML
    private void resizingTool() {
        pane = mainController.getPane();
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
                LineSegment resizeLine = new LineSegment();
                resizeLine.dataInitialize(operationHistory);
                resizeLine.resizeShape(component, pane);
            } else if (component instanceof Polygon) {
                TriangleShape resizeTriangle = new TriangleShape();
                resizeTriangle.dataInitialize(operationHistory);
                resizeTriangle.resizeShape(component, pane);
            } else if (component instanceof Rectangle) {
                Rectangle r = (Rectangle) component;
                if (r.getWidth() == r.getHeight()) {
                    SquareShape resizeSquare = new SquareShape();
                    resizeSquare.dataInitialize(operationHistory);
                    resizeSquare.resizeShape(component, pane);
                } else {
                    RectangleShape resizeRectangle = new RectangleShape();
                    resizeRectangle.dataInitialize(operationHistory);
                    resizeRectangle.resizeShape(component, pane);
                }
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape resizeCircle = new CircleShape();
                    resizeCircle.dataInitialize(operationHistory);
                    resizeCircle.resizeShape(component, pane);
                } else {
                    EllipseShape resizeEllipse = new EllipseShape();
                    resizeEllipse.dataInitialize(operationHistory);
                    resizeEllipse.resizeShape(component, pane);
                }
            }
        }
    }

    @FXML
    private void undoTool() {
        if (operationHistory.getPrimaryStackSize() >= MIN_SIZE) {
            pane = mainController.getPane();
            operationHistory.invokeUndoProcess(pane);
        }
    }

    @FXML
    private void redoTool() {
        if (operationHistory.getSecondaryStackSize() >= MIN_SIZE) {
            pane = mainController.getPane();
            operationHistory.invokeRedoProcess(pane);
        }
    }

    @FXML
    private void fillingTool() {
        pane = mainController.getPane();
        canvas = mainController.getCanvas();
        canvas.toBack();
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
                LineSegment newLine = new LineSegment();
                newLine.dataInitialize(operationHistory);
                newLine.fillShape(pane, colorPicker.getValue(), component);
            } else if (component instanceof Rectangle) {
                Rectangle r = (Rectangle) component;
                if (r.getWidth() == r.getHeight()) {
                    SquareShape newSquare = new SquareShape();
                    newSquare.dataInitialize(operationHistory);
                    newSquare.fillShape(pane, colorPicker.getValue(), component);
                } else {
                    RectangleShape newRectangle = new RectangleShape();
                    newRectangle.dataInitialize(operationHistory);
                    newRectangle.fillShape(pane, colorPicker.getValue(), component);
                }
            } else if (component instanceof Polygon) {
                TriangleShape newTriangle = new TriangleShape();
                newTriangle.dataInitialize(operationHistory);
                newTriangle.fillShape(pane, colorPicker.getValue(), component);
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape newCircle = new CircleShape();
                    newCircle.dataInitialize(operationHistory);
                    newCircle.fillShape(pane, colorPicker.getValue(), component);
                } else {
                    EllipseShape newEllipse = new EllipseShape();
                    newEllipse.dataInitialize(operationHistory);
                    newEllipse.fillShape(pane, colorPicker.getValue(), component);
                }
            }
        }
    }

    @FXML
    private void deletingTool() {
        pane = mainController.getPane();
        canvas = mainController.getCanvas();
        canvas.toBack();
        for (Node component : pane.getChildren()) {
            if (component instanceof Line) {
            LineSegment newLine = new LineSegment();
            newLine.dataInitialize(operationHistory);
            newLine.deleteShape(pane, component);
            } else if (component instanceof Rectangle) {
                Rectangle r = (Rectangle) component;
                if (r.getWidth() == r.getHeight()) {
                    SquareShape newSquare = new SquareShape();
                    newSquare.dataInitialize(operationHistory);
                    newSquare.deleteShape(pane, component);
                } else {
                    RectangleShape newRectangle = new RectangleShape();
                    newRectangle.dataInitialize(operationHistory);
                    newRectangle.deleteShape(pane, component);
                }
            } else if (component instanceof Polygon) {
                TriangleShape newTriangle = new TriangleShape();
                newTriangle.dataInitialize(operationHistory);
                newTriangle.deleteShape(pane, component);
            } else if (component instanceof Ellipse) {
                Ellipse e = (Ellipse) component;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape newCircle = new CircleShape();
                    newCircle.dataInitialize(operationHistory);
                    newCircle.deleteShape(pane, component);
                } else {
                    EllipseShape newEllipse = new EllipseShape();
                    newEllipse.dataInitialize(operationHistory);
                    newEllipse.deleteShape(pane, component);
                }
            }
        }
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