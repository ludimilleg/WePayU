package br.ufal.ic.p2.wepayu.exception;
public class ComissaoNulaException extends Exception {

    public ComissaoNulaException() {
        super("Comissao nao pode ser nula.");
    }
}