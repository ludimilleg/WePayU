package br.ufal.ic.p2.wepayu.exception;

public class ComissaoModeloInvalidaException extends RuntimeException {

    public ComissaoModeloInvalidaException() {
        super("Comissao deve ser finita e nao negativa.");
    }
}
