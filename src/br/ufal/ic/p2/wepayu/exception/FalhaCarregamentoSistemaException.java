package br.ufal.ic.p2.wepayu.exception;

public class FalhaCarregamentoSistemaException extends RuntimeException {

    public FalhaCarregamentoSistemaException(String arquivo, Throwable causa) {
        super("Falha ao carregar o estado do sistema de " + arquivo, causa);
    }
}
