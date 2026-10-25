package br.ufal.ic.p2.wepayu.exception;

public class AgendaPagamentoIndisponivelException extends Exception {

    public AgendaPagamentoIndisponivelException() {
        super("Agenda de pagamento nao esta disponivel");
    }
}
