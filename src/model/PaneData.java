package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for representing the serializable form of the drawing.
 */
public class PaneData {

    /**
     * A list of strings which contains the information of the pane represented as strings.
     */
    private List<String> paneData = new ArrayList<>();

    /**
     * A constructor for this class objects.
     * @param pane The pane which represents the drawing to be serialized.
     */
    public PaneData(Pane pane) {
        this.paneData.add(Double.toString(pane.getWidth()));
        this.paneData.add(Double.toString(pane.getHeight()));
        for (int i=0 ; i < pane.getChildren().size() ; i++) {
            if (!(pane.getChildren().get(i) instanceof Canvas)) {
                this.paneData.add(pane.getChildren().get(i).toString());
            }
        }
    }

    /**
     * A getter "accessor" for the list of strings which represents the pane.
     * @return The list of strings which represents the pane and its children.
     */
    public List<String> getPaneData() {
        return this.paneData;
    }

    /**
     * Adds an information of the pane in the form of a string
     * to the list of strings which represents the pane information.
     * @param paneInfo The info which is required to be added.
     */
    public void addPaneInfo(String paneInfo) {
        this.paneData.add(paneInfo);
    }
}