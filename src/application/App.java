package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class is responsible for invoking the main application, initializing its window to a certain dimensions
 * and loading the FXML files and instantiating its controllers' objects.
 */
public class App extends Application {

    /**
     * Initializes the application's stage to a certain dimensions, shows it and loads the fxml files.
     * @param primaryStage The stage where the FXML files' components will appear,
     * In other words the stage which will contains the application components itself.
     * @throws Exception An exception that will be thrown if any error occurs while loading the FXML files
     * or while showing the application window.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/designPaint.fxml"));
        primaryStage.setTitle("Paint");
        primaryStage.getIcons().add(new Image("file:resources/painter-palette.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    /**
     * Launches the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}