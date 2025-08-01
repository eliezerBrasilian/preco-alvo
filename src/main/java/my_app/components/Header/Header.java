package my_app.components.Header;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Header extends HBox {
    public Header() {
        getStylesheets().add(getClass().getResource("./style.css").toExternalForm());

        getStyleClass().add("container");
        // imagem a esquerda
        // columna a direita ( titulo e nome)

        ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream("/assets/images/mago.jpg")));
        imgView.setFitHeight(60);
        imgView.setFitWidth(60);
        // setClip
        imgView.setClip(new Circle(30, 30, 30));

        VBox columnRight = new VBox();
        columnRight.setAlignment(Pos.CENTER_LEFT);
        Text titulo = new Text("Portifolio");
        titulo.getStyleClass().add("titulo");

        Text nome = new Text("Eliezer Brasilian");
        nome.getStyleClass().add("nome-usuario");

        columnRight.getChildren().addAll(titulo, nome);

        getChildren().addAll(imgView, columnRight);
    }
}
