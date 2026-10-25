package br.ufal.ic.p2.wepayu.Exception;

public class BancoModeloInvalidoException extends RuntimeException {

    public BancoModeloInvalidoException() {
        super("Banco nao pode ser nulo ou vazio.");
    }
}
