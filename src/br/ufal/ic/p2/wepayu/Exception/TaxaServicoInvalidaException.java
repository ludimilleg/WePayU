package br.ufal.ic.p2.wepayu.Exception;

public class TaxaServicoInvalidaException extends RuntimeException {

    public TaxaServicoInvalidaException() {
        super("Taxa de servico deve ser finita e positiva.");
    }
}
