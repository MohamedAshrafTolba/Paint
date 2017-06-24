package model;

import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public abstract class PolygonShape extends Shapes {

    public abstract void drawShape(Pane pane, ColorPicker pickColor, Slider lineWidth);

    public abstract void resizeShape(Node node, Pane pane);

    public abstract void setCursor(Node node, double stateX, double stateY);
    
}