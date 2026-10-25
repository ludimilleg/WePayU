package br.ufal.ic.p2.wepayu;

final class ConversorDecimal {

    private ConversorDecimal() {
    }

    static double converter(String valor) {
        if (valor == null) {
            throw new NumberFormatException("Valor decimal nulo.");
        }
        return Double.parseDouble(valor.replace(",", "."));
    }
}
