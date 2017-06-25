package controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.PaneData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents the menu bar controller which is responsible for handling
 * the operations that can be done from the menu bar as save/load, close, new sheet and so on.
 */
public class MenuBarController {

	/**
	 * The width of the canvas TextField FXML instance.
	 */
	@FXML
	private TextField canvasWidth;

	/**
	 * The Height of the canvas TextField FXML instance.
	 */
	@FXML
	private TextField canvasHeight;

	/**
	 * A pane instance.
	 */
	private Pane pane;

	/**
	 * A canvas instance.
	 */
	private Canvas canvas;

	/**
	 * The main controller singleton instance which controls the main functionality
	 * and the flow of control between the modules.
	 */
	private MainController mainController;

	/**
	 * Sets the canvas to new dimensions and clear its contents as it produces a new drawing sheet.
	 */
	@FXML
	private void newCanvasSize() {
		double paneWidth = Double.parseDouble(canvasWidth.getText());
		double paneHeight = Double.parseDouble(canvasHeight.getText());
		if (paneHeight > MainController.MAX_HEIGHT
                && paneWidth <= MainController.MAX_WIDTH) {
		    mainController.setCanvasDimensions(paneWidth, MainController.MAX_HEIGHT);
        } else if (paneWidth > MainController.MAX_WIDTH
                && paneHeight <= MainController.MAX_HEIGHT){
            mainController.setCanvasDimensions(MainController.MAX_WIDTH, paneHeight);
        } else if (paneWidth > MainController.MAX_WIDTH
                && paneHeight > MainController.MAX_HEIGHT) {
            mainController.setCanvasDimensions(MainController.MAX_WIDTH, MainController.MAX_HEIGHT);
        } else {
            mainController.setCanvasDimensions(paneWidth, paneHeight);
        }
	}
	/**
	 * Closes the application.
	 */
	@FXML
	private void closeApplication() {
		Platform.exit();
	}

	/**
	 * Saves the drawing to XML or JSON files according to the user choice.
	 * @throws IOException An exception thrown if an error occurred while saving the drawing.
	 */
	@FXML
	private void saveFile() throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter xmlExtensionFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		FileChooser.ExtensionFilter jsonExtensionFilter = new FileChooser.ExtensionFilter(
				"JSON files (*.json)", "*.json");
		fileChooser.getExtensionFilters().addAll(xmlExtensionFilter, jsonExtensionFilter);
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			String filePath = file.getPath();
			pane = mainController.getPane();
			canvas = mainController.getCanvas();
			if (filePath.endsWith(".xml")) {
				saveXMLFile(file);
			} else {
				saveJSONFile(file);
			}
		}
	}

	/**
	 * Saves the drawing to XML file.
	 * @param file The file which contains the path of the file.
	 * @throws IOException An exception thrown if an error occurred while serializing the data of the object.
	 */
	private void saveXMLFile(File file) throws IOException {
		XStream xStream = new XStream(new StaxDriver());
		PaneData paneData = new PaneData(pane);
		xStream.alias("Pane", PaneData.class);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		xStream.toXML(paneData, fileOutputStream);
		fileOutputStream.close();
	}

	/**
	 * Saves the drawing to JSON file.
	 * @param file The file which contains the path of the file.
	 * @throws IOException An exception thrown if an error occurred while serializing the data of the object.
	 */
	private void saveJSONFile(File file) throws IOException {
		XStream xStream = new XStream(new JettisonMappedXmlDriver());
		PaneData paneData = new PaneData(pane);
		xStream.alias("Pane", PaneData.class);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		xStream.toXML(paneData, fileOutputStream);
		fileOutputStream.close();
	}

	/**
	 * Loads the drawing from XML or JSON files according to the user choice.
	 * @throws IOException An exception thrown if an error occurred while loading the drawing.
	 */
	@FXML
	private void loadFile() throws IOException {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		pane = mainController.getPane();
		canvas = mainController.getCanvas();
		if (file != null) {
			String filePath = file.getPath();
			if (filePath.endsWith(".xml")) {
				loadXMLFile(file);
			} else {
				loadJSONFile(file);
			}
		}
	}

	/**
	 * Loads the drawing from XML file.
	 * @param file The file which contains the path of the file.
	 * @throws IOException An exception thrown if an error occurred while de-serializing the data of the object.
	 */
	private void loadXMLFile(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		XStream xStream = new XStream(new StaxDriver());
		xStream.alias("Pane", PaneData.class);
		PaneData data = (PaneData) xStream.fromXML(fileReader);
		List<String> paneData = data.getPaneData();
		constructPane(paneData);
	}

	/**
	 * Loads the drawing from JSON file.
	 * @param file The file which contains the path of the file.
	 * @throws IOException An exception thrown if an error occurred while de-serializing the data of the object.
	 */
	private void loadJSONFile(File file) throws IOException{
		FileReader fileReader = new FileReader(file);
		XStream xStream = new XStream(new JettisonMappedXmlDriver());
		xStream.alias("Pane", PaneData.class);
		PaneData data = (PaneData) xStream.fromXML(fileReader);
		List<String> paneData = data.getPaneData();
		constructPane(paneData);
	}

	/**
	 * Constructs the pane from the de-serialized object after loading it.
	 * @param paneData A list of strings which represents the pane data(Shapes that were drawn on the pane).
	 */
	private void constructPane(List<String> paneData) {
		List<Double> data = new ArrayList<>();
		double paneWidth = Double.parseDouble(paneData.get(0));
		double paneHeight = Double.parseDouble(paneData.get(1));
		List<Node> removeChildrenList = new LinkedList<>();
		for (Node child : pane.getChildren()) {
			if (!(child instanceof Canvas)) {
				removeChildrenList.add(child);
			}
		}
		pane.getChildren().removeAll(removeChildrenList);
		for (int i = 2; i < paneData.size(); i++) {
			String node = paneData.get(i);
			data.clear();
			Matcher matcher = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(node);
			while (matcher.find()) {
				double value = Double.parseDouble(matcher.group());
				data.add(value);
			}
			String strokeColor = "";
			String fillColor = "";
			if (node.contains("stroke=")) {
				int startStrokeColor = node.indexOf("stroke=") + 7;
				int endStrokeColor = startStrokeColor + 10;
				strokeColor = node.substring(startStrokeColor, endStrokeColor);
			}
			if (node.contains("fill=")) {
				int startFillColor = node.indexOf("fill=") + 5;
				int endFillColor = startFillColor + 10;
				fillColor = node.substring(startFillColor, endFillColor);
			}
			if (node.contains("Line")) {
				Line line = new Line(data.get(0), data.get(1), data.get(2), data.get(3));
				line.setStrokeWidth(data.get(data.size() - 1));
				line.setStroke(Color.web(strokeColor));
				pane.getChildren().add(line);
			} else if (node.contains("Polygon")) {
				Polygon polygon = new Polygon();
				polygon.getPoints().addAll(
						data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5));
				polygon.setStrokeWidth(data.get(data.size() - 1));
				polygon.setStroke(Color.web(strokeColor));
				polygon.setFill(Color.web(fillColor));
				pane.getChildren().add(polygon);
			} else if (node.contains("Rectangle")) {
				Rectangle rectangle = new Rectangle(data.get(0), data.get(1), data.get(2), data.get(3));
				rectangle.setStrokeWidth(data.get(data.size() - 1));
				rectangle.setStroke(Color.web(strokeColor));
				rectangle.setFill(Color.web(fillColor));
				pane.getChildren().add(rectangle);
			} else if (node.contains("Ellipse")) {
				Ellipse ellipse = new Ellipse(data.get(0), data.get(1), data.get(2), data.get(3));
				ellipse.setStrokeWidth(data.get(data.size() - 1));
				ellipse.setStroke(Color.web(strokeColor));
				ellipse.setFill(Color.web(fillColor));
				pane.getChildren().add(ellipse);
			}
		}
		this.pane.setMaxWidth(paneWidth+1.0);
		this.pane.setMinWidth(paneWidth+1.0);
		this.pane.setPrefWidth(paneWidth+1.0);
		this.pane.setMaxHeight(paneHeight+1.0);
		this.pane.setMinHeight(paneHeight+1.0);
		this.pane.setPrefHeight(paneHeight+1.0);
		this.canvas.setWidth(paneWidth);
		this.canvas.setHeight(paneHeight);
	}

	/**
	 * Initializes some fields of this class once an instance is instantiated from this class.
	 * @param mainController The main controller singleton instance.
	 */
	void initialize(MainController mainController) {
		this.mainController = mainController;
		this.pane = mainController.getPane();
		this.canvas = mainController.getCanvas();
	}
}