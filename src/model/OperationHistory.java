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

public class OperationHistory {

    /**
     * A stack which contains the sequence of operations mainly for undo operations.
     */
    private Stack<Set<Node>> primaryStack = new Stack<>();

    /**
     * A stack which contains the sequence of operations mainly for redo operations.
     */
    private Stack<Set<Node>> secondaryStack = new Stack<>();

    /**
     * Pushes a pane data to the primary stack.
     * @param pane The pane whose data to be added to the primary stack.
     */
    private void pushToPrimaryStack(Pane pane) {
        Set<Node> oList = new HashSet<>();
        for (Node node : pane.getChildren()) {
            if (!(node instanceof Canvas)) {
                Object intermediate = getClone(node);
                oList.add((Node) intermediate);
            }
        }
        primaryStack.push(oList);
    }

    /**
     * Undoes an operation.
     * @param pane The pane which is required to undo the last operation done on it.
     */
    public void undo(Pane pane) {
        List<Node> nList = new LinkedList<>();
        for (Node node : pane.getChildren()) {
            if (!(node instanceof Canvas)) {
                nList.add(node);
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

    /**
     * Redoes an operation.
     * @param pane The pane which is required to redo the last operation un-done from it.
     */
    public void redo(Pane pane) {
        List<Node> nList = new LinkedList<>();
        for (Node node : pane.getChildren()) {
            if (!(node instanceof Canvas)) {
                nList.add(node);
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

    /**
     * Clones an object to another object.
     * @param object The object which wanted to be cloned to another object.
     * @return The cloned object.
     */
    private Object getClone(Object object) {
        if (object instanceof Line) {
            Line newLine = new Line();
            newLine.setStartX(((Line) object).getStartX());
            newLine.setStartY(((Line) object).getStartY());
            newLine.setEndX(((Line) object).getEndX());
            newLine.setEndY(((Line) object).getEndY());
            newLine.setStroke(((Line) object).getStroke());
            newLine.setStrokeWidth(((Line) object).getStrokeWidth());
            return newLine;
        } else if (object instanceof Ellipse) {
            Ellipse newEllipse = new Ellipse();
            newEllipse.setCenterX(((Ellipse) object).getCenterX());
            newEllipse.setCenterY(((Ellipse) object).getCenterY());
            newEllipse.setRadiusX(((Ellipse) object).getRadiusX());
            newEllipse.setRadiusY(((Ellipse) object).getRadiusY());
            newEllipse.setStroke(((Ellipse) object).getStroke());
            newEllipse.setStrokeWidth(((Ellipse) object).getStrokeWidth());
            newEllipse.setFill(((Ellipse) object).getFill());
            return newEllipse;
        } else if (object instanceof Rectangle) {
            Rectangle newRectangle = new Rectangle();
            newRectangle.setWidth(((Rectangle) object).getWidth());
            newRectangle.setHeight(((Rectangle) object).getHeight());
            newRectangle.setX(((Rectangle) object).getX());
            newRectangle.setY(((Rectangle) object).getY());
            newRectangle.setStroke(((Rectangle) object).getStroke());
            newRectangle.setStrokeWidth(((Rectangle) object).getStrokeWidth());
            newRectangle.setFill(((Rectangle) object).getFill());
            return newRectangle;
        } else if (object instanceof Polygon) {
            Polygon newPolygon = new Polygon();
            ObservableList<Double> x;
            x = ((Polygon) object).getPoints();
            newPolygon.getPoints().add(x.get(0));
            newPolygon.getPoints().add(x.get(1));
            newPolygon.getPoints().add(x.get(2));
            newPolygon.getPoints().add(x.get(3));
            newPolygon.getPoints().add(x.get(4));
            newPolygon.getPoints().add(x.get(5));
            newPolygon.setStroke(((Polygon) object).getStroke());
            newPolygon.setStrokeWidth(((Polygon) object).getStrokeWidth());
            newPolygon.setFill(((Polygon) object).getFill());
            return newPolygon;
        }
        return null;
    }

    /**
     * Adds a pane to a primary stack and clears the secondary stack.
     * @param pane The pane which is required to be added to the primary stack.
     */
    void shapeDrawn(Pane pane) {
        pushToPrimaryStack(pane);
        clearSecondaryStack();
    }

    /**
     * A getter "accessor" to the primary stack's size.
     * @return The size of the primary stack.
     */
    public int getPrimaryStackSize() {
        return primaryStack.size();
    }

    /**
     * A getter "accessor" to the secondary stack's size.
     * @return The size of the secondary stack.
     */
    public int getSecondaryStackSize() {
        return secondaryStack.size();
    }

    /**
     * Clears the primary stack.
     */
    public void clearPrimaryStack() {
        primaryStack.clear();
    }

    /**
     * Clears the secondary stack.
     */
    public void clearSecondaryStack() {
        secondaryStack.clear();
    }
}