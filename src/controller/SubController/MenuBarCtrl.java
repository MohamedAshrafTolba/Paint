package controller.SubController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

//import java.io.File;

import controller.MainController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
//import javafx.stage.FileChooser;
//import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class MenuBarCtrl {

    /// Input from the user represents the desired width of the sheet
    @FXML
    private TextField canvasWidthId;

    /// input from the user represents the desired height of the sheet
    @FXML
    private TextField canvasHeightId;

    private Pane paneInp;
    private Pane outPane;
    private double widthLoadedPane;
    private double heightLoadedPane;

    /// A reference to the main controller
    private MainController mainCtrl;

    /// This method pass the dimensions of the canvas entered by user to the
    /// main controller to process this task
    @FXML
    private void newCanvasSize() {
	double width = Double.parseDouble(canvasWidthId.getText());
	double height = Double.parseDouble(canvasHeightId.getText());
	mainCtrl.invokeNewCanvas(width, height);

    }

    /// This method end the application
    @FXML
    private void closeApplication() {
	Platform.exit();
    }

    /// This method triggers the saving process which will direct the user
    /// toward two provided XML and JSON
    @FXML
    private void saveFile() throws IOException {
	FileChooser fileChooser = new FileChooser();

	// Set extension filter
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
	FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("JASON files (*.jason)", "*.jason");

	fileChooser.getExtensionFilters().addAll(extFilter, extFilter2);

	// Show save file dialog
	File path = fileChooser.showSaveDialog(null);
	if (path != null) {
	    String type = path.toString();
	    paneInp = mainCtrl.passPane();
	    if (type.endsWith(".xml")) {
		saveXML(path);
	    } else {
		saveJason(path);
	    }
	}
    }

    /// This method triggers the saving process with the format XML it takes the
    /// path to the required directory to save it.
    public void saveXML(File path) throws IOException {

	// xStream...
	XStream xStream = new XStream(new StaxDriver());

	SavePane s = new SavePane(generateArrayList());

	xStream.alias("Pane", SavePane.class);

	// Write to a file in the file system
	try {
	    FileOutputStream f = new FileOutputStream(path);
	    xStream.toXML(s, f);
	    f.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /// This method triggers the saving process with the format JSON it takes
    /// the path to the required directory to save it.
    public void saveJason(File path) throws IOException {
	XStream xStream = new XStream(new JettisonMappedXmlDriver());
	SavePane s = new SavePane(generateArrayList());
	xStream.alias("Pane", SavePane.class);

	// Write to a file in the file system
	try {
	    FileOutputStream f = new FileOutputStream(path);
	    xStream.toXML(s, f);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /// This method triggers the parsing process back on loading process and it
    /// takes list of strings which represents data as a parameter.
    public void drawString(List<String> x) {
	List<Double> data = new ArrayList<Double>();
	widthLoadedPane = Double.parseDouble(x.get(0));
	heightLoadedPane = Double.parseDouble(x.get(1));
	List<Node> remL = new LinkedList<Node>();
	for (Node child : paneInp.getChildren()) {
	    if (!(child instanceof Canvas)) {
		remL.add(child);
	    }
	}

	paneInp.getChildren().removeAll(remL);

	for (int k = 2; k < x.size(); k++) {
	    String my = x.get(k);
	    System.out.println(my);

	    // Extract Data...
	    data.clear();
	    Matcher m = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(my);
	    while (m.find()) {
		double value = Double.parseDouble(m.group());
		data.add(value);
	    }
	    System.out.println(data);

	    // COLORS...
	    String color1 = "";
	    String color2 = "";

	    // Extract Color...
	    if (my.indexOf("stroke=") != -1) {
		int startColor = my.indexOf("stroke=") + 7;
		int endColor = startColor + 10;
		color1 = my.substring(startColor, endColor);
	    }

	    // Extract Color2...
	    if (my.indexOf("fill=") != -1) {
		int startColor2 = my.indexOf("fill=") + 5;
		int endColor2 = startColor2 + 10;
		color2 = my.substring(startColor2, endColor2);
	    }

	    if (my.contains("Line")) {
		Line l = new Line(data.get(0), data.get(1), data.get(2), data.get(3));
		l.setStrokeWidth(data.get(data.size() - 1));
		l.setStroke(Color.web(color1));
		l.setFill(Color.web(color2));

		paneInp.getChildren().add(l);
	    } else if (my.contains("Polygon")) {
		Polygon p = new Polygon();
		p.getPoints().addAll(
			new Double[] { data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5) });
		p.setStrokeWidth(data.get(data.size() - 1));
		p.setStroke(Color.web(color1));
		p.setFill(Color.web(color2));
		paneInp.getChildren().add(p);
	    } else if (my.contains("Rectangle")) {
		Rectangle r = new Rectangle(data.get(0), data.get(1), data.get(2), data.get(3));
		r.setStrokeWidth(data.get(data.size() - 1));
		r.setStroke(Color.web(color1));
		r.setFill(Color.web(color2));
		paneInp.getChildren().add(r);
	    } else if (my.contains("Ellipse")) {
		Ellipse e = new Ellipse(data.get(0), data.get(1), data.get(2), data.get(3));
		e.setStrokeWidth(data.get(data.size() - 1));
		e.setStroke(Color.web(color1));
		e.setFill(Color.web(color2));
		paneInp.getChildren().add(e);
	    }

	}

	mainCtrl.adjustSheetAfterLoad(outPane, widthLoadedPane, heightLoadedPane);

    }

    public List<String> generateArrayList() {
	int index = -1;
	System.out.println(paneInp.getChildren().size());
	List<String> myPane = new ArrayList<String>();
	myPane.add(Double.toString(paneInp.getWidth()));
	myPane.add(Double.toString(paneInp.getHeight()));
	for (Node component : paneInp.getChildren()) {
	    index++;
	    if (component instanceof Line || component instanceof Polygon || component instanceof Rectangle
		    || component instanceof Ellipse) {
		myPane.add(paneInp.getChildren().get(index).toString());
	    }
	}
	return myPane;
    }

    @FXML
    private void loadFile() throws IOException {
	FileChooser fileChooser = new FileChooser();
	File path = fileChooser.showOpenDialog(null);
	paneInp = mainCtrl.passPane();
	if (path != null) {
	    // Show save file dialog
	    System.out.println(path);
	    String type = path.toString();
	    if (type.endsWith(".xml")) {
		System.out.println("xml");
		loadXML(path);
	    } else if (type.endsWith(".jason")) {
		loadJason(path);
	    }
	}
    }

    public void loadXML(File path) throws IOException {

	try {
	    FileReader fileReader = new FileReader(path);

	    XStream xStream = new XStream(new StaxDriver());

	    xStream.alias("Pane", SavePane.class);

	    SavePane p = (SavePane) xStream.fromXML(fileReader);

	    List<String> x = p.paneShapes;

	    System.out.println(x.get(0));
	    drawString(x);

	} catch (FileNotFoundException ex) {
	    System.out.println("e");
	}

    }

    public void loadJason(File path) {
	try {
	    FileReader fileReader = new FileReader(path);
	    XStream xStream = new XStream(new JettisonMappedXmlDriver());
	    xStream.alias("Pane", SavePane.class);
	    SavePane p = (SavePane) xStream.fromXML(fileReader);
	    System.out.println(p);
	    List<String> x = p.paneShapes;
	    System.out.println(x.get(0));
	    drawString(x);
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	}
    }

    // @FXML
    // private void addShapeClass() {
    // FileChooser fileChooser = new FileChooser();
    // fileChooser.getExtensionFilters().add(new ExtensionFilter("Class
    // files(*.class)", "*.class") );
    // fileChooser.setTitle("Add shapes");
    // canvasInp = mainCtrl.passCanvas();
    // File file = fileChooser.showOpenDialog(canvasInp.getScene().getWindow());
    // mainCtrl.getFileName(file);
    // }

    public void instantiate(MainController mainController) {
	mainCtrl = mainController;
    }

}
