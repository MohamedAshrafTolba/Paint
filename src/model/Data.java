package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Data {

    private Stack<Set<Node>> primaryStack = new Stack<Set<Node>>();
    private Stack<Set<Node>> secondaryStack = new Stack<Set<Node>>();

    private void pushToPrimaryStack(Pane pane) {
	Set<Node> oList = new HashSet<Node>();
	for (Node component : pane.getChildren()) {
	    if (!(component instanceof Canvas)) {
		Object intermediate = getClone(component);
		oList.add((Node) intermediate);

	    }
	}

	primaryStack.push(oList);

    }

    private void undoInvoked(Pane pane) {
	List<Node> nList = new LinkedList<Node>();
	for (Node component : pane.getChildren()) {
	    if (!(component instanceof Canvas)) {
		Node intermediate = component;
		nList.add(intermediate);
	    }
	}

	pane.getChildren().removeAll(nList);

	secondaryStack.push(primaryStack.peek());
	primaryStack.pop();

	if (!primaryStack.isEmpty()) {
	    for (Node componentTwo : primaryStack.peek()) {
		Object intermediate = getClone(componentTwo);
		pane.getChildren().add((Node) intermediate);
	    }
	}

    }

    private void redoInvoked(Pane pane) {
	System.out.println("Elements in pane:" + pane.getChildren());
	List<Node> nList = new LinkedList<Node>();
	for (Node component : pane.getChildren()) {
	    if (!(component instanceof Canvas)) {
		Node intermediate = component;
		nList.add(intermediate);
	    }
	}

	pane.getChildren().removeAll(nList);

	primaryStack.push(secondaryStack.peek());
	secondaryStack.pop();
	if (!primaryStack.isEmpty()) {
	    for (Node componentTwo : primaryStack.peek()) {
		Object intermediate = getClone(componentTwo);
		pane.getChildren().add((Node) intermediate);
	    }
	}
    }

    private void clearSecondaryStack() {
	secondaryStack.clear();
    }

    public Object getClone(Object node) {
	if (node instanceof Line) {
	    Line newLine = new Line();
	    newLine.setStartX(((Line) node).getStartX());
	    newLine.setStartY(((Line) node).getStartY());
	    newLine.setEndX(((Line) node).getEndX());
	    newLine.setEndY(((Line) node).getEndY());
	    newLine.setStroke(((Line) node).getStroke());
	    newLine.setStrokeWidth(((Line) node).getStrokeWidth());

	    return newLine;
	} else if (node instanceof Ellipse) {
	    Ellipse newEllipse = new Ellipse();
	    newEllipse.setCenterX(((Ellipse) node).getCenterX());
	    newEllipse.setCenterY(((Ellipse) node).getCenterY());
	    newEllipse.setRadiusX(((Ellipse) node).getRadiusX());
	    newEllipse.setRadiusY(((Ellipse) node).getRadiusY());
	    newEllipse.setStroke(((Ellipse) node).getStroke());
	    newEllipse.setStrokeWidth(((Ellipse) node).getStrokeWidth());
	    newEllipse.setFill(((Ellipse) node).getFill());

	    return newEllipse;
	} else if (node instanceof Rectangle) {
	    Rectangle newRectangle = new Rectangle();
	    newRectangle.setWidth(((Rectangle) node).getWidth());
	    newRectangle.setHeight(((Rectangle) node).getHeight());
	    newRectangle.setX(((Rectangle) node).getX());
	    newRectangle.setY(((Rectangle) node).getY());
	    newRectangle.setStroke(((Rectangle) node).getStroke());
	    newRectangle.setStrokeWidth(((Rectangle) node).getStrokeWidth());
	    newRectangle.setFill(((Rectangle) node).getFill());

	    return newRectangle;
	} else if (node instanceof Polygon) {
	    Polygon newPolygon = new Polygon();

	    ObservableList<Double> x;
	    x = ((Polygon) node).getPoints();
	    System.out.println(x);
	    newPolygon.getPoints().add(x.get(0));
	    newPolygon.getPoints().add(x.get(1));
	    newPolygon.getPoints().add(x.get(2));
	    newPolygon.getPoints().add(x.get(3));
	    newPolygon.getPoints().add(x.get(4));
	    newPolygon.getPoints().add(x.get(5));
	    newPolygon.setStroke(((Polygon) node).getStroke());
	    newPolygon.setStrokeWidth(((Polygon) node).getStrokeWidth());
	    newPolygon.setFill(((Polygon) node).getFill());
	    return newPolygon;
	}

	return null;
    }

    public void shapeDrawn(Pane pane) {
	pushToPrimaryStack(pane);
	clearSecondaryStack();
    }

    public int getPrimaryStackSize() {
	return primaryStack.size();
    }

    public int getSecondaryStackSize() {
	return secondaryStack.size();
    }

    public void invokeUndoProcess(Pane pane) {
	undoInvoked(pane);
    }

    public void invokeRedoProcess(Pane pane) {
	redoInvoked(pane);
    }
    
    public void clearStacks() {
	primaryStack.clear();
	secondaryStack.clear();
    }
 
}
