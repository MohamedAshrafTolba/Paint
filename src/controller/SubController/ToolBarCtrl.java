package controller.SubController;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class ToolBarCtrl {
    private static final int PENCIL_TOOL = 1;
    private static final int BRUSH_TOOL = 2;
    private static final int ERASER_TOOL = 3;

    private static boolean firstTimeBuffer;
    private MainController mainCtrl;

    @FXML
    private ColorPicker colorPickerFXid;
    @FXML
    private Slider sliderFXid;

    public void instantiate(MainController mainController) {
	mainCtrl = mainController;
    }

    @FXML
    private void pencilSketching() {

	if (!firstTimeBuffer) {
	    mainCtrl.invokeFreeSketch(PENCIL_TOOL, false);
	    firstTimeBuffer = true;
	} else {
	    mainCtrl.invokeFreeSketch(PENCIL_TOOL, true);
	}

    }

    @FXML
    private void burshSketching() {

	if (!firstTimeBuffer) {
	    mainCtrl.invokeFreeSketch(BRUSH_TOOL, false);
	    firstTimeBuffer = true;
	} else {
	    mainCtrl.invokeFreeSketch(BRUSH_TOOL, true);
	}

    }

    @FXML
    private void fillingbackground() {
	mainCtrl.invokeFillingTool();
    }

    @FXML
    private void eraserTool() {
	
	if (!firstTimeBuffer) {
	    mainCtrl.invokeFreeSketch(ERASER_TOOL, false);
	    firstTimeBuffer = true;
	} else {
	    mainCtrl.invokeFreeSketch(ERASER_TOOL, true);
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
