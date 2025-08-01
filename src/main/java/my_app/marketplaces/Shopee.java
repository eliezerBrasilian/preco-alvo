package my_app.marketplaces;

import java.math.BigDecimal;

import my_app.marketplaces.Revendedor.Pedido.Categoria;

public class Shopee extends Marketplace {
    public boolean fazParteProgramaDeAfiliados = false;

    public Shopee() {
        this.taxaDeComissao = 4;
        this.taxaPorItemVendido = 14 / 100f;
        this.taxaTransporte = 0;
    }

    @Override
    public BigDecimal preverComissao(
            BigDecimal total, Categoria categoria) {
        float porc = this.taxaPorItemVendido; // 14%

        // 14% do total
        BigDecimal comissao = total.multiply(BigDecimal.valueOf(porc));
        // 2,28
        BigDecimal taxaPorItem = BigDecimal.valueOf(4);
        BigDecimal valorTotalComissao = comissao.add(taxaPorItem);
        // 6.28
        return valorTotalComissao;
    }
}
