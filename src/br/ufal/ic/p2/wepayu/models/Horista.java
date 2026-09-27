package br.ufal.ic.p2.wepayu.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.DayOfWeek;


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

    @Override
    public void setSalario(double salario) {
        this.salarioHora = salario;
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {
         if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
                return 0;
            }
        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            if (!dataCartao.isAfter(dataPagamento)
                    && !dataCartao.isBefore(dataPagamento.minusDays(6))) {

                double horas = cartoes.get(data);

                if (horas <= 8) {
                    total += horas * salarioHora;
                } else {
                    total += 8 * salarioHora;
                    total += (horas - 8) * salarioHora * 1.5;
                }
            }
        }

        return total;
    }

    public double getHorasNormais(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            if (!dataCartao.isAfter(dataPagamento)
                    && !dataCartao.isBefore(dataPagamento.minusDays(6))) {

                double horas = cartoes.get(data);

                if (horas <= 8) {
                    total += horas;
                } else {
                    total += 8;
                }
            }
        }

        return total;
    }

    public double getHorasExtras(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            if (!dataCartao.isAfter(dataPagamento)
                    && !dataCartao.isBefore(dataPagamento.minusDays(6))) {

                double horas = cartoes.get(data);

                if (horas > 8) {
                    total += horas - 8;
                }
            }
        }

        return total;
    }

    @Override
    public Empregado copiar() {
        Horista copia = new Horista(id, nome, endereco, sindicalizado, salarioHora);
        copiarCamposComuns(copia);
        copia.cartoes = new HashMap<>(this.cartoes);
        return copia;
    }
}
