package br.ufal.ic.p2.wepayu.exception;

public class ContaCorrenteNulaException extends Exception {

    public ContaCorrenteNulaException() {
        super("Conta corrente nao pode ser nulo.");
    }
}