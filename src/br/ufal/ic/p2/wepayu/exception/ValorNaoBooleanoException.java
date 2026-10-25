package br.ufal.ic.p2.wepayu.exception;

public class ValorNaoBooleanoException extends Exception {

    public ValorNaoBooleanoException() {
        super("Valor deve ser true ou false.");
    }
}