package controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane anchorPaneFXid;
    @FXML
	ToolBarController toolBarController;
    @FXML
	MenuBarController menuBarController;

    

    private static final int PENCIL_TOOL = 1;
    private static final int BRUSH_TOOL = 2;
    private static final int ERASER_TOOL = 3;
    private static final int MIN_CHILD = 1;

    static int state;
    private static boolean changeOccur;
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
	
	if (paneFXid.getChildren().size() > MIN_CHILD) {
	    List<Node> removeList = new LinkedList<Node>();
	    for (Node child : paneFXid.getChildren()) {
		if (!(child instanceof Canvas)) {
		    removeList.add(child);
		}
	    }
	    paneFXid.getChildren().removeAll(removeList);
	}
	
	toolBarController.clearData();
	scrollPaneFXid.setContent(paneFXid);

    }

    public void invokeNewCanvas(double width, double height) {
	setCanvasNewSize(width, height);
    }

    private void startFreeSketching(boolean actionSet) {

	/// LOGGING THE STATE
	System.out.println("state = " + state);

	if (state == ERASER_TOOL) {
	    toolBarController.setColorPickerValue(Color.WHITE);
	}

	if (!actionSet) {

	    canvasFXid.setOnMousePressed(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

		    //// LOGGING THE STATE
		    // System.out.println("state = " + state);

		    if (state == BRUSH_TOOL || state == ERASER_TOOL || state == PENCIL_TOOL) {
			event.consume();
			graphContextIn.setStroke(toolBarController.getColorPickerValue());
			graphContextIn.setLineWidth(toolBarController.getSliderValue());
			graphContextIn.beginPath();
			graphContextIn.moveTo(event.getX(), event.getY());
			graphContextIn.stroke();
			changeOccur = true;
			System.out.println(changeOccur + "change occur wohoo");

		    }
		}
	    });

	    canvasFXid.setOnMouseDragged(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
		    if (state == BRUSH_TOOL || state == ERASER_TOOL || state == PENCIL_TOOL) {
			event.consume();

			if (state == BRUSH_TOOL) {
			    graphContextIn.setLineCap(StrokeLineCap.ROUND);
			    graphContextIn.setLineJoin(StrokeLineJoin.ROUND);
			} else if (state == PENCIL_TOOL || state == ERASER_TOOL) {
			    graphContextIn.setLineCap(StrokeLineCap.SQUARE);
			    graphContextIn.setLineJoin(StrokeLineJoin.BEVEL);
			}

			graphContextIn.lineTo(event.getX(), event.getY());
			graphContextIn.stroke();

		    }
		}
	    });

	    canvasFXid.setOnMouseReleased(new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
		    if (state == BRUSH_TOOL || state == ERASER_TOOL || state == PENCIL_TOOL) {
			graphContextIn.closePath();
			event.consume();
			
		    }

		}

	    });

	}
	
    }

  

    public void invokeFreeSketch(boolean flag) {
	graphContextIn = canvasFXid.getGraphicsContext2D();
	canvasFXid.toFront();
	startFreeSketching(flag);

    }

    public Pane passPane() {
	return this.paneFXid;
    }

    public Canvas passCanvas() {
	return this.canvasFXid;
    }
    
    public void setPane(Pane newPane) {
	this.paneFXid =  newPane;
    }

    public void setState(int stateInp) {
	state = stateInp;
    }
    
    public void getFileName(File file) {
	toolBarController.accessFileLoader(file);
    }
    
    public void adjustSheetAfterLoad(Pane pane , double width ,double height) {
	
//	List<Node> remLPane = new LinkedList<Node>();
//	
//	for (Node child : pane.getChildren()) {
//	    if (!(child instanceof Canvas)) {
//		remLPane.add(child);
//	    }
//	}
//	pane.getChildren().removeAll(remLPane);
//	
//	List<Node> remLPaneFXid = new LinkedList<Node>();
//	
//	for (Node child : paneFXid.getChildren()) {
//	    if (!(child instanceof Canvas)) {
//		remLPaneFXid.add(child);
//	    }
//	}
//	
//	paneFXid.getChildren().removeAll(remLPaneFXid);
//	
//	for (Node child : remLPaneFXid) {
//	    paneFXid
//	}
//	
//	this.paneFXid = pane;
	this.paneFXid.setMaxWidth(width+1.0);
	this.paneFXid.setMinWidth(width+1.0);
	this.paneFXid.setPrefWidth(width+1.0);
	this.paneFXid.setMaxHeight(height+1.0);
	this.paneFXid.setMinHeight(height+1.0);
	this.paneFXid.setPrefHeight(height+1.0);
	this.canvasFXid.setWidth(width);
	this.canvasFXid.setHeight(height);
    }

}
