package br.ufal.ic.p2.wepayu.exception;

public class NomeEmpregadoInvalidoException extends RuntimeException {

    public NomeEmpregadoInvalidoException() {
        super("Nome do empregado nao pode ser nulo ou vazio.");
    }
}
