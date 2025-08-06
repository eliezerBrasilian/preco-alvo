package my_app.screens.HomeScreen.components;

import org.kordamp.ikonli.antdesignicons.AntDesignIconsOutlined;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import toolkit.declarative_components.*;

record Gasto(String title, String preco) {
}

public class GastosFixosCard extends Column {
    SimpleBooleanProperty expanded = new SimpleBooleanProperty(false);

    SimpleStringProperty nomeInput = new SimpleStringProperty("");
    SimpleStringProperty valorInput = new SimpleStringProperty("");

    public GastosFixosCard(ObservableList<Gasto> gastos) {
        render((col, mod) -> {
            mod.padding(15).width(230);
            mod.styles().bgColor(Color.WHITE).borderRadius(10);

            new Text_("Meus gastos fixos");
            new Spacer(m -> m.height(10));

            col.each(
                    gastos,
                    gasto -> new Row_((m) -> {
                        m.fillMaxWidth(true)
                                .spaceBetween();

                        new Text_(gasto.title());

                        new Row_(() -> {

                            new Button_(FontIcon.of(
                                    AntDesignIconsOutlined.EDIT,
                                    12), null);

                            new Button_((btn, btn_md) -> {
                                btn_md.onClick(() -> gastos.remove(gasto))
                                        .icon(FontIcon.of(
                                                AntDesignIconsOutlined.DELETE,
                                                12));
                            });
                        });
                    }),
                    () -> new Text_("Nenhum gasto adicionado"));

            new Button_("Criar +", (btn_md) -> {
                btn_md.onClick(() -> expanded.set(!expanded.get()))
                        .fillMaxWidth(true)
                        .styles().bgColor(Color.web("#1E331F"))
                        .textColor(Color.WHITE)
                        .borderRadius(10);
            });
            // col.when(condition).else(()->{})
            col.when(expanded, () -> new Column(column -> {
                new TextField_("Nome do gasto", nomeInput);
                new TextField_("R$ 0,00", valorInput);
                new Button_("Adicionar", () -> {
                    gastos.add(new Gasto(nomeInput.getValue(), valorInput.getValue()));
                    nomeInput.set("");
                    valorInput.set("");
                });
            }));

        });
    }
}