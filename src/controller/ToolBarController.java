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

/**
 * This class represents the tool bar controller which is responsible for handling
 * the operations that can be done from the tool bar as geometric shapes drawing, moving, resizing, deleting and so on.
 */
public class ToolBarController {

    /**
     * The ColorPicker FXML instance.
     */
    @FXML
    private ColorPicker colorPicker;

    /**
     * The Slider FXML instance.
     */
    @FXML
    private Slider slider;

    /**
     * A pane instance.
     */
    private Pane pane;

    /**
     * A canvas instance.
     */
    private Canvas canvas;

    /**
     * The main controller singleton instance which controls the main functionality
     * and the flow of control between the modules.
     */
    private MainController mainController;

    /**
     * Minimum number of children of the pane.
     */
    private static final int MIN_SIZE = 1;

    /**
     * The operationHistory instance which is responsible for handling the undo/redo operations.
     */
    private OperationHistory operationHistory = new OperationHistory();

    /**
     * Starts pencil sketching mode.
     */
    @FXML
    private void pencilSketching() {
        mainController.setState(PENCIL_SKETCHING);
        mainController.freeSketching();
    }

    /**
     * Starts brush sketching mode.
     */
    @FXML
    private void brushSketching() {
        mainController.setState(BRUSH_SKETCHING);
        mainController.freeSketching();
    }

    /**
     * Starts erasing mode.
     */
    @FXML
    private void eraserTool() {
        mainController.setState(ERASE_SKETCHING);
        mainController.freeSketching();
    }

    /**
     * Draws a rectangle.
     */
    @FXML
    private void drawRectangle() {
        RectangleShape rectangle = new RectangleShape(this.operationHistory);
        mainController.setState(RECTANGLE_DRAW);
        configureShape(rectangle);
    }

    /**
     * Draws a square.
     */
    @FXML
    private void drawSquare() {
        SquareShape square = new SquareShape(this.operationHistory);
        mainController.setState(SQUARE_DRAW);
        configureShape(square);
    }

    /**
     * Draws an ellipse.
     */
    @FXML
    private void drawEllipse() {
        EllipseShape ellipse = new EllipseShape(this.operationHistory);
        mainController.setState(ELLIPSE_DRAW);
        configureShape(ellipse);
    }

    /**
     * Draws a circle.
     */
    @FXML
    private void drawCircle() {
        CircleShape circle = new CircleShape(this.operationHistory);
        mainController.setState(CIRCLE_DRAW);
        configureShape(circle);
    }

    /**
     * Draws a line segment.
     */
    @FXML
    private void drawLine() {
        LineSegment line = new LineSegment(this.operationHistory);
        mainController.setState(LINE_DRAW);
        configureShape(line);
    }

    /**
     * Draws a triangle.
     */
    @FXML
    private void drawTriangle() {
        TriangleShape triangle = new TriangleShape(this.operationHistory);
        mainController.setState(TRIANGLE_DRAW);
        configureShape(triangle);
    }

    /**
     * Starts dragging/moving mode.
     */
    @FXML
    private void draggingTool() {
        mainController.setState(DRAG_MODE);
        canvas.toBack();
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                LineSegment line = new LineSegment(this.operationHistory);
                line.moveShape(node, pane);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape(this.operationHistory);
                    square.moveShape(node, pane);
                } else {
                    RectangleShape rectangle = new RectangleShape(this.operationHistory);
                    rectangle.moveShape(node, pane);
                }
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape(this.operationHistory);
                triangle.moveShape(node, pane);
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape(this.operationHistory);
                    circle.moveShape(node, pane);
                } else {
                    EllipseShape ellipse = new EllipseShape(this.operationHistory);
                    ellipse.moveShape(node, pane);
                }
            }
        }
    }

    /**
     * Starts resizing mode.
     */
    @FXML
    private void resizingTool() {
        mainController.setState(RESIZE_MODE);
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                LineSegment line = new LineSegment(this.operationHistory);
                line.resizeShape(node, pane);
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape(this.operationHistory);
                triangle.resizeShape(node, pane);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape(this.operationHistory);
                    square.resizeShape(node, pane);
                } else {
                    RectangleShape rectangle = new RectangleShape(this.operationHistory);
                    rectangle.resizeShape(node, pane);
                }
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape(this.operationHistory);
                    circle.resizeShape(node, pane);
                } else {
                    EllipseShape ellipse = new EllipseShape(this.operationHistory);
                    ellipse.resizeShape(node, pane);
                }
            }
        }
    }

    /**
     * Undoes an operation.
     */
    @FXML
    private void undoTool() {
        mainController.setState(UNDO_MODE);
        if (operationHistory.getPrimaryStackSize() >= MIN_SIZE) {
            operationHistory.undo(pane);
        }
    }

    /**
     * Redoes an operation.
     */
    @FXML
    private void redoTool() {
        mainController.setState(REDO_MODE);
        if (operationHistory.getSecondaryStackSize() >= MIN_SIZE) {
            operationHistory.redo(pane);
        }
    }

    /**
     * Starts filling mode.
     */
    @FXML
    private void fillingTool() {
        mainController.setState(FILL_MODE);
        canvas.toBack();
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                LineSegment line = new LineSegment(this.operationHistory);
                line.fillShape(pane, colorPicker.getValue(), node);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape(this.operationHistory);
                    square.fillShape(pane, colorPicker.getValue(), node);
                } else {
                    RectangleShape rectangle = new RectangleShape(this.operationHistory);
                    rectangle.fillShape(pane, colorPicker.getValue(), node);
                }
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape(this.operationHistory);
                triangle.fillShape(pane, colorPicker.getValue(), node);
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape(this.operationHistory);
                    circle.fillShape(pane, colorPicker.getValue(), node);
                } else {
                    EllipseShape ellipse = new EllipseShape(this.operationHistory);
                    ellipse.fillShape(pane, colorPicker.getValue(), node);
                }
            }
        }
    }

    /**
     * Starts deleting mode.
     */
    @FXML
    private void deletingTool() {
        mainController.setState(DELETE_MODE);
        canvas.toBack();
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
            LineSegment line = new LineSegment(this.operationHistory);
            line.deleteShape(pane, node);
            } else if (node instanceof Rectangle) {
                Rectangle quadrilateral = (Rectangle) node;
                if (quadrilateral.getWidth() == quadrilateral.getHeight()) {
                    SquareShape square = new SquareShape(this.operationHistory);
                    square.deleteShape(pane, node);
                } else {
                    RectangleShape rectangle = new RectangleShape(this.operationHistory);
                    rectangle.deleteShape(pane, node);
                }
            } else if (node instanceof Polygon) {
                TriangleShape triangle = new TriangleShape(this.operationHistory);
                triangle.deleteShape(pane, node);
            } else if (node instanceof Ellipse) {
                Ellipse e = (Ellipse) node;
                if (e.getRadiusX() == e.getRadiusY()) {
                    CircleShape circle = new CircleShape(this.operationHistory);
                    circle.deleteShape(pane, node);
                } else {
                    EllipseShape ellipse = new EllipseShape(this.operationHistory);
                    ellipse.deleteShape(pane, node);
                }
            }
        }
    }

    /**
     * Configures the shape before drawing it.
     * @param shape The shape to be configured before being drawn.
     */
    private void configureShape(Shapes shape) {
        shape.drawShape(pane, colorPicker, slider);
    }

    /**
     * Initializes some fields of this class once an instance is instantiated from this class.
     * @param mainController The main controller singleton instance.
     */
    void initialize(MainController mainController) {
        this.mainController = mainController;
        this.pane = mainController.getPane();
        this.canvas = mainController.getCanvas();
    }

    /**
     * A getter "accessor" for the the line width slider.
     * @return The value of the line width slider.
     */
    double getSliderValue() {
        return slider.getValue();
    }

    /**
     * A setter "modifier" for the color picker used for choosing the color.
     * @param color A color that is required to be shown as the current color on the color picker.
     */
    void setColorPickerValue(Color color) {
        colorPicker.setValue(color);
    }

    /**
     * A getter "accessor" for the color picker current color.
     * @return The current color on the color picker.
     */
    Color getColorPickerValue() {
        return colorPicker.getValue();
    }

    /**
     * Clears the stacks used for the undo/redo operations.
     */
    void clearData() {
        operationHistory.clearPrimaryStack();
        operationHistory.clearSecondaryStack();
    }
}