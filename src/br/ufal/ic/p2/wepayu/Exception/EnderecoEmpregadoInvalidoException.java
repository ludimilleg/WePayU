package br.ufal.ic.p2.wepayu.Exception;

public class EnderecoEmpregadoInvalidoException extends RuntimeException {

    public EnderecoEmpregadoInvalidoException() {
        super("Endereco do empregado nao pode ser nulo ou vazio.");
    }
}
