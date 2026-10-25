package br.ufal.ic.p2.wepayu.exception;
public class NadaParaDesfazerException extends Exception {
    public NadaParaDesfazerException() {
        super("Nao ha comando a desfazer.");
    }
}