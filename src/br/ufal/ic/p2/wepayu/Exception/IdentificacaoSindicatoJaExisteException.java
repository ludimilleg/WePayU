package br.ufal.ic.p2.wepayu.Exception;

public class IdentificacaoSindicatoJaExisteException extends Exception {

    public IdentificacaoSindicatoJaExisteException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
