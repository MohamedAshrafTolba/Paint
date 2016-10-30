package controller;

import controller.SubController.MenuBarCtrl;
import controller.SubController.ToolBarCtrl;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class MainController {

    @FXML
    private ScrollPane scrollPaneFXid;
    @FXML
    private Canvas canvasFXid;
    @FXML
    private Pane paneFXid;
    @FXML
    ToolBarCtrl toolBarController;
    @FXML
    MenuBarCtrl menuBarController;

    private static final int PENCIL_TOOL = 1;
    private static final int BRUSH_TOOL = 2;
    private static final int ERASER_TOOL = 3;

    static int state;
    private GraphicsContext graphContextIn;

    @FXML
    public void initialize() {

	toolBarController.instantiate(this);
	menuBarController.instantiate(this);
    }

    private void setCanvasNewSize(double width, double height) {

	paneFXid.setMinWidth(width + 1.0);
	paneFXid.setMinHeight(height + 1.0);

	paneFXid.setMaxWidth(width + 1.0);
	paneFXid.setMaxHeight(height + 1.0);

	paneFXid.setPrefWidth(width + 1.0);
	paneFXid.setPrefHeight(height + 1.0);

	canvasFXid.setHeight(height);
	canvasFXid.setWidth(width);

	if (graphContextIn != null) {
	    graphContextIn.clearRect(0, 0, width, height);
	}
        scrollPaneFXid.setPrefViewportWidth(paneFXid.getWidth()+10.0);
        scrollPaneFXid.setPrefViewportHeight(paneFXid.getHeight()+10.0);
        scrollPaneFXid.setMinHeight(paneFXid.getHeight() + 10.0);
        scrollPaneFXid.setMinWidth(paneFXid.getWidth() + 10.0);
	scrollPaneFXid.setContent(paneFXid);

    }

    public void invokeNewCanvas(double width, double height) {
	setCanvasNewSize(width, height);
    }

    private void startFreeSketching(boolean set) {
	if (state == 3) {
	    toolBarController.setColorPickerValue(Color.WHITE);
	}

	if (!set) {

	    canvasFXid.setOnMousePressed(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

		    graphContextIn.setStroke(toolBarController.getColorPickerValue());
		    graphContextIn.setLineWidth(toolBarController.getSliderValue());
		    graphContextIn.beginPath();
		    graphContextIn.moveTo(event.getX(), event.getY());
		    graphContextIn.stroke();

		}
	    });

	    canvasFXid.setOnMouseDragged(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
		    /// The brush tool can be developed later on to look more
		    /// different than the normal pencil
		    /// noting that the main difference between both occur on
		    /// large size where rendering process appear to be more
		    /// clear in brush mode

		    if (state == 2) {
			graphContextIn.setLineCap(StrokeLineCap.ROUND);
			graphContextIn.setLineJoin(StrokeLineJoin.ROUND);
		    } else {
			graphContextIn.setLineCap(StrokeLineCap.SQUARE);
			graphContextIn.setLineJoin(StrokeLineJoin.BEVEL);
		    }

		    graphContextIn.lineTo(event.getX(), event.getY());
		    graphContextIn.stroke();

		}
	    });

	}
    }

    public void invokeFreeSketch(int stateInput, boolean set) {
	graphContextIn = canvasFXid.getGraphicsContext2D();
	if (stateInput == PENCIL_TOOL) {
	    state = PENCIL_TOOL;
	    startFreeSketching(set);
	} else if (stateInput == BRUSH_TOOL) {
	    state = BRUSH_TOOL;
	    startFreeSketching(set);
	} else {
	    state = ERASER_TOOL;
	    startFreeSketching(set);
	}

    }

    private void fillingTool() {
	graphContextIn.setFill(toolBarController.getColorPickerValue());
	graphContextIn.fillRect(0.0, 0.0, canvasFXid.getWidth(), canvasFXid.getHeight());
    }

    public void invokeFillingTool() {
	graphContextIn = canvasFXid.getGraphicsContext2D();
	fillingTool();
    }

}
