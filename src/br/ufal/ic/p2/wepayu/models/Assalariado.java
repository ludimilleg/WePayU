package br.ufal.ic.p2.wepayu.models;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Assalariado extends Empregado {
    double salarioMensal;

    public Assalariado(int id, String nome, String endereço, boolean sindicalizado, double salarioMensal){
        super(id, nome, endereço, sindicalizado);
        this.salarioMensal = salarioMensal;
    }

    public String getTipo(){
        return "assalariado";
    }

    public double getSalario() {
        return salarioMensal;
    }

    @Override
    public void setSalario(double salario) {
        this.salarioMensal = salario;
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {

        LocalDate ultimoDia =
            dataPagamento.with(
                TemporalAdjusters.lastDayOfMonth()
            );

        while (ultimoDia.getDayOfWeek() == DayOfWeek.SATURDAY
                || ultimoDia.getDayOfWeek() == DayOfWeek.SUNDAY) {

            ultimoDia = ultimoDia.minusDays(1);
        }

        if (!dataPagamento.equals(ultimoDia)) {
            return 0;
        }

        return salarioMensal;
    }

    @Override
    public Empregado copiar() {
        Assalariado copia = new Assalariado(id, nome, endereco, sindicalizado, salarioMensal);
        copiarCamposComuns(copia);
        return copia;
    }   
}