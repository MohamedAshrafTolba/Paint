package controller.SubController;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MenuBarCtrl {

    @FXML
    TextField canvasWidthId;

    @FXML
    TextField canvasHeightId;

    private MainController mainCtrl;

    @FXML
    private void newCanvasSize() {
	double width = Double.parseDouble(canvasWidthId.getText());
	double height = Double.parseDouble(canvasHeightId.getText());
	mainCtrl.invokeNewCanvas(width, height);

    }

    public void instantiate(MainController mainController) {
	mainCtrl = mainController;
    }

}
