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
    private static final int MIN_SIZE = 0;

    private void pushToPrimaryStack(Pane pane) {
	Set<Node> oList = new HashSet<Node>();

	System.out.println("EVERY THING IN PANE BEFORE PROCESSING NODES:" + pane.getChildren());
	for (Node component : pane.getChildren()) {
	    if (!(component instanceof Canvas)) {
		Object intermediate = getClone(component);
		oList.add((Node) intermediate);

	    }
	}

	// System.out.println("HELLO FROM PUSH TO PRIMARY STACK EVERY THINGIN
	// LIST :" + oList);

	primaryStack.push(oList);
	// System.out.println(primaryStack.peek());

    }

    private void undoInvoked(Pane pane) {
	if (primaryStack.size() != MIN_SIZE) {
	    // // System.out.println("HELLO UNDO");
	    // // System.out.println(primaryStack.size());
	    // System.out.println("PRIMARY STACK CONTENTS");
	    // System.out.println(primaryStack);
	    List<Node> nList = new LinkedList<Node>();
	    for (Node component : pane.getChildren()) {
		if (!(component instanceof Canvas)) {
		    Node intermediate = component;
		    nList.add(intermediate);
		}
	    }

	    pane.getChildren().removeAll(nList);

//	    System.out.println("EVERYTHING IN PANE ON INVOCATION OF UNDO :" + pane.getChildren());

	    secondaryStack.push(primaryStack.peek());
	    primaryStack.pop();

	    if (!primaryStack.isEmpty()) {
		for (Node componentTwo : primaryStack.peek()) {
		    Object intermediate = getClone(componentTwo);
		    pane.getChildren().add((Node) intermediate);
		}
	    }

	}
    }

    private void redoInvoked(Pane pane) {
	if (secondaryStack.size() != MIN_SIZE) {

	   System.out.println("Elements in pane:" + pane.getChildren());
	    List<Node> nList = new LinkedList<Node>();
	    for (Node component : pane.getChildren()) {
		if (!(component instanceof Canvas)) {
		    Node intermediate = component;
		    nList.add(intermediate);
		}
	    }
	    
	    pane.getChildren().removeAll(nList);
	    
	    System.out.println("All elements to be removed from pane :" + nList);
	    System.out.println("Elements in sec. stack peek :" + secondaryStack.peek());
	    primaryStack.push(secondaryStack.peek());
	    secondaryStack.pop();
	    if (!primaryStack.isEmpty()) {
		for (Node componentTwo : primaryStack.peek()) {
		    Object intermediate = getClone(componentTwo);
		    pane.getChildren().add((Node) intermediate);
		}
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

    public void invokeUndoProcess(Pane pane) {
	undoInvoked(pane);
    }

    public void invokeRedoProcess(Pane pane) {
	redoInvoked(pane);
    }

}
