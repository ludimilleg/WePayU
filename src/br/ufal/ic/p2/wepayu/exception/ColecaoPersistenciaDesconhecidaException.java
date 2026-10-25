package br.ufal.ic.p2.wepayu.exception;

public class ColecaoPersistenciaDesconhecidaException extends RuntimeException {

    public ColecaoPersistenciaDesconhecidaException(String colecao) {
        super("Colecao de persistencia desconhecida: " + colecao);
    }
}
