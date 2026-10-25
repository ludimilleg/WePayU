package br.ufal.ic.p2.wepayu.exception;

public class SalarioModeloInvalidoException extends RuntimeException {

    public SalarioModeloInvalidoException() {
        super("Salario deve ser finito e nao negativo.");
    }
}
