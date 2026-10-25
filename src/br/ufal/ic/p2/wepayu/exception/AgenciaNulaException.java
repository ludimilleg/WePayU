package br.ufal.ic.p2.wepayu.exception;

public class AgenciaNulaException extends Exception {

    public AgenciaNulaException() {
        super("Agencia nao pode ser nulo.");
    }
}