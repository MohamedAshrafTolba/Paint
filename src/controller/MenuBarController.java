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
import model.Data;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuBarController {

	@FXML
	private TextField canvasWidth;

	@FXML
	private TextField canvasHeight;

	private Pane pane;

	private Canvas canvas;

	private MainController mainController;

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

	@FXML
	private void closeApplication() {
		Platform.exit();
	}

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

	private void saveXMLFile(File file) throws IOException {
		XStream xStream = new XStream(new StaxDriver());
		Data data = new Data(pane);
		xStream.alias("Pane", Data.class);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		xStream.toXML(data, fileOutputStream);
		fileOutputStream.close();
	}

	private void saveJSONFile(File file) throws IOException {
		XStream xStream = new XStream(new JettisonMappedXmlDriver());
		Data data = new Data(pane);
		xStream.alias("Pane", Data.class);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		xStream.toXML(data, fileOutputStream);
		fileOutputStream.close();
	}

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

	private void loadXMLFile(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		XStream xStream = new XStream(new StaxDriver());
		xStream.alias("Pane", Data.class);
		Data data = (Data) xStream.fromXML(fileReader);
		List<String> paneData = data.getPaneData();
		constructPane(paneData);
	}

	private void loadJSONFile(File file) throws IOException{
		FileReader fileReader = new FileReader(file);
		XStream xStream = new XStream(new JettisonMappedXmlDriver());
		xStream.alias("Pane", Data.class);
		Data data = (Data) xStream.fromXML(fileReader);
		List<String> paneData = data.getPaneData();
		constructPane(paneData);
	}
	
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
	void initialize(MainController mainController) {
		this.mainController = mainController;
		this.pane = mainController.getPane();
		this.canvas = mainController.getCanvas();
	}
}