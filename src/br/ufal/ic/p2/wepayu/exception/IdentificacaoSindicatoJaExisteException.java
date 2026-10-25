package br.ufal.ic.p2.wepayu.exception;

public class IdentificacaoSindicatoJaExisteException extends Exception {

    public IdentificacaoSindicatoJaExisteException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
