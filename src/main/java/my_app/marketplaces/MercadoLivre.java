package my_app.marketplaces;

import java.math.BigDecimal;
import my_app.marketplaces.Revendedor.Pedido.Categoria;

public class MercadoLivre extends Marketplace {
    public boolean anuncioClassico = true;
    public boolean anuncioPremium = false;

    @Override
    public BigDecimal preverComissao(
            BigDecimal valorItem,
            Categoria categoria) {

        final var tarifa = BigDecimal.valueOf(obterTarifaFromCategoria(categoria));
        final var custoFixo = BigDecimal.valueOf(obterCustoFixo(valorItem));

        return tarifa.multiply(valorItem).add(custoFixo);
    }

    public float obterTarifaFromCategoria(Categoria categoria) {
        if (anuncioPremium) {
            switch (categoria) {
                case ELETRONICOS:
                    return 0.17f;
                case RELOGIOS:
                    return 0.19f;
                default:
                    return 0.15f; // valor genérico para Premium
            }
        } else if (anuncioClassico) {
            switch (categoria) {
                case ELETRONICOS:
                    return 0.11f;
                case RELOGIOS:
                    return 0.14f;
                default:
                    return 0.10f; // valor genérico para Clássico
            }
        }
        return 0.05f; // padrão básico, talvez usado em outras modalidades
    }

    public float obterCustoFixo(BigDecimal precoDeVendaDoItem) {
        BigDecimalKing king = new BigDecimalKing(precoDeVendaDoItem);

        if (king.MaiorOuIgualQue(BigDecimal.valueOf(12.50)) &&
                king.MenorQue(BigDecimal.valueOf(29.00))) {
            return 6.25f;
        } else if (king.MaiorOuIgualQue(BigDecimal.valueOf(29.00)) &&
                king.MenorQue(BigDecimal.valueOf(50.00))) {
            return 6.50f;
        } else if (king.MaiorOuIgualQue(BigDecimal.valueOf(50.00)) &&
                king.MenorQue(BigDecimal.valueOf(79.00))) {
            return 6.75f;
        } else {
            return 0f;
        }
    }
}
