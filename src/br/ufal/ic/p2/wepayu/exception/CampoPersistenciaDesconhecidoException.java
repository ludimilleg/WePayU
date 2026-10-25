package br.ufal.ic.p2.wepayu.exception;

public class CampoPersistenciaDesconhecidoException extends RuntimeException {

    public CampoPersistenciaDesconhecidoException(String campo) {
        super("Campo de persistencia desconhecido: " + campo);
    }
}
