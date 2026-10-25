package br.ufal.ic.p2.wepayu.exception;
public class SalarioInvalidoException extends Exception {

    public SalarioInvalidoException() {
        super("Salario deve ser nao-negativo.");
    }
}