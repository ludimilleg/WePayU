package br.ufal.ic.p2.wepayu.exception;

public class TaxaSindicalInvalidaException extends RuntimeException {

    public TaxaSindicalInvalidaException() {
        super("Taxa sindical deve ser finita e nao negativa.");
    }
}
