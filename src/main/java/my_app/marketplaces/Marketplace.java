package my_app.marketplaces;

import java.math.BigDecimal;
import my_app.marketplaces.Revendedor.Pedido.Categoria;

public abstract class Marketplace {
    public int taxaDeComissao;// 14% na shopee
    public float taxaPorItemVendido; // 4 reais na shopee
    // parece ser cobrado so na shopee, então mover pra lá, se necessario
    public int taxaTransporte;// 6 reais na shopee se fizer parte do programa de frete gratos

    abstract public BigDecimal preverComissao(
            // totalCalculadoAteOMomento
            BigDecimal total,
            Categoria categoriaDoPedido);
}
