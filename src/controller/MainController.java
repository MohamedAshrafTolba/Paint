package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import model.enums;

import java.util.LinkedList;
import java.util.List;

import static model.enums.State.*;

public class MainController {

	@FXML
    private ScrollPane scrollPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Pane pane;

	@FXML
	private ToolBarController toolBarController;

    @FXML
	private MenuBarController menuBarController;

    private static final int MIN_CHILD = 1;
    static final double MAX_HEIGHT = 768;
    static final double MAX_WIDTH = 1366;
    private static enums.State state;

	private GraphicsContext graphicsContext;

    @FXML
    public void initialize() {
		toolBarController.initialize(this);
		menuBarController.initialize(this);
    }

    void setCanvasDimensions(double width, double height) {
		pane.setMinWidth(width + 1.0);
		pane.setMinHeight(height + 1.0);
		pane.setMaxWidth(width + 1.0);
		pane.setMaxHeight(height + 1.0);
		pane.setPrefWidth(width + 1.0);
		pane.setPrefHeight(height + 1.0);
		canvas.setWidth(width);
		canvas.setHeight(height);
		if (graphicsContext != null) {
			graphicsContext.clearRect(0, 0, width, height);
		}
		if (pane.getChildren().size() > MIN_CHILD) {
			List<Node> removeList = new LinkedList<>();
			for (Node child : pane.getChildren()) {
				if (!(child instanceof Canvas)) {
					removeList.add(child);
				}
			}
			pane.getChildren().removeAll(removeList);
		}
		toolBarController.clearData();
		scrollPane.setContent(pane);
    }

    void freeSketching() {
		graphicsContext = canvas.getGraphicsContext2D();
		canvas.toFront();
		if (state == ERASE_SKETCHING) {
			toolBarController.setColorPickerValue(Color.WHITE);
		}
		canvas.setOnMousePressed(event -> {
			if (state == BRUSH_SKETCHING || state == ERASE_SKETCHING || state == PENCIL_SKETCHING) {
				event.consume();
				graphicsContext.setStroke(toolBarController.getColorPickerValue());
				graphicsContext.setLineWidth(toolBarController.getSliderValue());
				graphicsContext.beginPath();
				graphicsContext.moveTo(event.getX(), event.getY());
				graphicsContext.stroke();
			}});

		canvas.setOnMouseDragged(event -> {
			if (state == BRUSH_SKETCHING || state == ERASE_SKETCHING || state == PENCIL_SKETCHING) {
				event.consume();
				if (state == BRUSH_SKETCHING) {
					graphicsContext.setLineCap(StrokeLineCap.ROUND);
					graphicsContext.setLineJoin(StrokeLineJoin.ROUND);
				} else if (state == PENCIL_SKETCHING || state == ERASE_SKETCHING) {
					graphicsContext.setLineCap(StrokeLineCap.SQUARE);
					graphicsContext.setLineJoin(StrokeLineJoin.BEVEL);
				}
				graphicsContext.lineTo(event.getX(), event.getY());
				graphicsContext.stroke();
			}});

		canvas.setOnMouseReleased(event -> {
			if (state == BRUSH_SKETCHING || state == ERASE_SKETCHING || state == PENCIL_SKETCHING) {
				graphicsContext.closePath();
				event.consume();
			}});
    }

    public Pane getPane() {
		return this.pane;
    }

    Canvas getCanvas() {
		return this.canvas;
    }
    
    public void setPane(Pane pane) {
		this.pane = pane;
    }

	void setState(enums.State state) {
    	MainController.state = state;
	}
}