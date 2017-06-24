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

/**
 * This class represents the main controller which controls the main functionality of the application
 * and distributes them among other controllers to handled.
 */
public class MainController {
	/**
	 * The ScrollPane FXML instance.
	 */
	@FXML
    private ScrollPane scrollPane;
	/**
	 * The Canvas FXML instance.
	 */
    @FXML
    private Canvas canvas;
	/**
	 * The Pane FXML instance.
	 */
    @FXML
    private Pane pane;
	/**
	 * The ToolBarController singleton instance.
	 */
	@FXML
	private ToolBarController toolBarController;
	/**
	 * The MenuBarController singleton instance.
	 */
    @FXML
	private MenuBarController menuBarController;
	/**
	 * Minimum number of children of the pane.
	 */
	private static final int MIN_CHILD = 1;
	/**
	 * Maximum height of the drawing sheet.
	 */
    static final double MAX_HEIGHT = 768;
	/**
	 * Maximum width of the drawing sheet.
	 */
	static final double MAX_WIDTH = 1366;
	/**
	 * Enum represents the current state(operation).
	 */
    private static enums.State state;
	/**
	 * GraphicsContext of the canvas.
	 */
	private GraphicsContext graphicsContext;

	/**
	 * Instantiate instances from the sub-controllers' classes once the FXML file
	 * corresponding to this controller is loaded and those instances mentioned above
	 * is injected from that FXML file to this class.
	 */
    @FXML
    public void initialize() {
		toolBarController.initialize(this);
		menuBarController.initialize(this);
    }

	/**
	 * Sets the canvas's dimensions (width - height) to certain values
	 * and clears its contents as it produces a new drawing sheet.
	 * @param width A double value which represents the width of the canvas in pixels.
	 * @param height A double value which represents the height of the canvas in pixels.
	 */
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

	/**
	 * Handles the free-sketching using pencil or brush and the erasing work as well on the canvas.
	 */
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
			}
		});

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
			}
		});

		canvas.setOnMouseReleased(event -> {
			if (state == BRUSH_SKETCHING || state == ERASE_SKETCHING || state == PENCIL_SKETCHING) {
				graphicsContext.closePath();
				event.consume();
			}
		});
    }

	/**
	 * A getter "accessor" for the pane.
	 * @return An object represents the pane.
	 */
	public Pane getPane() {
		return this.pane;
    }

	/**
	 * A getter "accessor" for the canvas.
	 * @return An object represents the canvas.
	 */
	Canvas getCanvas() {
		return this.canvas;
    }

	/**
	 * A setter "modifier" for the pane.
	 * @param pane An object represents the new pane.
	 */
	public void setPane(Pane pane) {
		this.pane = pane;
    }

	/**
	 * A setter "modifier" for the current state(operation).
	 * @param state An enum that represents the required state.
	 */
	void setState(enums.State state) {
    	MainController.state = state;
	}
}