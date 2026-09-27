package br.ufal.ic.p2.wepayu.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatterBuilder; 
import java.time.format.ResolverStyle;

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

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        LocalDate primeiroPagamento =
            LocalDate.of(2005, 1, 14);

        long dias = ChronoUnit.DAYS.between(
            primeiroPagamento,
            dataPagamento
        );

        if (dias < 0 || dias % 14 != 0) {
            return 0;
        }

        double salarioFixo =
            salarioMensal * 12 / 52 * 2;

        // Trunca para duas casas decimais
        salarioFixo =
            Math.floor(salarioFixo * 100) / 100;

        double totalComissao = 0;

        LocalDate inicio = dataPagamento.minusDays(13);

        for (String data : vendas.keySet()) {

            LocalDate dataVenda = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            if (!dataVenda.isBefore(inicio)
                    && !dataVenda.isAfter(dataPagamento)) {

                totalComissao +=
                    vendas.get(data) * comissao;
            }
        }

        totalComissao =
            Math.floor(totalComissao * 100) / 100;

        return salarioFixo + totalComissao;
    }

    public double getSalarioFixo(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 14);

        long dias = ChronoUnit.DAYS.between(primeiroPagamento, dataPagamento);

        if (dias < 0 || dias % 14 != 0) {
            return 0;
        }

        double salarioFixo = salarioMensal * 12 / 52 * 2;

        return Math.floor(salarioFixo * 100) / 100;
    }

    public double getVendasNoPeriodo(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 14);

        long dias = ChronoUnit.DAYS.between(primeiroPagamento, dataPagamento);

        if (dias < 0 || dias % 14 != 0) {
            return 0;
        }

        double total = 0;

        LocalDate inicio = dataPagamento.minusDays(13);

        for (String data : vendas.keySet()) {

            LocalDate dataVenda = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(dataPagamento)) {
                total += vendas.get(data);
            }
        }

        return total;
    }

    public double getComissaoNoPeriodo(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 14);

        long dias = ChronoUnit.DAYS.between(primeiroPagamento, dataPagamento);

        if (dias < 0 || dias % 14 != 0) {
            return 0;
        }

        double totalComissao = 0;

        LocalDate inicio = dataPagamento.minusDays(13);

        for (String data : vendas.keySet()) {

            LocalDate dataVenda = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(dataPagamento)) {
                totalComissao += vendas.get(data) * comissao;
            }
        }

        return Math.floor(totalComissao * 100) / 100;
    }
    @Override
    public Empregado copiar() {
        Comissionado copia = new Comissionado(id, nome, endereco, sindicalizado, salarioMensal, comissao);
        copiarCamposComuns(copia);
        copia.vendas = new HashMap<>(this.vendas);
        return copia;
    }
    }
