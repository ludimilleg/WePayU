package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.DataUtil;
import br.ufal.ic.p2.wepayu.exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.exception.ComissaoModeloInvalidaException;
import br.ufal.ic.p2.wepayu.exception.ValorVendaInvalidoException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

        setComissao(comissao);
        setAgendaPagamento(AgendaPagamento.QUINZENAL);

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
        if (!Double.isFinite(comissao) || comissao < 0) {
            throw new ComissaoModeloInvalidaException();
        }
        this.comissao = comissao;
    }

    @Override
    public Map<String, Double> getVendas() {
        return Collections.unmodifiableMap(vendas);
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {
        if (!recebeNaData(dataPagamento)) {
            return 0;
        }
        return getSalarioFixoNoPeriodo(dataPagamento)
                + somarVendas(AgendaPagamento.inicioPeriodo(getAgendaPagamento(), dataPagamento),
                        dataPagamento, true);
    }

    @Override
    public double getSalarioFixoNoPeriodo(LocalDate dataPagamento) {
        if (!recebeNaData(dataPagamento)) {
            return 0;
        }
        int semanas = AgendaPagamento.semanasNoPeriodo(getAgendaPagamento());
        if (semanas == 0) {
            return getSalario();
        }
        return Math.floor(getSalario() * 12 / 52 * semanas * 100) / 100;
    }

    @Override
    public double getVendasNoPeriodo(LocalDate dataPagamento) {
        if (!recebeNaData(dataPagamento)) {
            return 0;
        }
        return somarVendas(AgendaPagamento.inicioPeriodo(getAgendaPagamento(), dataPagamento),
                dataPagamento, false);
    }

    @Override
    public double getComissaoNoPeriodo(LocalDate dataPagamento) {
        if (!recebeNaData(dataPagamento)) {
            return 0;
        }
        return somarVendas(AgendaPagamento.inicioPeriodo(getAgendaPagamento(), dataPagamento),
                dataPagamento, true);
    }

    private double somarVendas(LocalDate inicio, LocalDate fim, boolean calcularComissao) {
        double total = 0;
        for (String data : vendas.keySet()) {
            LocalDate dataVenda = LocalDate.parse(data, DataUtil.FORMATO);
            if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(fim)) {
                double valor = vendas.get(data);
                total += calcularComissao ? valor * comissao : valor;
            }
        }
        return calcularComissao ? Math.floor(total * 100) / 100 : total;
    }

    @Override
    public void lancarVenda(String data, double valor)
            throws DataInvalidaException, ValorPositivoException {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new ValorPositivoException();
        }

        if (data == null) {
            throw new DataInvalidaException();
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
        setComissao(novaComissao);
    }

    @Override
    public Empregado copiar() {
        Comissionado copia = new Comissionado(
                getId(), getNome(), getEndereco(), false, getSalario(), comissao);
        copiarCamposComuns(copia);
        copia.vendas.putAll(vendas);
        return copia;
    }
}