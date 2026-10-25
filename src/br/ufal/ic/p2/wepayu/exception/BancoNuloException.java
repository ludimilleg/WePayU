package br.ufal.ic.p2.wepayu.exception;

public class BancoNuloException extends Exception {

    public BancoNuloException() {
        super("Banco nao pode ser nulo.");
    }
}