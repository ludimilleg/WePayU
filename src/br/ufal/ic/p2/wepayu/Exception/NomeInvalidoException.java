package br.ufal.ic.p2.wepayu.Exception;
public class NomeInvalidoException extends Exception {

    public NomeInvalidoException() {
        super("Nome nao pode ser nulo.");
    }
}