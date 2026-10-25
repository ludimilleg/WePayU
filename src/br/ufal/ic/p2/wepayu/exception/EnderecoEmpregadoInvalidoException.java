package br.ufal.ic.p2.wepayu.exception;

public class EnderecoEmpregadoInvalidoException extends RuntimeException {

    public EnderecoEmpregadoInvalidoException() {
        super("Endereco do empregado nao pode ser nulo ou vazio.");
    }
}
