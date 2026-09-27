package br.ufal.ic.p2.wepayu.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.HorasPositivasException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.Map;


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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
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
    public void lancarCartao(String data, double horas)
            throws DataInvalidaException, HorasPositivasException {
        if (horas <= 0) {
            throw new HorasPositivasException();
        }

        try {
            LocalDate.parse(
                data,
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
            );
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        cartoes.put(data, horas);
    }

    @Override
    public double getHorasNormaisTrabalhadas(LocalDate inicial, LocalDate dataFinal) {
        double total = 0;

        for (String data : cartoes.keySet()) {
            LocalDate dataCartao = LocalDate.parse(
                data,
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
            );

            if (!dataCartao.isBefore(inicial) && dataCartao.isBefore(dataFinal)) {
                double horas = cartoes.get(data);
                total += horas <= 8 ? horas : 8;
            }
        }

        return total;
    }

    @Override
    public double getHorasExtrasTrabalhadas(LocalDate inicial, LocalDate dataFinal) {
        double total = 0;

        for (String data : cartoes.keySet()) {
            LocalDate dataCartao = LocalDate.parse(
                data,
                br.ufal.ic.p2.wepayu.DataUtil.FORMATO
            );

            if (!dataCartao.isBefore(inicial) && dataCartao.isBefore(dataFinal)) {
                double horas = cartoes.get(data);
                if (horas > 8) {
                    total += horas - 8;
                }
            }
        }

        return total;
    }

    @Override
    public void salvarDadosExtras(Document doc, Element elemento) {
        Element cartoesElem = doc.createElement("cartoes");

        for (Map.Entry<String, Double> cartao : cartoes.entrySet()) {
            Element cartaoElem = doc.createElement("cartao");
            cartaoElem.setAttribute("data", cartao.getKey());
            cartaoElem.setTextContent(String.valueOf(cartao.getValue()));
            cartoesElem.appendChild(cartaoElem);
        }

        elemento.appendChild(cartoesElem);
    }

    @Override
    public void carregarDadosExtras(Element elemento) {
        NodeList cartoesNodes = elemento.getElementsByTagName("cartao");

        for (int i = 0; i < cartoesNodes.getLength(); i++) {
            Element cartaoElem = (Element) cartoesNodes.item(i);
            cartoes.put(
                cartaoElem.getAttribute("data"),
                Double.parseDouble(cartaoElem.getTextContent())
            );
        }
    }

    @Override
    public Empregado copiar() {
        Horista copia = new Horista(id, nome, endereco, sindicalizado, salarioHora);
        copiarCamposComuns(copia);
        copia.cartoes = new HashMap<>(this.cartoes);
        return copia;
    }
}
