package br.ufal.ic.p2.wepayu.Exception;

public class AgenciaModeloInvalidaException extends RuntimeException {

    public AgenciaModeloInvalidaException() {
        super("Agencia nao pode ser nula ou vazia.");
    }
}
