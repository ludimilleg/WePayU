package br.ufal.ic.p2.wepayu.Exception;

public class HorasCartaoInvalidasException extends RuntimeException {

    public HorasCartaoInvalidasException() {
        super("Horas do cartao devem ser finitas e positivas.");
    }
}
