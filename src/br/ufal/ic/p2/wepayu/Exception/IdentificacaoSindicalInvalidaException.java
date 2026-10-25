package br.ufal.ic.p2.wepayu.Exception;

public class IdentificacaoSindicalInvalidaException extends RuntimeException {

    public IdentificacaoSindicalInvalidaException() {
        super("Identificacao sindical nao pode ser vazia.");
    }
}
