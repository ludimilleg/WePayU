package br.ufal.ic.p2.wepayu.exception;

public class MetodoPagamentoModeloInvalidoException extends RuntimeException {

    public MetodoPagamentoModeloInvalidoException() {
        super("Metodo de pagamento invalido no modelo do empregado.");
    }
}
