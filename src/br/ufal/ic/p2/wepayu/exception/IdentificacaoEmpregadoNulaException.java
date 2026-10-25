package br.ufal.ic.p2.wepayu.exception;

public class IdentificacaoEmpregadoNulaException extends Exception {

    public IdentificacaoEmpregadoNulaException() {
        super("Identificacao do empregado nao pode ser nula.");
    }
}