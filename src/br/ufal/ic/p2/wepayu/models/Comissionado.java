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

    private static final LocalDate PRIMEIRO_PAGAMENTO = LocalDate.of(2005, 1, 14);

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
            throw new IllegalArgumentException("Comissão deve ser não negativa.");
        }
        this.comissao = comissao;
    }

    public Map<String, Double> getVendas() {
        return Collections.unmodifiableMap(vendas);
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {
        LocalDate inicio = inicioPeriodoPagamento(dataPagamento);
        if (inicio == null) {
            return 0;
        }
        return salarioFixoQuinzenal() + somarVendas(inicio, dataPagamento, true);
    }

    @Override
    public double getSalarioFixoNoPeriodo(LocalDate dataPagamento) {
        if (inicioPeriodoPagamento(dataPagamento) == null) {
            return 0;
        }
        return salarioFixoQuinzenal();
    }

    @Override
    public double getVendasNoPeriodo(LocalDate dataPagamento) {
        LocalDate inicio = inicioPeriodoPagamento(dataPagamento);
        if (inicio == null) {
            return 0;
        }
        return somarVendas(inicio, dataPagamento, false);
    }

    @Override
    public double getComissaoNoPeriodo(LocalDate dataPagamento) {
        LocalDate inicio = inicioPeriodoPagamento(dataPagamento);
        if (inicio == null) {
            return 0;
        }
        return somarVendas(inicio, dataPagamento, true);
    }

    private LocalDate inicioPeriodoPagamento(LocalDate dataPagamento) {
        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return null;
        }
        long dias = ChronoUnit.DAYS.between(PRIMEIRO_PAGAMENTO, dataPagamento);
        if (dias < 0 || dias % 14 != 0) {
            return null;
        }
        return dataPagamento.minusDays(13);
    }

    private double salarioFixoQuinzenal() {
        double salarioFixo = getSalario() * 12 / 52 * 2;
        return Math.floor(salarioFixo * 100) / 100;
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
            if (!Double.isFinite(valor) || valor <= 0) {
                throw new IllegalArgumentException("Valor da venda deve ser positivo.");
            }
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