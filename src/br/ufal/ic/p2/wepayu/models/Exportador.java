package br.ufal.ic.p2.wepayu.models;

public interface Exportador {

    void campo(String nome, String valor);

    void registro(String colecao, String data, double valor);
}