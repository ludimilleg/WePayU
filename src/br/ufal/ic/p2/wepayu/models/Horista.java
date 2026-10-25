package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.DataUtil;
import br.ufal.ic.p2.wepayu.exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.exception.SalarioModeloInvalidoException;
import br.ufal.ic.p2.wepayu.exception.HorasCartaoInvalidasException;

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

        setSalario(salarioHora);
        setAgendaPagamento(AgendaPagamento.SEMANAL);

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
        if (!Double.isFinite(salario) || salario < 0) {
            throw new SalarioModeloInvalidoException();
        }
        this.salarioHora = salario;
    }

    @Override
    public Map<String, Double> getCartoes() {
        return Collections.unmodifiableMap(cartoes);
    }

    @Override
    public double calculaPagamento(LocalDate dataPagamento) {
        if (!recebeNaData(dataPagamento)) {
            return 0;
        }
        LocalDate inicioPeriodo = AgendaPagamento.inicioPeriodo(
                getAgendaPagamento(), dataPagamento);
        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                DataUtil.FORMATO
            );

            if (!dataCartao.isAfter(dataPagamento) && !dataCartao.isBefore(inicioPeriodo)) {

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

        if (!recebeNaData(dataPagamento)) {
            return 0;
        }

        LocalDate inicioPeriodo = AgendaPagamento.inicioPeriodo(
                getAgendaPagamento(), dataPagamento);
        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                DataUtil.FORMATO
            );

            if (!dataCartao.isAfter(dataPagamento) && !dataCartao.isBefore(inicioPeriodo)) {

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

        if (!recebeNaData(dataPagamento)) {
            return 0;
        }

        LocalDate inicioPeriodo = AgendaPagamento.inicioPeriodo(
                getAgendaPagamento(), dataPagamento);
        double total = 0;

        for (String data : cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(
                data,
                DataUtil.FORMATO
            );

            if (!dataCartao.isAfter(dataPagamento) && !dataCartao.isBefore(inicioPeriodo)) {

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
        if (!Double.isFinite(horas) || horas <= 0) {
            throw new HorasPositivasException();
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
    public Empregado copiar() {
        Horista copia = new Horista(
                getId(), getNome(), getEndereco(), false, salarioHora);
        copiarCamposComuns(copia);
        copia.cartoes.putAll(cartoes);
        return copia;
    }
}