package br.ufal.ic.p2.wepayu.models;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;

import java.time.LocalDate;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public abstract class Empregado{
    int id;
    String nome;
    String endereco;
    boolean sindicalizado;
    String idSindicato;
    double taxaSindical;
    HashMap<String, Double> taxasServico;
    String metodoPagamento;
    String banco;
    String agencia;
    String contaCorrente;

    public Empregado(int id, String nome, String endereco, boolean sindicalizado){
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
    }

    public void setIdSindicato(String idSindicato) {
        this.idSindicato = idSindicato;
    }

    public void setTaxaSindical(double taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    public String getMetodoPagamento() {
    return metodoPagamento;
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

    public HashMap<String, Double> getTaxasServico() {
        return taxasServico;
    }

     public abstract double getSalario();

    public abstract void setSalario(double salario);


     public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public abstract double calculaPagamento(LocalDate dataPagamento);
    
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

        // Taxas de serviço
        for (String dataTaxa : taxasServico.keySet()) {

            LocalDate dataServico =
                LocalDate.parse(
                    dataTaxa,
                    new DateTimeFormatterBuilder()
                        .appendPattern("d/M/uuuu")
                        .toFormatter()
                        .withResolverStyle(ResolverStyle.STRICT)
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

    protected void copiarCamposComuns(Empregado copia) {
        copia.idSindicato = this.idSindicato;
        copia.taxaSindical = this.taxaSindical;
        copia.sindicalizado = this.sindicalizado;
        copia.nome = this.nome;
        copia.endereco = this.endereco;
        copia.metodoPagamento = this.metodoPagamento;
        copia.banco = this.banco;
        copia.agencia = this.agencia;
        copia.contaCorrente = this.contaCorrente;
        copia.taxasServico = new HashMap<>(this.taxasServico);
    }

    public abstract Empregado copiar();

}