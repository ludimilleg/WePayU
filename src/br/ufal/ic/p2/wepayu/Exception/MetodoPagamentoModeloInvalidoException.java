package br.ufal.ic.p2.wepayu.Exception;

public class MetodoPagamentoModeloInvalidoException extends RuntimeException {

    public MetodoPagamentoModeloInvalidoException() {
        super("Metodo de pagamento invalido no modelo do empregado.");
    }
}
