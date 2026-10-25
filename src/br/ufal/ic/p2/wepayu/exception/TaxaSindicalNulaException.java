package br.ufal.ic.p2.wepayu.exception;

public class TaxaSindicalNulaException extends Exception {

    public TaxaSindicalNulaException() {
        super("Taxa sindical nao pode ser nula.");
    }
}