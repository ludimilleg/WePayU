package br.ufal.ic.p2.wepayu.models;

import java.util.HashMap;

public class Comissionado extends Assalariado {

    double comissao;

    public HashMap<String, Double> vendas;

    public Comissionado(
            int id,
            String nome,
            String endereco,
            boolean sindicalizado,
            double salarioMensal,
            double comissao) {

        super(id, nome, endereco, sindicalizado, salarioMensal);

        this.comissao = comissao;

        vendas = new HashMap<>();
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }

    @Override
    public double getComissao() {
        return comissao;
    }
}
