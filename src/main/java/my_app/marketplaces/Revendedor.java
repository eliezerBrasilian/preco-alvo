package my_app.marketplaces;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// import marketplaces.Marketplace;
// import marketplaces.Shopee;

public class Revendedor {
    private final Marketplace marketplace;
    public Pedido pedido;
    private List<Gasto> gastosFixos;
    public float percentualReinvestimento = 70;

    public Revendedor(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    public void vincularGastosFixos(Gasto[] gastos) {
        if (gastosFixos == null) {
            this.gastosFixos = new ArrayList<>();
        }

        for (Gasto gasto : gastos) {
            this.gastosFixos.add(gasto);
        }
    }

    public BigDecimal obterPrecoMinimoDeVenda() {
        // obter o valor minimo que posso vender a unidade e então aplicar a comissão da
        // shopee
        // comissão da shopee = 14% + R$ 4,00 por item vendido

        BigDecimal custoTotalSemComissaoDoMarketplace = obterCustoTotal();

        return custoTotalSemComissaoDoMarketplace
                .add(marketplace.preverComissao(custoTotalSemComissaoDoMarketplace, pedido.categoria));

    }

    public BigDecimal obterValorParaReinvestimento(BigDecimal lucroBruto) {
        BigDecimal percentual = new BigDecimal(this.percentualReinvestimento / 100f);
        BigDecimal valorDeReinvestimento = lucroBruto.multiply(percentual);

        return valorDeReinvestimento;
    }

    public BigDecimal obterLucroLiquido(BigDecimal lucroBruto) {
        BigDecimal percentual = new BigDecimal(this.percentualReinvestimento / 100f);
        BigDecimal valorDeReinvestimento = lucroBruto.multiply(percentual);

        return lucroBruto.subtract(valorDeReinvestimento);
    }

    public BigDecimal obterPrecoDeVendaFromMargem(float margemLucro) {
        BigDecimal custoTotal = obterCustoTotal();

        // 1 + acresenta o acrescimo
        BigDecimal vendaSemComissao = custoTotal.multiply(BigDecimal.valueOf(1 + margemLucro / 100));

        BigDecimal vendaFinal = vendaSemComissao.add(marketplace.preverComissao(vendaSemComissao, pedido.categoria));

        return vendaFinal;

    }

    public BigDecimal obterLucroBrutoFromMargem(float margemLucro) {
        BigDecimal custoTotal = obterCustoTotal();

        // 1 + acresenta o acrescimo
        BigDecimal vendaSemComissao = custoTotal.multiply(BigDecimal.valueOf(1 + margemLucro / 100));

        BigDecimal vendaFinal = vendaSemComissao.add(marketplace.preverComissao(vendaSemComissao, pedido.categoria));

        BigDecimal lucroBruto = vendaFinal.subtract(custoTotal)
                .subtract(marketplace.preverComissao(vendaFinal, pedido.categoria));

        return lucroBruto;

    }

    public BigDecimal obterLucroBrutoFromPrecoDeVenda(
            BigDecimal precoVendaEscolhido) {

        BigDecimal custoTotal = obterCustoTotal();
        BigDecimal comissao = marketplace.preverComissao(
                precoVendaEscolhido, pedido.categoria);

        BigDecimal lucroBruto = precoVendaEscolhido.subtract(custoTotal).subtract(comissao);

        return lucroBruto;
    }

    public BigDecimal obterCustoTotal() {
        // adiciono os gastos fixos para fazer a revenda na shopee
        // quanto saiu cada item no aliexpress
        BigDecimal valorItem = pedido.quantoSaiuCadaItem();
        BigDecimal result = valorItem.add(BigDecimal.ZERO);

        for (Gasto gasto : gastosFixos) {
            result = result.add(gasto.valor());
        }
        return result;

    }

    public record Gasto(
            String nome,
            BigDecimal valor) {
    }

    public record Pedido(
            String nome,
            int quantidade,
            BigDecimal total,
            Categoria categoria) {

        public enum Categoria {
            ELETRONICOS,
            RELOGIOS,
            OUTROS
        }

        public BigDecimal quantoSaiuCadaItem() {
            BigDecimal valorCadaItem = total.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
            return valorCadaItem;
        }
    }

}

class InnerRevendedor {

    public static void main(String[] args) {

    }

    public static void testShopee() {
        Marketplace shopee = new Shopee();

        Revendedor rev = new Revendedor(shopee);
        Revendedor.Pedido pedido = new Revendedor.Pedido("Relogios",
                10,
                new BigDecimal(132.96),
                Revendedor.Pedido.Categoria.RELOGIOS);

        Revendedor.Gasto[] gastos = {
                new Revendedor.Gasto("Embalagem", BigDecimal.ONE)
        };

        //
        final var precoVendaEscolhido = new BigDecimal(97.99);
        final var margemLucro = 70f;

        rev.pedido = pedido;
        rev.vincularGastosFixos(gastos);

        BigDecimal lucroBrutoFromPrecoDeVenda = rev.obterLucroBrutoFromPrecoDeVenda(precoVendaEscolhido);

        BigDecimal lucroLiquidoFromPrecoDeVenda = rev.obterLucroLiquido(lucroBrutoFromPrecoDeVenda);

        BigDecimal valorParaReinvestimentoFromPrecoDeVenda = rev
                .obterValorParaReinvestimento(lucroBrutoFromPrecoDeVenda);

        // ------------------------

        BigDecimal lucroBrutoFromMargem = rev.obterLucroBrutoFromMargem(margemLucro);

        BigDecimal precoVendaFromMargem = rev.obterPrecoDeVendaFromMargem(70);

        final var custoTotal = rev.obterPrecoMinimoDeVenda();

        BigDecimal valorParaReinvestimentoFromMargem = rev.obterValorParaReinvestimento(lucroBrutoFromMargem);

        BigDecimal lucroLiquidoFromMargem = rev.obterLucroLiquido(lucroBrutoFromMargem);

        System.out.println("Você deve vender o produto acima de %.2f".formatted(custoTotal));

        System.out.println("-------------------");
        System.out.println("--------SHOPEE----------");
        System.out
                .println(
                        "(PRECO DE VENDA) Vendendo o produto a %.2f\n\tLucro bruto: %.2f\n\tTotal para reinvestir: R$ %.2f\n\tLucro liquido é de: %.2f"
                                .formatted(precoVendaEscolhido,
                                        lucroBrutoFromPrecoDeVenda,
                                        valorParaReinvestimentoFromPrecoDeVenda,
                                        lucroLiquidoFromPrecoDeVenda));

        System.out.println(
                "(MARGEM) Preço de venda gerado: %.2f\n\tMargem de: %.2f\n\tLucro bruto: %.2f\n\tTotal para reinvestir: R$ %.2f\n\tLucro liquido é de: %.2f"
                        .formatted(
                                precoVendaFromMargem,
                                margemLucro,
                                lucroBrutoFromMargem,
                                valorParaReinvestimentoFromMargem,
                                lucroLiquidoFromMargem));
    }
}