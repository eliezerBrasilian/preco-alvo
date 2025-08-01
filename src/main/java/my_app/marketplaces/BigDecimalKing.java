package my_app.marketplaces;

import java.math.BigDecimal;

public class BigDecimalKing {
    private final BigDecimal value;

    public BigDecimalKing(BigDecimal value) {
        this.value = value;
    }

    public boolean MaiorOuIgualQue(BigDecimal other) {
        return value.compareTo(other) >= 0;
    }

    public boolean MenorOuIgualQue(BigDecimal other) {
        return value.compareTo(other) <= 0;
    }

    public boolean MenorQue(BigDecimal other) {
        return value.compareTo(other) < 0;
    }

    public boolean MaiorQue(BigDecimal other) {
        return value.compareTo(other) > 0;
    }

    public boolean IgualA(BigDecimal other) {
        return value.compareTo(other) == 0;
    }
}