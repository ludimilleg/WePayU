package br.ufal.ic.p2.wepayu.Exception;
public class SalarioInvalidoException extends Exception {

    public SalarioInvalidoException() {
        super("Salario deve ser nao-negativo.");
    }
}