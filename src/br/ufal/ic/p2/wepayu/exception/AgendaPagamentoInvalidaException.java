package br.ufal.ic.p2.wepayu.exception;

public class AgendaPagamentoInvalidaException extends RuntimeException {

    public AgendaPagamentoInvalidaException() {
        super("Agenda de pagamento invalida.");
    }
}
