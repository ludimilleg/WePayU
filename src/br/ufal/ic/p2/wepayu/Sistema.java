package br.ufal.ic.p2.wepayu;
import java.util.HashMap;
import java.util.Locale;

import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.models.Assalariado;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.HorasPositivasException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;

import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;



public class Sistema {

    HashMap<Integer, Empregado> empregados;

    int idGerado;

    public Sistema() {
        idGerado = 0;
        empregados = new HashMap<>();
    }

    public int gerarId() {
        idGerado++;
        return idGerado;
    }

    public void zerarSistema() {
        idGerado = 0;
        empregados.clear();
    }

    public int criarEmpregado(String nome, String endereco, String tipo, double salario, Double comissao)
        throws NomeInvalidoException,
               EnderecoInvalidoException,
               TipoInvalidoException,
               SalarioInvalidoException,
               TipoNaoAplicavelException {

        if (nome == null || nome.equals("")) {
            throw new NomeInvalidoException();
        }

        if (endereco == null || endereco.equals("")) {
            throw new EnderecoInvalidoException();
        }

        if (tipo == null ||
            (!tipo.equals("horista") &&
             !tipo.equals("assalariado") &&
             !tipo.equals("comissionado"))) {

            throw new TipoInvalidoException();
        }

        if (salario < 0) {
            throw new SalarioInvalidoException();
        }

        if (!tipo.equals("comissionado") && comissao != null) {
            throw new TipoNaoAplicavelException();
        }

        if (tipo.equals("comissionado") && comissao == null) {
            throw new TipoNaoAplicavelException();
        }

        int id = gerarId();

        Empregado empregado;

        if (tipo.equals("horista")) {

            empregado = new Horista(
                id, nome, endereco, false, salario
            );

        } else if (tipo.equals("assalariado")) {

            empregado = new Assalariado(
                id, nome, endereco, false, salario
            );

        } else {

            empregado = new Comissionado(
                id, nome, endereco, false, salario, comissao
            );
        }

        empregados.put(id, empregado);

        return id;
    }

   public Object getAtributoEmpregado(int id, String atributo)
    throws EmpregadoNaoExisteException,
           TipoNaoAplicavelException,
           AtributoNaoExisteException {

        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (atributo.equals("nome")) {
            return empregado.getNome();
        }

        if (atributo.equals("endereco")) {
            return empregado.getEndereco();
        }

        if (atributo.equals("tipo")) {
            return empregado.getTipo();
        }

        if (atributo.equals("salario")) {
            return String.format(Locale.US, "%.2f", empregado.getSalario()).replace(".", ",");
        }

        if (atributo.equals("comissao")) {
            return String.format(Locale.US, "%.2f", empregado.getComissao()).replace(".", ",");
        }

        if (atributo.equals("sindicalizado")) {
            return empregado.isSindicalizado();
        }

        throw new AtributoNaoExisteException();
    }

    public void removerEmpregado(int id)
        throws EmpregadoNaoExisteException {

        if (!empregados.containsKey(id)) {
            throw new EmpregadoNaoExisteException();
        }

        empregados.remove(id);
    }

    public void lancaCartao(int id, String data, double horas)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInvalidaException,
                HorasPositivasException {

        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (!(empregado instanceof Horista)) {
            throw new EmpregadoNaoEhHoristaException();
        }

        if (horas <= 0) {
            throw new HorasPositivasException();
        }

        try {
            DateTimeFormatter formato = new DateTimeFormatterBuilder()
            .appendPattern("d/M/uuuu")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(data, formato);

        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        Horista horista = (Horista) empregado;

        horista.cartoes.put(data, horas);
    }

    public double getHorasNormaisTrabalhadas(
        int id,
        String dataInicial,
        String dataFinal)
        throws EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

    Empregado empregado = empregados.get(id);

    if (empregado == null) {
        throw new EmpregadoNaoExisteException();
    }

    if (!(empregado instanceof Horista)) {
        throw new EmpregadoNaoEhHoristaException();
    }

    DateTimeFormatter formato =
        DateTimeFormatter.ofPattern("d/M/yyyy");

    LocalDate inicial;

    try {
        inicial = LocalDate.parse(dataInicial, formato);
    } catch (DateTimeParseException e) {
        throw new DataInicialInvalidaException();
    }

    LocalDate finalDate;

    try {
        finalDate = LocalDate.parse(dataFinal, formato);
    } catch (DateTimeParseException e) {
        throw new DataFinalInvalidaException();
    }

    if (inicial.isAfter(finalDate)) {
        throw new DataInicialPosteriorDataFinalException();
    }

    Horista horista = (Horista) empregado;

    double total = 0;

    for (String data : horista.cartoes.keySet()) {

        LocalDate dataCartao =
            LocalDate.parse(data, formato);

        if (!dataCartao.isBefore(inicial) &&
            dataCartao.isBefore(finalDate)) {

            double horas = horista.cartoes.get(data);

            if (horas <= 8) {
                total += horas;
            } else {
                total += 8;
            }
        }
    }

    return total;
    }

    public double getHorasExtrasTrabalhadas(
            int id,
            String dataInicial,
            String dataFinal)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (!(empregado instanceof Horista)) {
            throw new EmpregadoNaoEhHoristaException();
        }

        DateTimeFormatter formato = new DateTimeFormatterBuilder()
        .appendPattern("d/M/uuuu")
        .toFormatter()
        .withResolverStyle(ResolverStyle.STRICT);

        LocalDate inicial;

        try {
            inicial = LocalDate.parse(dataInicial, formato);
        } catch (DateTimeParseException e) {
            throw new DataInicialInvalidaException();
        }

        LocalDate finalDate;

        try {
            finalDate = LocalDate.parse(dataFinal, formato);
        } catch (DateTimeParseException e) {
            throw new DataFinalInvalidaException();
        }

        if (inicial.isAfter(finalDate)) {
            throw new DataInicialPosteriorDataFinalException();
        }

        Horista horista = (Horista) empregado;

        double total = 0;

        for (String data : horista.cartoes.keySet()) {

            LocalDate dataCartao = LocalDate.parse(data, formato);

            if (!dataCartao.isBefore(inicial) &&
                dataCartao.isBefore(finalDate)) {

                double horas = horista.cartoes.get(data);

                if (horas > 8) {
                    total += horas - 8;
                }
            }
        }

        return total;
    }

    public void lancaVenda(
            int id,
            String data,
            double valor)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInvalidaException,
                ValorPositivoException {

        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (!(empregado instanceof Comissionado)) {
            throw new EmpregadoNaoEhComissionadoException();
        }

        if (valor <= 0) {
            throw new ValorPositivoException();
        }

        DateTimeFormatter formato = new DateTimeFormatterBuilder()
                .appendPattern("d/M/uuuu")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalDate.parse(data, formato);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        Comissionado comissionado = (Comissionado) empregado;

        comissionado.vendas.put(data, valor);
    }

    public double getVendasRealizadas(
            int id,
            String dataInicial,
            String dataFinal)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (!(empregado instanceof Comissionado)) {
            throw new EmpregadoNaoEhComissionadoException();
        }

        DateTimeFormatter formato = new DateTimeFormatterBuilder()
                .appendPattern("d/M/uuuu")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

        LocalDate inicial;

        try {
            inicial = LocalDate.parse(dataInicial, formato);
        } catch (DateTimeParseException e) {
            throw new DataInicialInvalidaException();
        }

        LocalDate finalDate;

        try {
            finalDate = LocalDate.parse(dataFinal, formato);
        } catch (DateTimeParseException e) {
            throw new DataFinalInvalidaException();
        }

        if (inicial.isAfter(finalDate)) {
            throw new DataInicialPosteriorDataFinalException();
        }

        Comissionado comissionado = (Comissionado) empregado;

        double total = 0;

        for (String data : comissionado.vendas.keySet()) {

            LocalDate dataVenda = LocalDate.parse(data, formato);

            if (!dataVenda.isBefore(inicial) &&
                dataVenda.isBefore(finalDate)) {

                total += comissionado.vendas.get(data);
            }
        }

        return total;
    }

}