package br.ufal.ic.p2.wepayu.Exception;

public class TaxaSindicalInvalidaException extends RuntimeException {

    public TaxaSindicalInvalidaException() {
        super("Taxa sindical deve ser finita e nao negativa.");
    }
}
