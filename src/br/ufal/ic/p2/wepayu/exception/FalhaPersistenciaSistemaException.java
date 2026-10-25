package br.ufal.ic.p2.wepayu.exception;

public class FalhaPersistenciaSistemaException extends RuntimeException {

    public FalhaPersistenciaSistemaException(String arquivo, Throwable causa) {
        super("Falha ao salvar o estado do sistema em " + arquivo, causa);
    }
}
