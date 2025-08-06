package my_app.screens.LoginScreen;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;

import my_app.navigation.NavigationHandler;
import toolkit.declarative_components.Button_;
import toolkit.declarative_components.Column;
import toolkit.declarative_components.ImageView_;
import toolkit.declarative_components.Label_;
import toolkit.declarative_components.TextField_;
import toolkit.declarative_components.Text_;

public class LoginScreen extends Column {

    SimpleStringProperty chaveAcesso = new SimpleStringProperty("");

    public LoginScreen(Stage stage, NavigationHandler navigationHandler) {

        mountEffect(() -> {
            setupStage(stage);
        });

        render((modifier) -> {

            var maxWidth = 280;

            modifier.alignment(Pos.TOP_CENTER)
                    .padding(20)
                    .spacing(15)
                    .styles().bgColor(Color.WHITE);

            new ImageView_(
                    new Image(LoginScreen.class.getResourceAsStream("/assets/coesion-effect-logo.png")),
                    m -> {
                        m.size(100)
                                .animation(anim -> {
                                    anim.setDuration(Duration.millis(900));
                                    anim.setFromX(1.0);
                                    anim.setFromY(1.0);
                                    anim.setToX(1.4);
                                    anim.setToY(1.4);
                                    anim.setAutoReverse(true);
                                    anim.setCycleCount(2);
                                });
                    });

            new Text_("Seja bem vindo(a) ao Vendedor Superior, faça seu login abaixo.", m -> {
                m.alignment(TextAlignment.CENTER)
                        .maxWidth(maxWidth);
            });

            new Column(() -> {
                new Label_("Sua chave de acesso",
                        m -> m.fontSize(15)
                                .styles().color(Color.BLACK));
                var field = new TextField_();
                field.textProperty().bindBidirectional(chaveAcesso);
            });

            new Button_("Acessar", (md_btn) -> {
                md_btn.marginTop(15)
                        .fillMaxWidth(true)
                        .styles().bgColor(Color.web("1E8155"))
                        .textColor(Color.WHITE);
            });

            new Text_("v1.0", m -> {
                m.marginTop(30);
            });

        });

    }

    void setupStage(Stage stage) {
        stage.setTitle("Área de login - Bem vindo");
        stage.setWidth(390);
        stage.setResizable(false);
    }

}
