package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.DataUtil;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.HorasPositivasException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Horista extends Empregado {

    private double salarioHora;

    private final HashMap<String, Double> cartoes;

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

    public Map<String, Double> getCartoes() {
        return Collections.unmodifiableMap(cartoes);
    }

    @Override
    public boolean recebeNaData(LocalDate dataPagamento) {
        return dataPagamento.getDayOfWeek() == DayOfWeek.FRIDAY;
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
                DataUtil.FORMATO
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

    @Override
    public double getHorasNormais(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                DataUtil.FORMATO
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

    @Override
    public double getHorasExtras(LocalDate dataPagamento) {

        if (dataPagamento.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return 0;
        }

        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                DataUtil.FORMATO
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
                DataUtil.FORMATO
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
                DataUtil.FORMATO
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
                DataUtil.FORMATO
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
    public void exportarDadosExtras(Exportador destino) {
        super.exportarDadosExtras(destino);

        for (Map.Entry<String, Double> cartao : cartoes.entrySet()) {
            destino.registro("cartao", cartao.getKey(), cartao.getValue());
        }
    }

    @Override
    public void importarRegistro(String colecao, String data, double valor) {
        if (colecao.equals("cartao")) {
            cartoes.put(data, valor);
            return;
        }

        super.importarRegistro(colecao, data, valor);
    }

    @Override
    public Empregado copiar() {
        Horista copia = new Horista(
                getId(), getNome(), getEndereco(), isSindicalizado(), salarioHora);
        copiarCamposComuns(copia);
        copia.cartoes.putAll(cartoes);
        return copia;
    }
}