package br.ufal.ic.p2.wepayu.Exception;

public class ContaCorrenteModeloInvalidaException extends RuntimeException {

    public ContaCorrenteModeloInvalidaException() {
        super("Conta corrente nao pode ser nula ou vazia.");
    }
}
