package my_app.screens.HomeScreen.components;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import my_app.marketplaces.Revendedor;
import my_app.screens.HomeScreen.HomeScreen.SimulacaoCallback;
import toolkit.Component;
import toolkit.declarative_components.Button_;
import toolkit.declarative_components.Column;
import toolkit.declarative_components.Row_;
import toolkit.declarative_components.Select_;
import toolkit.declarative_components.Spacer;
import toolkit.declarative_components.TextField_;
import toolkit.declarative_components.Text_;

public class MainSection extends Row_ {

    SimpleStringProperty nomeProperty = new SimpleStringProperty("");

    SimpleStringProperty quantidadeProperty = new SimpleStringProperty("");

    SimpleStringProperty valorProperty = new SimpleStringProperty("");

    ObservableList<Gasto> gastosFixosCard = FXCollections.observableArrayList();

    SimpleStringProperty precoVendaField = new SimpleStringProperty("");

    public MainSection(SimulacaoCallback callback) {

        render((self, modifier) -> {

            modifier.spacing(20);

            Left();

            new GastosFixosCard(gastosFixosCard);

            new Column((col_, md) -> {
                md.height(200);

                new Text_("Opções de Venda");
                new Select_<>(List.of("Estipular preço de venda", "Estipular margem de lucro"));

                new Text_("Preço de Venda");
                new TextField_("R$ 0,00", precoVendaField);

                new Button_("Fazer simulação", (btn_md) -> {
                    btn_md.fillMaxWidth(true)
                            .marginTop(20)
                            .onClick(() -> {
                                BigDecimal precoVenda = new BigDecimal(precoVendaField.getValue().replace(",", "."));

                                BigDecimal[] gastos = gastosFixosCard.stream()
                                        .map(g -> new BigDecimal(g.preco().replace(",", ".")))
                                        .toArray(BigDecimal[]::new);

                                Revendedor.Pedido pedido = new Revendedor.Pedido(
                                        nomeProperty.get(),
                                        Integer.valueOf(quantidadeProperty.get()),
                                        new BigDecimal(valorProperty.get()),
                                        Revendedor.Pedido.Categoria.RELOGIOS);// TODO mudar aqui depois para ser
                                                                              // dinamico, pegar categoria de um select

                                callback.executar(precoVenda, gastos, pedido);
                            })
                            .styles()
                            .textColor(Color.WHITE).bgColor(Color.web("#5B44BF"))
                            .borderRadius(10);
                });
            });

        });
    }

    @Component
    Column Left() {

        SimpleBooleanProperty preencheuCampos = new SimpleBooleanProperty(false);
        SimpleObjectProperty<BigDecimal> valorItemProperty = new SimpleObjectProperty<>(BigDecimal.ZERO);

        // Sempre atualiza o valorItem quando os campos mudam
        Runnable atualizarValorItem = () -> {
            try {
                if (valorProperty.get().isBlank() || quantidadeProperty.get().isBlank()) {
                    valorItemProperty.set(BigDecimal.ZERO);
                    return;
                }
                BigDecimal valorFormatado = new BigDecimal(valorProperty.get().trim().replace(",", "."));
                BigDecimal quantidadeFormatada = new BigDecimal(quantidadeProperty.get().trim());

                if (quantidadeFormatada.compareTo(BigDecimal.ZERO) == 0) {
                    valorItemProperty.set(BigDecimal.ZERO);
                    return;
                }
                preencheuCampos.set(true);
                valorItemProperty.set(valorFormatado.divide(quantidadeFormatada, 2, RoundingMode.HALF_UP));
            } catch (NumberFormatException ex) {
                valorItemProperty.set(BigDecimal.ZERO);
            }
        };

        quantidadeProperty.addListener((obs, oldVal, newVal) -> atualizarValorItem.run());
        valorProperty.addListener((obs, oldVal, newVal) -> atualizarValorItem.run());

        ObjectBinding<String> objectBinding = Bindings.createObjectBinding(() -> {
            atualizarValorItem.run();
            if (!preencheuCampos.get()) {
                return "Preencha quantidade e valor";
            }
            return "Cada item saiu por: " + valorItemProperty.get();
        }, quantidadeProperty, valorProperty);

        return new Column((modifier) -> {

            new Column((md) -> {
                md.styles().bgColor(Color.WHITE).borderRadius(10);

                md.padding(10).height(170);
                new Text_("Produtos para revenda");
                new Spacer(m -> m.height(10));
                new TextField_("Nome", nomeProperty);
                new TextField_("Quantidade", quantidadeProperty);
                new TextField_("R$", valorProperty);
            });

            new Text_("Detalhes");
            new Text_(objectBinding.get()).textProperty().bind(objectBinding);

        });
    }
}
