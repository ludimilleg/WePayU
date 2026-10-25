package br.ufal.ic.p2.wepayu.Exception;

public class ColecaoPersistenciaDesconhecidaException extends RuntimeException {

    public ColecaoPersistenciaDesconhecidaException(String colecao) {
        super("Colecao de persistencia desconhecida: " + colecao);
    }
}
