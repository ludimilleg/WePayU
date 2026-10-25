package br.ufal.ic.p2.wepayu.exception;

public class IdentificadorEmpregadoInvalidoException extends RuntimeException {

    public IdentificadorEmpregadoInvalidoException() {
        super("Identificador do empregado deve ser positivo.");
    }
}
