package br.ufal.ic.p2.wepayu.exception;

public class TaxaServicoInvalidaException extends RuntimeException {

    public TaxaServicoInvalidaException() {
        super("Taxa de servico deve ser finita e positiva.");
    }
}
