package br.ufal.ic.p2.wepayu.models;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Assalariado extends Empregado {
    private double salarioMensal;

    public Assalariado(int id, String nome, String endereço, boolean sindicalizado, double salarioMensal){
        super(id, nome, endereço, sindicalizado);
        setSalario(salarioMensal);
        setAgendaPagamento(AgendaPagamento.MENSAL);
    }

    public String getTipo(){
        return "assalariado";
    }

    public double getSalario() {
        return salarioMensal;
    }

    @Override
    public void setSalario(double salario) {
        if (!Double.isFinite(salario) || salario < 0) {
            throw new IllegalArgumentException("Salário deve ser não negativo.");
        }
        this.salarioMensal = salario;
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {
        if (!recebeNaData(dataPagamento)) {
            return 0;
        }

        int semanas = AgendaPagamento.semanasNoPeriodo(getAgendaPagamento());
        if (semanas == 0) {
            return salarioMensal;
        }
        return Math.floor(salarioMensal * 12 / 52 * semanas * 100) / 100;
    }

    @Override
    public Empregado copiar() {
        Assalariado copia = new Assalariado(
                getId(), getNome(), getEndereco(), isSindicalizado(), salarioMensal);
        copiarCamposComuns(copia);
        return copia;
    }   
}