package br.ufal.ic.p2.wepayu.Exception;

public class AgendaPagamentoInvalidaException extends RuntimeException {

    public AgendaPagamentoInvalidaException() {
        super("Agenda de pagamento invalida.");
    }
}
