package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.DataUtil;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.MetodoPagamentoModeloInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.BancoModeloInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.AgenciaModeloInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.ContaCorrenteModeloInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.NomeEmpregadoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.EnderecoEmpregadoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TaxaServicoInvalidaException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Empregado {
    private int id;
    private String nome;
    private String endereco;
    private boolean sindicalizado;
    private String idSindicato;
    private double taxaSindical;
    private HashMap<String, Double> taxasServico;
    private String metodoPagamento;
    private String banco;
    private String agencia;
    private String contaCorrente;
    private String agendaPagamento = AgendaPagamento.MENSAL;

    public Empregado(int id, String nome, String endereco, boolean sindicalizado) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.sindicalizado = sindicalizado;
        this.idSindicato = null;
        this.taxaSindical = 0;
        taxasServico = new HashMap<>();
        this.metodoPagamento = "emMaos";
        this.banco = null;
        this.agencia = null;
        this.contaCorrente = null;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public boolean isSindicalizado() {
        return sindicalizado;
    }

    public String getIdSindicato() {
        return idSindicato;
    }

    public int getId() {
        return id;
    }

    public double getTaxaSindical() {
        return taxaSindical;
    }

    public void setSindicalizado(boolean sindicalizado) {
        this.sindicalizado = sindicalizado;
        if (!sindicalizado) {
            idSindicato = null;
            taxaSindical = 0;
        }
    }

    public void setIdSindicato(String idSindicato) {
        if (idSindicato != null && idSindicato.isEmpty()) {
            throw new IdentificacaoSindicalInvalidaException();
        }
        this.idSindicato = idSindicato;
    }

    public void setTaxaSindical(double taxaSindical) {
        if (!Double.isFinite(taxaSindical) || taxaSindical < 0) {
            throw new TaxaSindicalInvalidaException();
        }
        this.taxaSindical = taxaSindical;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public String getAgendaPagamento() {
        return agendaPagamento;
    }

    public void setAgendaPagamento(String agendaPagamento) {
        this.agendaPagamento = AgendaPagamento.normalizar(agendaPagamento);
    }

    public String getBanco() {
        return banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }

    public abstract String getTipo();

    public Map<String, Double> getTaxasServico() {
        return Collections.unmodifiableMap(taxasServico);
    }

    public abstract double getSalario();

    public abstract void setSalario(double salario);

    public void setMetodoPagamento(String metodoPagamento) {
        if (!"emMaos".equals(metodoPagamento)
                && !"correios".equals(metodoPagamento)
                && !"banco".equals(metodoPagamento)) {
            throw new MetodoPagamentoModeloInvalidoException();
        }
        this.metodoPagamento = metodoPagamento;
    }

    public void setBanco(String banco) {
        if (banco == null || banco.isEmpty()) {
            throw new BancoModeloInvalidoException();
        }
        this.banco = banco;
    }

    public void setAgencia(String agencia) {
        if (agencia == null || agencia.isEmpty()) {
            throw new AgenciaModeloInvalidaException();
        }
        this.agencia = agencia;
    }

    public void setContaCorrente(String contaCorrente) {
        if (contaCorrente == null || contaCorrente.isEmpty()) {
            throw new ContaCorrenteModeloInvalidaException();
        }
        this.contaCorrente = contaCorrente;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new NomeEmpregadoInvalidoException();
        }
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isEmpty()) {
            throw new EnderecoEmpregadoInvalidoException();
        }
        this.endereco = endereco;
    }

    public abstract double calculaPagamento(LocalDate dataPagamento);

    public boolean recebeNaData(LocalDate dataPagamento) {
        return AgendaPagamento.dataDePagamento(agendaPagamento, dataPagamento);
    }

    public double getComissao() throws TipoNaoAplicavelException {
        throw new TipoNaoAplicavelException();
    }

    public double getHorasNormais(LocalDate dataPagamento) {
        return 0;
    }

    public double getHorasExtras(LocalDate dataPagamento) {
        return 0;
    }

    public double getDescontos(LocalDate dataPagamento) {

        double salarioBruto =
            calculaPagamento(dataPagamento);

        if (salarioBruto == 0) {
            return 0;
        }

        LocalDate dataInicio =
            LocalDate.of(2005, 1, 1);

        LocalDate dataPagamentoAnterior = null;

        LocalDate data =
            dataPagamento.minusDays(1);

        while (!data.isBefore(dataInicio)) {

            if (calculaPagamento(data) > 0) {
                dataPagamentoAnterior = data;
                break;
            }

            data = data.minusDays(1);
        }

        LocalDate primeiraData;

        if (dataPagamentoAnterior == null) {
            primeiraData = dataInicio;
        } else {
            primeiraData =
                dataPagamentoAnterior.plusDays(1);
        }

        double descontos = 0;

        // Taxa sindical
        if (sindicalizado) {

            long dias =
                ChronoUnit.DAYS.between(
                    primeiraData,
                    dataPagamento
                ) + 1;

            descontos += dias * taxaSindical;
        }

        // Taxas de servico
        for (String dataTaxa : taxasServico.keySet()) {

            LocalDate dataServico =
                LocalDate.parse(
                    dataTaxa,
                    DataUtil.FORMATO
                );

            if (!dataServico.isBefore(primeiraData)
                    && !dataServico.isAfter(dataPagamento)) {

                descontos += taxasServico.get(dataTaxa);
            }
        }

        return descontos;
    }

    public double getSalarioLiquido(LocalDate dataPagamento) {

        return calculaPagamento(dataPagamento)
                - getDescontos(dataPagamento);
    }

    public String getMetodoPagamentoFormatado() {

        if (metodoPagamento.equals("banco")) {

            return "Banco do Brasil, Ag. "
                    + agencia
                    + " CC "
                    + contaCorrente;
        }

        if (metodoPagamento.equals("correios")) {
            return "Correios, " + endereco;
        }

        return "Em maos";
    }

    public void copiarCamposComuns(Empregado copia) {
        copia.idSindicato = this.idSindicato;
        copia.taxaSindical = this.taxaSindical;
        copia.sindicalizado = this.sindicalizado;
        copia.nome = this.nome;
        copia.endereco = this.endereco;
        copia.metodoPagamento = this.metodoPagamento;
        copia.banco = this.banco;
        copia.agencia = this.agencia;
        copia.contaCorrente = this.contaCorrente;
        copia.agendaPagamento = this.agendaPagamento;
        copia.taxasServico = new HashMap<>(this.taxasServico);
    }

    public abstract Empregado copiar();

    public double getSalarioFixoNoPeriodo(LocalDate dataPagamento) {
        return 0;
    }

    public double getVendasNoPeriodo(LocalDate dataPagamento) {
        return 0;
    }

    public double getComissaoNoPeriodo(LocalDate dataPagamento) {
        return 0;
    }

    public void exportarDadosExtras(Exportador destino) {
        for (Map.Entry<String, Double> taxa : taxasServico.entrySet()) {
            destino.registro("taxaServico", taxa.getKey(), taxa.getValue());
        }
    }

    public void importarRegistro(String colecao, String data, double valor) {
        if (colecao.equals("taxaServico")) {
            if (!Double.isFinite(valor) || valor <= 0) {
                throw new TaxaServicoInvalidaException();
            }
            taxasServico.put(data, valor);
        }
    }

    public void lancarCartao(String data, double horas)
            throws EmpregadoNaoEhHoristaException, DataInvalidaException,
            HorasPositivasException {
        throw new EmpregadoNaoEhHoristaException();
    }

    public double getHorasNormaisTrabalhadas(LocalDate inicial, LocalDate dataFinal)
            throws EmpregadoNaoEhHoristaException {
        throw new EmpregadoNaoEhHoristaException();
    }

    public double getHorasExtrasTrabalhadas(LocalDate inicial, LocalDate dataFinal)
            throws EmpregadoNaoEhHoristaException {
        throw new EmpregadoNaoEhHoristaException();
    }

    public void lancarVenda(String data, double valor)
            throws EmpregadoNaoEhComissionadoException, DataInvalidaException,
            ValorPositivoException {
        throw new EmpregadoNaoEhComissionadoException();
    }

    public double getVendasRealizadasNoPeriodo(LocalDate inicial, LocalDate dataFinal)
            throws EmpregadoNaoEhComissionadoException {
        throw new EmpregadoNaoEhComissionadoException();
    }

    public void alterarComissao(double novaComissao)
            throws EmpregadoNaoEhComissionadoException {
        throw new EmpregadoNaoEhComissionadoException();
    }
}