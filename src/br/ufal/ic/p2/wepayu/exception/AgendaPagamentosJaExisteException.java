package br.ufal.ic.p2.wepayu.exception;

public class AgendaPagamentosJaExisteException extends Exception {

    public AgendaPagamentosJaExisteException() {
        super("Agenda de pagamentos ja existe");
    }
}
