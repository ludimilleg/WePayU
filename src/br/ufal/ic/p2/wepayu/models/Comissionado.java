package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.DataUtil;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Comissionado extends Assalariado {

    private double comissao;

    private final HashMap<String, Double> vendas;

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

    public Map<String, Double> getVendas() {
        return Collections.unmodifiableMap(vendas);
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
            getSalario() * 12 / 52 * 2;

        // Trunca para duas casas decimais
        salarioFixo =
            Math.floor(salarioFixo * 100) / 100;

        double totalComissao = 0;

        LocalDate inicio = dataPagamento.minusDays(13);

        for (String data : vendas.keySet()) {

            LocalDate dataVenda = LocalDate.parse(
                data,
                DataUtil.FORMATO
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

    @Override
    public double getSalarioFixoNoPeriodo(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        LocalDate primeiroPagamento = LocalDate.of(2005, 1, 14);

        long dias = ChronoUnit.DAYS.between(primeiroPagamento, dataPagamento);

        if (dias < 0 || dias % 14 != 0) {
            return 0;
        }

        double salarioFixo = getSalario() * 12 / 52 * 2;

        return Math.floor(salarioFixo * 100) / 100;
    }

    @Override
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
                DataUtil.FORMATO
            );

            if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(dataPagamento)) {
                total += vendas.get(data);
            }
        }

        return total;
    }

    @Override
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
                DataUtil.FORMATO
            );

            if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(dataPagamento)) {
                totalComissao += vendas.get(data) * comissao;
            }
        }

        return Math.floor(totalComissao * 100) / 100;
    }

    @Override
    public void lancarVenda(String data, double valor)
            throws DataInvalidaException, ValorPositivoException {
        if (valor <= 0) {
            throw new ValorPositivoException();
        }

        try {
            LocalDate.parse(
                data,
                DataUtil.FORMATO
            );
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        vendas.put(data, valor);
    }

    @Override
    public double getVendasRealizadasNoPeriodo(LocalDate inicial, LocalDate dataFinal) {
        double total = 0;

        for (String data : vendas.keySet()) {
            LocalDate dataVenda = LocalDate.parse(
                data,
                DataUtil.FORMATO
            );

            if (!dataVenda.isBefore(inicial) && dataVenda.isBefore(dataFinal)) {
                total += vendas.get(data);
            }
        }

        return total;
    }

    @Override
    public void alterarComissao(double novaComissao) {
        this.comissao = novaComissao;
    }

    @Override
    public void exportarDadosExtras(Exportador destino) {
        super.exportarDadosExtras(destino);

        destino.campo("comissao", String.valueOf(comissao));

        for (Map.Entry<String, Double> venda : vendas.entrySet()) {
            destino.registro("venda", venda.getKey(), venda.getValue());
        }
    }

    @Override
    public void importarRegistro(String colecao, String data, double valor) {
        if (colecao.equals("venda")) {
            vendas.put(data, valor);
            return;
        }

        super.importarRegistro(colecao, data, valor);
    }

    @Override
    public Empregado copiar() {
        Comissionado copia = new Comissionado(
                getId(), getNome(), getEndereco(), isSindicalizado(), getSalario(), comissao);
        copiarCamposComuns(copia);
        copia.vendas.putAll(vendas);
        return copia;
    }
}