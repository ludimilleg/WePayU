package br.ufal.ic.p2.wepayu.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatterBuilder; 
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Map;

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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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

        double salarioFixo = salarioMensal * 12 / 52 * 2;

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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
    public void salvarDadosExtras(Document doc, Element elemento) {
        Element comissaoElem = doc.createElement("comissao");
        comissaoElem.setTextContent(String.valueOf(comissao));
        elemento.appendChild(comissaoElem);

        Element vendasElem = doc.createElement("vendas");

        for (Map.Entry<String, Double> venda : vendas.entrySet()) {
            Element vendaElem = doc.createElement("venda");
            vendaElem.setAttribute("data", venda.getKey());
            vendaElem.setTextContent(String.valueOf(venda.getValue()));
            vendasElem.appendChild(vendaElem);
        }

        elemento.appendChild(vendasElem);
    }

    @Override
    public void carregarDadosExtras(Element elemento) {
        NodeList vendasNodes = elemento.getElementsByTagName("venda");

        for (int i = 0; i < vendasNodes.getLength(); i++) {
            Element vendaElem = (Element) vendasNodes.item(i);
            vendas.put(
                vendaElem.getAttribute("data"),
                Double.parseDouble(vendaElem.getTextContent())
            );
        }
    }

    @Override
    public Empregado copiar() {
        Comissionado copia = new Comissionado(id, nome, endereco, sindicalizado, salarioMensal, comissao);
        copiarCamposComuns(copia);
        copia.vendas = new HashMap<>(this.vendas);
        return copia;
    }
    }
