package br.ufal.ic.p2.wepayu.Exception;

public class CampoPersistenciaDesconhecidoException extends RuntimeException {

    public CampoPersistenciaDesconhecidoException(String campo) {
        super("Campo de persistencia desconhecido: " + campo);
    }
}
