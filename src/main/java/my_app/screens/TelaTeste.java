package my_app.screens;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import toolkit.declarative_components.Column;
import toolkit.declarative_components.FXNodeContext;

class Button_ extends Button {
    public Button_(String value) {
        super(value);
        FXNodeContext.add(this);
    }
}

class Text_ extends Text {
    public Text_(String value) {
        super(value);
        FXNodeContext.add(this);
    }
}

public class TelaTeste extends Column {

    public TelaTeste() {
        super(col -> {
            new Text_("Testando");

            new Button_("clique");

            new Column(col2 -> {
                new Button_("clique2");
            });
        });

    }

}
