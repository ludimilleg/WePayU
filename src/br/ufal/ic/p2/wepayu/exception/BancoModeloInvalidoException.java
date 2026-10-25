package br.ufal.ic.p2.wepayu.exception;

public class BancoModeloInvalidoException extends RuntimeException {

    public BancoModeloInvalidoException() {
        super("Banco nao pode ser nulo ou vazio.");
    }
}
