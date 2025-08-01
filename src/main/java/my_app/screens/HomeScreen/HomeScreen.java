package my_app.screens.HomeScreen;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import toolkit.declarative_components.Button_;
import toolkit.declarative_components.Column;
import toolkit.declarative_components.Hr_;
import toolkit.declarative_components.Row_;
import toolkit.declarative_components.Text_;
import my_app.marketplaces.Marketplace;
import my_app.marketplaces.Revendedor;
import my_app.marketplaces.Shopee;
import my_app.screens.HomeScreen.components.MainSection;

public class HomeScreen extends Column {
    SimpleObjectProperty<BigDecimal> resultadoShopee = new SimpleObjectProperty<>(BigDecimal.ZERO);

    SimpleObjectProperty<BigDecimal> precoMinimoDeVendaShopeeState = new SimpleObjectProperty<>(BigDecimal.ZERO);

    SimpleObjectProperty<BigDecimal> lucroBrutoShopeeState = new SimpleObjectProperty<>(BigDecimal.ZERO);
    SimpleObjectProperty<BigDecimal> lucroLiquidoShopeeState = new SimpleObjectProperty<>(BigDecimal.ZERO);
    SimpleObjectProperty<BigDecimal> totalParaReinvestirState = new SimpleObjectProperty<>(BigDecimal.ZERO);

    @FunctionalInterface
    public interface SimulacaoCallback {
        void executar(BigDecimal precoVenda, BigDecimal[] gastosFixos, Revendedor.Pedido pedido);
    }

    SimulacaoCallback simulacaoCallback;

    void handleFazerSimulacao(BigDecimal precoVendaEscolhido, BigDecimal[] gastosFixos,
            Revendedor.Pedido pedido) {
        // calcular e colocar o resultado na state resultadoShopee
        System.out.println(precoVendaEscolhido);
        System.out.println(gastosFixos);

        Marketplace shopee = new Shopee();

        Revendedor.Gasto[] gastos = new Revendedor.Gasto[gastosFixos.length];

        for (int i = 0; i < gastosFixos.length; i++) {
            gastos[i] = new Revendedor.Gasto("indefinido", gastosFixos[i]);
        }

        Revendedor rev = new Revendedor(shopee);
        rev.pedido = pedido;
        rev.vincularGastosFixos(gastos);

        // preco de venda
        BigDecimal lucroBrutoFromPrecoDeVenda = rev.obterLucroBrutoFromPrecoDeVenda(precoVendaEscolhido);

        BigDecimal lucroLiquidoFromPrecoDeVenda = rev.obterLucroLiquido(lucroBrutoFromPrecoDeVenda);

        BigDecimal valorParaReinvestimentoFromPrecoDeVenda = rev
                .obterValorParaReinvestimento(lucroBrutoFromPrecoDeVenda);

        final var custoTotal = rev.obterPrecoMinimoDeVenda();

        System.out.println("Você deve vender o produto acima de %.2f".formatted(custoTotal));

        precoMinimoDeVendaShopeeState.set(custoTotal);

        lucroBrutoShopeeState.set(lucroBrutoFromPrecoDeVenda);
        lucroLiquidoShopeeState.set(lucroLiquidoFromPrecoDeVenda);
        totalParaReinvestirState.set(valorParaReinvestimentoFromPrecoDeVenda);
    };

    public HomeScreen(Stage stage) {
        mountEffect(() -> {
            stage.setResizable(true);
            stage.setWidth(1200);
            stage.setHeight(720);
        });

        DecimalFormat formato = (DecimalFormat) DecimalFormat.getInstance(new Locale("pt", "BR"));
        formato.applyPattern("#,##0.00");

        ObjectBinding<String> precoMinimoDeVendaShopeeBinding = Bindings.createObjectBinding(
                () -> "Para não ter prejuízo você deve vender o produto acima de "
                        + formato.format(precoMinimoDeVendaShopeeState.get()),
                lucroBrutoShopeeState, precoMinimoDeVendaShopeeState);

        ObjectBinding<String> lucroBrutoShopeeBinding = Bindings.createObjectBinding(
                () -> "Lucro bruto " + formato.format(lucroBrutoShopeeState.get()),
                lucroBrutoShopeeState);

        ObjectBinding<String> lucroLiquidoShopeeBinding = Bindings.createObjectBinding(
                () -> "Lucro líquido " + formato.format(lucroLiquidoShopeeState.get()),
                lucroLiquidoShopeeState);

        ObjectBinding<String> valorParaReinvestirShopeeBinding = Bindings.createObjectBinding(
                () -> "Valor para reinvestir " + formato.format(totalParaReinvestirState.get()),
                totalParaReinvestirState);

        render((col, modf) -> {
            modf.padding(15);
            modf.styles().bgColor(Color.web("#D1D0C3"));

            new Text_("Area de simulação");
            new Text_(
                    "Você pode simular uma compra de itens para revenda e descobrir por quanto você poderia vender sem ficar no prejuizo.",
                    m -> {
                        m.marginTop(20);
                    });

            // mesmo que (a,b,c) -> (a,b,c)
            new MainSection(this::handleFazerSimulacao);

            new Hr_((modifier) -> {
                modifier.marginTop(20);
            });

            new Row_((mo) -> {
                mo.fillMaxWidth(true).spaceBetween().marginTop(30);

                new Column((md) -> {
                    new Text_("Resultado da simulação");
                    new Text_("Ao vender na Shopee:");

                    new Text_(precoMinimoDeVendaShopeeBinding.get()).textProperty()
                            .bind(precoMinimoDeVendaShopeeBinding);

                    new Text_(lucroBrutoShopeeBinding.get()).textProperty().bind(lucroBrutoShopeeBinding);

                    new Text_(lucroLiquidoShopeeBinding.get()).textProperty().bind(lucroLiquidoShopeeBinding);

                    new Text_(valorParaReinvestirShopeeBinding.get()).textProperty()
                            .bind(valorParaReinvestirShopeeBinding);
                });

                new Button_("Salvar Simulação");

                // new Button_("Exportar Simulação");

            });
        });
    }
}
