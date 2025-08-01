package my_app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import my_app.navigation.NavigationHandler;
import my_app.screens.HomeScreen.HomeScreen;
import toolkit.ResourcesDirectory;

public class App extends Application {

    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        NavigationHandler nav = new NavigationHandler(primaryStage);

        // Scene scene = new Scene(new TelaTeste(), 700, 500);
        // Scene scene = new Scene(nav.root, 700, 500);
        Scene scene = new Scene(new HomeScreen(primaryStage), 700, 500);
        setup(scene);

        this.primaryStage.show();
    }

    void setup(Scene scene) {
        // icon on window
        primaryStage.getIcons().add(new Image(
                new ResourcesDirectory().get("/assets/app_ico_window_32_32.png")));

        // registering font
        Font.loadFont(
                new ResourcesDirectory().get("/assets/fonts/Montserrat-Regular.ttf"), 14);

        // styles
        scene.getStylesheets().add(
                getClass().getResource("./styles.css")
                        .toExternalForm());

        this.primaryStage.setTitle("My Coesion-App");
        this.primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}