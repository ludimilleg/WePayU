package br.ufal.ic.p2.wepayu.exception;

public class ValorPositivoException extends Exception {

    public ValorPositivoException() {
        super("Valor deve ser positivo.");
    }
}
