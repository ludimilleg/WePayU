package br.ufal.ic.p2.wepayu.exception;
public class SalarioNuloException extends Exception {

    public SalarioNuloException() {
        super("Salario nao pode ser nulo.");
    }
}