package br.ufal.ic.p2.wepayu.exception;

public class ContaCorrenteModeloInvalidaException extends RuntimeException {

    public ContaCorrenteModeloInvalidaException() {
        super("Conta corrente nao pode ser nula ou vazia.");
    }
}
