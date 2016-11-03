package controller.SubController;

import controller.MainController;
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
import model.CircleShape;
import model.Data;
import model.EllipseShape;
import model.LineSegment;
import model.RectangleShape;
import model.SquareShape;
import model.TriangleShape;

public class ToolBarCtrl {
    private static final int PENCIL_TOOL = 1;
    private static final int BRUSH_TOOL = 2;
    private static final int ERASER_TOOL = 3;
    private static final int RECTANGLE_DRAW = 4;
    private static final int SQUARE_DRAW = 5;
    private static final int ELLIPSE_DRAW = 6;
    private static final int CIRCLE_DRAW = 7;
    private static final int LINE_DRAW = 8;
    private static final int TRIANGLE_DRAW = 9;
    private static final int DRAG_MODE = 10;

    private static boolean firstTimeBuffer;
    private MainController mainCtrl;

    @FXML
    private ColorPicker colorPickerFXid;
    @FXML
    private Slider sliderFXid;

    private Pane paneInp;
    private Canvas canvasInp;

    Data history = new Data();

    public void instantiate(MainController mainController) {
	mainCtrl = mainController;
	// paneInp = mainCtrl.passPane();
	// passPaneToData(paneInp);
    }

    @FXML
    private void pencilSketching() {
	mainCtrl.setState(PENCIL_TOOL);
	firstTimeBufferInvocation();
    }

    @FXML
    private void burshSketching() {
	mainCtrl.setState(BRUSH_TOOL);
	firstTimeBufferInvocation();
    }

    @FXML
    private void eraserTool() {
	mainCtrl.setState(ERASER_TOOL);
	firstTimeBufferInvocation();
    }

    @FXML
    private void drawRectangle() {
	RectangleShape rectangle = new RectangleShape();
	rectangle.dataInitialize(history);
	System.out.println("RECTANGLE");
	paneInp = mainCtrl.passPane();
	mainCtrl.setState(RECTANGLE_DRAW);
	rectangle.drawShape(paneInp, colorPickerFXid, sliderFXid);

    }

    @FXML
    private void drawSquare() {
	SquareShape square = new SquareShape();
	square.dataInitialize(history);
	System.out.println("SQUARE");
	paneInp = mainCtrl.passPane();
	mainCtrl.setState(SQUARE_DRAW);
	square.drawShape(paneInp, colorPickerFXid, sliderFXid);
    }

    @FXML
    private void drawEllipse() {
	EllipseShape ellipse = new EllipseShape();
	ellipse.dataInitialize(history);
	System.out.println("ELLIPSE");
	paneInp = mainCtrl.passPane();
	mainCtrl.setState(ELLIPSE_DRAW);
	ellipse.drawShape(paneInp, colorPickerFXid, sliderFXid);
    }

    @FXML
    private void drawCircle() {
	CircleShape circle = new CircleShape();
	circle.dataInitialize(history);
	System.out.println("CIRCLE");
	paneInp = mainCtrl.passPane();
	mainCtrl.setState(CIRCLE_DRAW);
	circle.drawShape(paneInp, colorPickerFXid, sliderFXid);
    }

    @FXML
    private void drawLine() {
	LineSegment line = new LineSegment();
	line.dataInitialize(history);
	System.out.println("LINE");
	paneInp = mainCtrl.passPane();
	mainCtrl.setState(LINE_DRAW);
	line.drawShape(paneInp, colorPickerFXid, sliderFXid);

    }

    @FXML
    private void drawTriangle() {
	TriangleShape triangle = new TriangleShape();
	triangle.dataInitialize(history);
	System.out.println("TRIANGLE");
	paneInp = mainCtrl.passPane();
	mainCtrl.setState(TRIANGLE_DRAW);
	triangle.drawShape(paneInp, colorPickerFXid, sliderFXid);
    }

    @FXML
    private void draggingTool() {
	paneInp = mainCtrl.passPane();
	canvasInp = mainCtrl.passCanvas();
	canvasInp.toBack();
	// Shapes shape = new PolygonShape();
	// shape.dataInitialize(history);
	mainCtrl.setState(DRAG_MODE);
	System.out.println("DRAG_MODE");
	for (Node component : paneInp.getChildren()) {

	    if (component instanceof Line) {
		LineSegment newLine = new LineSegment();
		newLine.dataInitialize(history);
		newLine.moveShape(component, paneInp);
	    } else if (component instanceof Rectangle) {
		Rectangle r = (Rectangle) component;
		if (r.getWidth() == r.getHeight()) {
		    SquareShape newSquare = new SquareShape();
		    newSquare.dataInitialize(history);
		    newSquare.moveShape(component, paneInp);
		} else {
		    RectangleShape newRectangle = new RectangleShape();
		    newRectangle.dataInitialize(history);
		    newRectangle.moveShape(component, paneInp);
		}
	    } else if (component instanceof Polygon) {
		TriangleShape newTriangle = new TriangleShape();
		newTriangle.dataInitialize(history);
		newTriangle.moveShape(component, paneInp);
	    } else if (component instanceof Ellipse) {
		Ellipse e = (Ellipse) component;
		if (e.getRadiusX() == e.getRadiusY()) {
		    CircleShape newCircle = new CircleShape();
		    newCircle.dataInitialize(history);
		    newCircle.moveShape(component, paneInp);
		} else {
		    EllipseShape newEllipse = new EllipseShape();
		    newEllipse.dataInitialize(history);
		    newEllipse.moveShape(component, paneInp);
		}
	    }

	}

    }

    @FXML
    private void resizingTool() {
	paneInp = mainCtrl.passPane();
	for (Node component : paneInp.getChildren()) {

	    if (component instanceof Line) {
		LineSegment resizeLine = new LineSegment();
		resizeLine.dataInitialize(history);
		resizeLine.resizeShape(component, paneInp);
	    } else if (component instanceof Polygon) {
		TriangleShape resizeTriangle = new TriangleShape();
		resizeTriangle.dataInitialize(history);
		resizeTriangle.resizeShape(component, paneInp);
	    } else if (component instanceof Rectangle) {
		Rectangle r = (Rectangle) component;
		if (r.getWidth() == r.getHeight()) {
		    SquareShape resizeSquare = new SquareShape();
		    resizeSquare.dataInitialize(history);
		    resizeSquare.resizeShape(component, paneInp);
		} else {
		    RectangleShape resizeRectangle = new RectangleShape();
		    resizeRectangle.dataInitialize(history);
		    resizeRectangle.resizeShape(component, paneInp);
		}
	    } else if (component instanceof Ellipse) {
		Ellipse e = (Ellipse) component;
		if (e.getRadiusX() == e.getRadiusY()) {
		    CircleShape resizeCircle = new CircleShape();
		    resizeCircle.dataInitialize(history);
		    resizeCircle.resizeShape(component, paneInp);
		} else {
		    EllipseShape resizeEllipse = new EllipseShape();
		    resizeEllipse.dataInitialize(history);
		    resizeEllipse.resizeShape(component, paneInp);
		}

	    }
	}
    }

    @FXML
    private void undoTool() {

	paneInp = mainCtrl.passPane();
	history.invokeUndoProcess(paneInp);

    }

    @FXML
    private void redoTool() {

	paneInp = mainCtrl.passPane();
	history.invokeRedoProcess(paneInp);

    }

    @FXML
    private void fillingTool() {
	paneInp = mainCtrl.passPane();
	canvasInp = mainCtrl.passCanvas();
	canvasInp.toBack();
	for (Node component : paneInp.getChildren()) {

	    if (component instanceof Line) {
		LineSegment newLine = new LineSegment();
		newLine.dataInitialize(history);
		newLine.fillShape(paneInp, colorPickerFXid.getValue(), component);
	    } else if (component instanceof Rectangle) {
		Rectangle r = (Rectangle) component;
		if (r.getWidth() == r.getHeight()) {
		    SquareShape newSquare = new SquareShape();
		    newSquare.dataInitialize(history);
		    newSquare.fillShape(paneInp, colorPickerFXid.getValue(), component);
		} else {
		    RectangleShape newRectangle = new RectangleShape();
		    newRectangle.dataInitialize(history);
		    newRectangle.fillShape(paneInp, colorPickerFXid.getValue(), component);
		}
	    } else if (component instanceof Polygon) {
		TriangleShape newTriangle = new TriangleShape();
		newTriangle.dataInitialize(history);
		newTriangle.fillShape(paneInp, colorPickerFXid.getValue(), component);
	    } else if (component instanceof Ellipse) {
		Ellipse e = (Ellipse) component;
		if (e.getRadiusX() == e.getRadiusY()) {
		    CircleShape newCircle = new CircleShape();
		    newCircle.dataInitialize(history);
		    newCircle.fillShape(paneInp, colorPickerFXid.getValue(), component);
		} else {
		    EllipseShape newEllipse = new EllipseShape();
		    newEllipse.dataInitialize(history);
		    newEllipse.fillShape(paneInp, colorPickerFXid.getValue(), component);
		}
	    }

	}

    }

    @FXML
    private void deletingTool() {
	paneInp = mainCtrl.passPane();
	canvasInp = mainCtrl.passCanvas();
	canvasInp.toBack();
	for (Node component : paneInp.getChildren()) {

	    if (component instanceof Line) {
		LineSegment newLine = new LineSegment();
		newLine.dataInitialize(history);
		newLine.deleteShape(paneInp, component);
	    } else if (component instanceof Rectangle) {
		Rectangle r = (Rectangle) component;
		if (r.getWidth() == r.getHeight()) {
		    SquareShape newSquare = new SquareShape();
		    newSquare.dataInitialize(history);
		    newSquare.deleteShape(paneInp, component);
		} else {
		    RectangleShape newRectangle = new RectangleShape();
		    newRectangle.dataInitialize(history);
		    newRectangle.deleteShape(paneInp, component);
		}
	    } else if (component instanceof Polygon) {
		TriangleShape newTriangle = new TriangleShape();
		newTriangle.dataInitialize(history);
		newTriangle.deleteShape(paneInp, component);
	    } else if (component instanceof Ellipse) {
		Ellipse e = (Ellipse) component;
		if (e.getRadiusX() == e.getRadiusY()) {
		    CircleShape newCircle = new CircleShape();
		    newCircle.dataInitialize(history);
		    newCircle.deleteShape(paneInp, component);
		} else {
		    EllipseShape newEllipse = new EllipseShape();
		    newEllipse.dataInitialize(history);
		    newEllipse.deleteShape(paneInp, component);
		}
	    }

	}
    }

    private void firstTimeBufferInvocation() {
	if (!firstTimeBuffer) {
	    mainCtrl.invokeFreeSketch(false);
	} else {
	    mainCtrl.invokeFreeSketch(true);
	}
    }

    public void setSliderValue(double lineWidth) {
	sliderFXid.setValue(lineWidth);
    }

    public double getSliderValue() {
	return sliderFXid.getValue();
    }

    public void setColorPickerValue(Color paint) {
	colorPickerFXid.setValue(paint);
    }

    public Color getColorPickerValue() {
	return colorPickerFXid.getValue();
    }

}
