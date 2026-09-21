package br.ufal.ic.p2.wepayu.models;
public class Horista extends Empregado {
    double salarioHora;

    public Horista(int id, String nome, String endereço, boolean sindicalizado, double salarioHora){
        super(id, nome, endereço, sindicalizado);
        this.salarioHora = salarioHora;
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    public double getSalario() {
        return salarioHora; 
    }
}
