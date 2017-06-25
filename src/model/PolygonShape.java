package model;

import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

/**
 * This class represents the ADT of the polygon shape.
 */
public abstract class PolygonShape extends Shapes {

    PolygonShape(OperationHistory operationHistory) {
        this.operationHistory = operationHistory;
    }

    public abstract void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth);

    public abstract void resizeShape(Node node, Pane pane);

    public abstract void setCursor(Node node, double stateX, double stateY);
    
}