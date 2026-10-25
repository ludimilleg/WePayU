package br.ufal.ic.p2.wepayu.exception;

public class TaxaSindicalNegativaException extends Exception {

    public TaxaSindicalNegativaException() {
        super("Taxa sindical deve ser nao-negativa.");
    }
}