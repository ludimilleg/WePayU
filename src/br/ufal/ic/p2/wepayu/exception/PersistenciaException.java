package br.ufal.ic.p2.wepayu.exception;
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensagem) {
        super(mensagem);
    }

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}