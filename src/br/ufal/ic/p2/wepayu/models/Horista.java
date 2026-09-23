package br.ufal.ic.p2.wepayu.models;

import java.util.HashMap;

public class Horista extends Empregado {

    double salarioHora;

    public HashMap<String, Double> cartoes;

    public Horista(
            int id,
            String nome,
            String endereco,
            boolean sindicalizado,
            double salarioHora) {

        super(id, nome, endereco, sindicalizado);

        this.salarioHora = salarioHora;

        cartoes = new HashMap<>();
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    @Override
    public double getSalario() {
        return salarioHora;
    }
}
