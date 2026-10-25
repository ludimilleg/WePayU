package br.ufal.ic.p2.wepayu.Exception;

public class ComissaoModeloInvalidaException extends RuntimeException {

    public ComissaoModeloInvalidaException() {
        super("Comissao deve ser finita e nao negativa.");
    }
}
