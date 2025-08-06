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
import toolkit.declarative_components.Li;
import toolkit.declarative_components.Row_;
import toolkit.declarative_components.Spacer;
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

        render((modf) -> {
            modf.padding(15);
            modf.styles().bgColor(Color.web("#D1D0C3"));

            new Text_("Area de simulação");
            new Text_(
                    "Você pode simular uma compra de itens para revenda e descobrir por quanto você poderia vender sem ficar no prejuizo.",
                    m -> {
                        m.marginTop(20);
                    });

            new Spacer(m -> m.height(10));
            // mesmo que (a,b,c) -> (a,b,c)
            new MainSection(this::handleFazerSimulacao);

            new Hr_((modifier) -> {
                modifier.marginTop(20);
            });

            new Spacer(m -> m.height(10));
            new Row_((mo) -> {
                mo.fillMaxWidth(true).spaceBetween();

                new Column(() -> {
                    new Text_("Resultado da simulação", m -> m.fontSize(20));
                    new Spacer(m -> m.height(10));
                    new Text_("Ao vender na Shopee:");

                    new Li(precoMinimoDeVendaShopeeBinding.get()).contentProperty()
                            .bind(precoMinimoDeVendaShopeeBinding);

                    new Li(lucroBrutoShopeeBinding.get()).contentProperty().bind(lucroBrutoShopeeBinding);

                    new Li(lucroLiquidoShopeeBinding.get()).contentProperty().bind(lucroLiquidoShopeeBinding);

                    new Li(valorParaReinvestirShopeeBinding.get()).contentProperty()
                            .bind(valorParaReinvestirShopeeBinding);
                });

                new Button_("Salvar Simulação", m -> m.styles()
                        .bgColor(Color.web("#374060"))
                        .textColor(Color.WHITE)
                        .borderRadius(10));

                // new Button_("Exportar Simulação");

            });
        });
    }
}
