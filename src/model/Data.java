package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private List<String> paneData = new ArrayList<>();

    public Data(Pane pane) {
        this.paneData.add(Double.toString(pane.getWidth()));
        this.paneData.add(Double.toString(pane.getHeight()));
        for (int i=0 ; i < pane.getChildren().size() ; i++) {
            if (!(pane.getChildren().get(i) instanceof Canvas)) {
                this.paneData.add(pane.getChildren().get(i).toString());
            }
        }
    }

    public List<String> getPaneData() {
        return this.paneData;
    }
    
    public void addPaneInfo(String paneInfo) {
        this.paneData.add(paneInfo);
    }
}
