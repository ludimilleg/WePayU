package br.ufal.ic.p2.wepayu.Exception;

public class ValorVendaInvalidoException extends RuntimeException {

    public ValorVendaInvalidoException() {
        super("Valor da venda deve ser finito e positivo.");
    }
}
