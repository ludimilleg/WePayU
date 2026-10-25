package br.ufal.ic.p2.wepayu.exception;

public class EmpregadoNaoEhComissionadoException extends Exception {

    public EmpregadoNaoEhComissionadoException() {
        super("Empregado nao eh comissionado.");
    }
}
