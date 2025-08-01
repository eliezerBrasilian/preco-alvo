package my_app.navigation;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import my_app.screens.HomeScreen.HomeScreen;
import my_app.screens.LoginScreen.LoginScreen;

public class NavigationHandler {
    public final StackPane root = new StackPane();
    final Stage stage;

    public NavigationHandler(Stage stage) {
        this.stage = stage;
        login();
    }

    public void login() {
        root.getChildren().setAll(new LoginScreen(this.stage, this));
    }

    public void home() {
        root.getChildren().setAll(new HomeScreen(this.stage));
    }
}
