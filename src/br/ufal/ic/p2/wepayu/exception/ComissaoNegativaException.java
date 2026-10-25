package br.ufal.ic.p2.wepayu.exception;
public class ComissaoNegativaException extends Exception {

    public ComissaoNegativaException() {
        super("Comissao deve ser nao-negativa.");
    }
}