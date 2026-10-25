package br.ufal.ic.p2.wepayu.exception;
public class NomeInvalidoException extends Exception {

    public NomeInvalidoException() {
        super("Nome nao pode ser nulo.");
    }
}