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
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.Exception.MembroNaoExisteException;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNuloException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.Exception.BancoNuloException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.Exception.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.Exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNegativaException;
import br.ufal.ic.p2.wepayu.Exception.ValorNaoBooleanoException;
import java.time.DayOfWeek;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import br.ufal.ic.p2.wepayu.Exception.NadaParaDesfazerException;
import br.ufal.ic.p2.wepayu.Exception.NadaParaRefazerException;
import br.ufal.ic.p2.wepayu.Exception.NomeNaoEncontradoException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import br.ufal.ic.p2.wepayu.Exception.PersistenciaException;

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
               AtributoNaoExisteException,
               EmpregadoNaoRecebeEmBancoException,
               EmpregadoNaoEhSindicalizadoException, EmpregadoNaoEhComissionadoException {

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

            if (!empregado.getTipo().equals("comissionado")) {
                throw new EmpregadoNaoEhComissionadoException();
            }

            return String.format(
                Locale.US,
                "%.2f",
                empregado.getComissao()
            ).replace(".", ",");
        }

        if (atributo.equals("sindicalizado")) {
            return empregado.isSindicalizado();
        }

        if (atributo.equals("metodoPagamento")) {
            return empregado.getMetodoPagamento();
        }

        if (atributo.equals("banco")) {
            if (!empregado.getMetodoPagamento().equals("banco")) {
                throw new EmpregadoNaoRecebeEmBancoException();
            }

            return empregado.getBanco();
        }

        if (atributo.equals("agencia")) {
            if (!empregado.getMetodoPagamento().equals("banco")) {
                throw new EmpregadoNaoRecebeEmBancoException();
            }

            return empregado.getAgencia();
        }

        if (atributo.equals("contaCorrente")) {
            if (!empregado.getMetodoPagamento().equals("banco")) {
                throw new EmpregadoNaoRecebeEmBancoException();
            }

            return empregado.getContaCorrente();
        }

        if (atributo.equals("idSindicato")) {
            if (!empregado.isSindicalizado()) {
                throw new EmpregadoNaoEhSindicalizadoException();
            }

            return empregado.getIdSindicato();
        }

        if (atributo.equals("taxaSindical")) {
            if (!empregado.isSindicalizado()) {
                throw new EmpregadoNaoEhSindicalizadoException();
            }

            return String.format(
                Locale.US,
                "%.2f",
                empregado.getTaxaSindical()
            ).replace(".", ",");
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

        empregado.lancarCartao(data, horas);
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

        DateTimeFormatter formato = DataUtil.FORMATO;

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

        return empregado.getHorasNormaisTrabalhadas(inicial, finalDate);
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

        DateTimeFormatter formato = DataUtil.FORMATO;

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

        return empregado.getHorasExtrasTrabalhadas(inicial, finalDate);
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

        empregado.lancarVenda(data, valor);
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

        DateTimeFormatter formato = DataUtil.FORMATO;

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

        return empregado.getVendasRealizadasNoPeriodo(inicial, finalDate);
    }

    public void alteraEmpregado(
        int id,
        String atributo,
        String valor,
        String idSindicato,
        String taxaSindical,
        String valor1,
        String comissao,
        String salario,
        String banco,
        String agencia,
        String contaCorrente)throws EmpregadoNaoExisteException,
       IdentificacaoSindicatoJaExisteException,
       NomeInvalidoException,
       EnderecoInvalidoException,
       SalarioNuloException,
       SalarioNaoNumericoException,
       SalarioInvalidoException,
       TipoInvalidoException,
       ComissaoNulaException,
       ComissaoNaoNumericaException,
       ComissaoNegativaException,
       EmpregadoNaoEhComissionadoException,
       MetodoPagamentoInvalidoException,
       BancoNuloException,
       AgenciaNulaException,
       ContaCorrenteNulaException,
       ValorNaoBooleanoException,
       IdentificacaoSindicatoNulaException,
       TaxaSindicalNulaException,
       TaxaSindicalNaoNumericaException,
       TaxaSindicalNegativaException,
       AtributoNaoExisteException{
        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (atributo.equals("nome")) {

            if (valor.equals("")) {
                throw new NomeInvalidoException();
            }

            empregado.setNome(valor);
        }

        if (atributo.equals("endereco")) {

            if (valor.equals("")) {
                throw new EnderecoInvalidoException();
            }

            empregado.setEndereco(valor);
        }

        if (atributo.equals("salario")) {

            if (valor == null || valor.equals("")) {
                throw new SalarioNuloException();
            }

            double novoSalario;

            try {
                novoSalario = Double.parseDouble(valor.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new SalarioNaoNumericoException();
            }

            if (novoSalario < 0) {
                throw new SalarioInvalidoException();
            }

            empregado.setSalario(novoSalario);
        }

        if (atributo.equals("tipo")) {

            if (!valor.equals("horista") &&
                !valor.equals("assalariado") &&
                !valor.equals("comissionado")) {

                throw new TipoInvalidoException();
            }

            double salarioAtual = empregado.getSalario();

            if (valor.equals("horista")) {

                double novoSalario = salarioAtual;

                if (comissao != null && !comissao.equals("")) {
                    novoSalario = Double.parseDouble(comissao.replace(",", "."));
                }

                Empregado novoEmpregado = new Horista(
                    id,
                    empregado.getNome(),
                    empregado.getEndereco(),
                    empregado.isSindicalizado(),
                    novoSalario
                );

                empregado.copiarCamposComuns(novoEmpregado);

                empregados.put(id, novoEmpregado);
            }

            else if (valor.equals("assalariado")) {

                Empregado novoEmpregado = new Assalariado(
                    id,
                    empregado.getNome(),
                    empregado.getEndereco(),
                    empregado.isSindicalizado(),
                    salarioAtual
                );
                empregado.copiarCamposComuns(novoEmpregado);

                empregados.put(id, novoEmpregado);
            }

            else if (valor.equals("comissionado")) {

                if (comissao == null || comissao.equals("")) {
                    throw new ComissaoNulaException();
                }

                double novaComissao;

                try {
                    novaComissao = Double.parseDouble(
                        comissao.replace(",", ".")
                    );
                } catch (NumberFormatException e) {
                    throw new ComissaoNaoNumericaException();
                }

                if (novaComissao < 0) {
                    throw new ComissaoNegativaException();
                }

                Empregado novoEmpregado = new Comissionado(
                    id,
                    empregado.getNome(),
                    empregado.getEndereco(),
                    empregado.isSindicalizado(),
                    salarioAtual,
                    novaComissao
                );

                empregado.copiarCamposComuns(novoEmpregado);

                empregados.put(id, novoEmpregado);
            }
        }


        if (atributo.equals("comissao")) {

            if (valor == null || valor.equals("")) {
                throw new ComissaoNulaException();
            }

            double novaComissao;

            try {
                novaComissao = Double.parseDouble(
                    valor.replace(",", ".")
                );
            } catch (NumberFormatException e) {
                throw new ComissaoNaoNumericaException();
            }

            if (novaComissao < 0) {
                throw new ComissaoNegativaException();
            }

            empregado.alterarComissao(novaComissao);
        }


        if (atributo.equals("metodoPagamento")) {

            String metodo;

            if (valor1 != null) {
                metodo = valor1;
            } else {
                metodo = valor;
            }

            if (!metodo.equals("emMaos") &&
                !metodo.equals("correios") &&
                !metodo.equals("banco")) {

                throw new MetodoPagamentoInvalidoException();
            }

            if (metodo.equals("banco")) {

                if (banco == null || banco.equals("")) {
                    throw new BancoNuloException();
                }

                if (agencia == null || agencia.equals("")) {
                    throw new AgenciaNulaException();
                }

                if (contaCorrente == null || contaCorrente.equals("")) {
                    throw new ContaCorrenteNulaException();
                }

                empregado.setBanco(banco);
                empregado.setAgencia(agencia);
                empregado.setContaCorrente(contaCorrente);
            }

            empregado.setMetodoPagamento(metodo);
        }

        if (atributo.equals("sindicalizado")) {

            if (!valor.equals("true") && !valor.equals("false")) {
                throw new ValorNaoBooleanoException();
            }

            if (valor.equals("true")) {

                if (idSindicato == null || idSindicato.equals("")) {
                    throw new IdentificacaoSindicatoNulaException();
                }

                if (taxaSindical == null || taxaSindical.equals("")) {
                    throw new TaxaSindicalNulaException();
                }

                double taxa;

                try {
                    taxa = Double.parseDouble(
                        taxaSindical.replace(",", ".")
                    );
                } catch (NumberFormatException e) {
                    throw new TaxaSindicalNaoNumericaException();
                }

                if (taxa < 0) {
                    throw new TaxaSindicalNegativaException();
                }

                for (Empregado outro : empregados.values()) {

                    if (outro != empregado &&
                        outro.isSindicalizado() &&
                        outro.getIdSindicato() != null &&
                        outro.getIdSindicato().equals(idSindicato)) {

                        throw new IdentificacaoSindicatoJaExisteException();
                    }
                }

                empregado.setSindicalizado(true);
                empregado.setIdSindicato(idSindicato);
                empregado.setTaxaSindical(taxa);

            } else {

                empregado.setSindicalizado(false);
                empregado.setIdSindicato(null);
                empregado.setTaxaSindical(0);
            }
        }


        if (!atributo.equals("nome") &&
            !atributo.equals("endereco") &&
            !atributo.equals("salario") &&
            !atributo.equals("tipo") &&
            !atributo.equals("comissao") &&
            !atributo.equals("metodoPagamento") &&
            !atributo.equals("sindicalizado")) {

            throw new AtributoNaoExisteException();
        }
    }    

    public void lancaTaxaServico(
            String membro,
            String data,
            double valor)
            throws IdentificacaoMembroNulaException,
                MembroNaoExisteException,
                DataInvalidaException,
                ValorPositivoException {

        if (membro.equals("")) {
            throw new IdentificacaoMembroNulaException();
        }

        Empregado empregadoEncontrado = null;

        for (Empregado empregado : empregados.values()) {

            if (empregado.getIdSindicato() != null &&
                empregado.getIdSindicato().equals(membro)) {

                empregadoEncontrado = empregado;
                break;
            }
        }

        if (empregadoEncontrado == null) {
            throw new MembroNaoExisteException();
        }

        if (valor <= 0) {
            throw new ValorPositivoException();
        }

        DateTimeFormatter formato = DataUtil.FORMATO;

        try {
            LocalDate.parse(data, formato);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        empregadoEncontrado.getTaxasServico().put(data, valor);
    }

    public double getTaxasServico(
            int id,
            String dataInicial,
            String dataFinal)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhSindicalizadoException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        Empregado empregado = empregados.get(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (!empregado.isSindicalizado()) {
            throw new EmpregadoNaoEhSindicalizadoException();
        }

        DateTimeFormatter formato = DataUtil.FORMATO;

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

        double total = 0;

        for (String data : empregado.getTaxasServico().keySet()) {

            LocalDate dataTaxa = LocalDate.parse(data, formato);

            if (!dataTaxa.isBefore(inicial) &&
                dataTaxa.isBefore(finalDate)) {

                total += empregado.getTaxasServico().get(data);
            }
        }

        return total;
    }

    private double calculaPagamento(
            Empregado empregado,
            LocalDate dataPagamento) {

        return empregado.calculaPagamento(dataPagamento);
    }

   public double totalFolha(String data)
        throws DataInvalidaException {

        DateTimeFormatter formato = DataUtil.FORMATO;

        LocalDate dataPagamento;

        try {
            dataPagamento = LocalDate.parse(data, formato);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        double total = 0;

        for (Empregado empregado : empregados.values()) {
            total += calculaPagamento(empregado, dataPagamento);
        }

        return total;
    }

    public HashMap<Integer, Empregado> getEmpregados() {
        return empregados;
    }

    private static class Estado {
    HashMap<Integer, Empregado> empregados;
    int idGerado;
}

    private Deque<Estado> undoStack = new ArrayDeque<>();
    private Deque<Estado> redoStack = new ArrayDeque<>();

    private Estado criarEstadoAtual() {
        Estado estado = new Estado();
        estado.idGerado = this.idGerado;
        estado.empregados = new HashMap<>();

        for (Map.Entry<Integer, Empregado> entry : this.empregados.entrySet()) {
            estado.empregados.put(entry.getKey(), entry.getValue().copiar());
        }

        return estado;
    }

    private void aplicarEstado(Estado estado) {
        this.idGerado = estado.idGerado;
        this.empregados = estado.empregados;
    }

    public void salvarParaUndo() {
        undoStack.push(criarEstadoAtual());
        redoStack.clear();
    }

    public void descartarUltimoUndo() {
        if (!undoStack.isEmpty()) {
            undoStack.pop();
        }
    }

    public void undo() throws NadaParaDesfazerException {

        if (undoStack.isEmpty()) {
            throw new NadaParaDesfazerException();
        }

        redoStack.push(criarEstadoAtual());
        aplicarEstado(undoStack.pop());
    }

    public void redo() throws NadaParaRefazerException {

        if (redoStack.isEmpty()) {
            throw new NadaParaRefazerException();
        }

        undoStack.push(criarEstadoAtual());
        aplicarEstado(redoStack.pop());
    }

    public int getNumeroDeEmpregados() {
        return empregados.size();
    }

    public int getEmpregadoPorNome(String nome, int indice)
        throws NomeNaoEncontradoException {

        List<Integer> ids = new ArrayList<>(empregados.keySet());
        Collections.sort(ids);

        List<Empregado> encontrados = new ArrayList<>();

        for (Integer id : ids) {
            Empregado empregado = empregados.get(id);
            if (empregado.getNome().equals(nome)) {
                encontrados.add(empregado);
            }
        }

        if (indice < 1 || indice > encontrados.size()) {
            throw new NomeNaoEncontradoException();  // <- aqui, não EmpregadoNaoExisteException
        }

        return encontrados.get(indice - 1).getId();
    }

    public void salvarXML(String caminho) throws PersistenciaException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new PersistenciaException("Erro ao configurar o parser XML.");
        }

        Document doc = builder.newDocument();

        Element root = doc.createElement("sistema");
        doc.appendChild(root);

        Element idElem = doc.createElement("idGerado");
        idElem.setTextContent(String.valueOf(idGerado));
        root.appendChild(idElem);

        Element empsElem = doc.createElement("empregados");
        root.appendChild(empsElem);

        for (Empregado emp : empregados.values()) {

            Element e = doc.createElement("empregado");
            e.setAttribute("id", String.valueOf(emp.getId()));
            e.setAttribute("tipo", emp.getTipo());

            criarFilho(doc, e, "nome", emp.getNome());
            criarFilho(doc, e, "endereco", emp.getEndereco());
            criarFilho(doc, e, "sindicalizado", String.valueOf(emp.isSindicalizado()));

            if (emp.getIdSindicato() != null) {
                criarFilho(doc, e, "idSindicato", emp.getIdSindicato());
            }

            criarFilho(doc, e, "taxaSindical", String.valueOf(emp.getTaxaSindical()));
            criarFilho(doc, e, "metodoPagamento", emp.getMetodoPagamento());

            if (emp.getBanco() != null) {
                criarFilho(doc, e, "banco", emp.getBanco());
            }

            if (emp.getAgencia() != null) {
                criarFilho(doc, e, "agencia", emp.getAgencia());
            }

            if (emp.getContaCorrente() != null) {
                criarFilho(doc, e, "contaCorrente", emp.getContaCorrente());
            }

            criarFilho(doc, e, "salario", String.valueOf(emp.getSalario()));

            emp.salvarDadosExtras(doc, e);

            Element taxasElem = doc.createElement("taxasServico");

            for (Map.Entry<String, Double> t : emp.getTaxasServico().entrySet()) {
                Element tEl = doc.createElement("taxa");
                tEl.setAttribute("data", t.getKey());
                tEl.setTextContent(String.valueOf(t.getValue()));
                taxasElem.appendChild(tEl);
            }

            e.appendChild(taxasElem);

            empsElem.appendChild(e);
        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(new File(caminho)));
        } catch (TransformerException e) {
            throw new PersistenciaException("Erro ao escrever o arquivo XML.");
        }
    }

    public void carregarXML(String caminho) throws PersistenciaException {

        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new PersistenciaException("Erro ao configurar o parser XML.");
        }

        Document doc;

        try {
            doc = builder.parse(arquivo);
        } catch (SAXException e) {
            throw new PersistenciaException("Arquivo XML mal formado.");
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao ler o arquivo XML.");
        }

        empregados = new HashMap<>();

        Element root = doc.getDocumentElement();

        Node idNode = root.getElementsByTagName("idGerado").item(0);
        idGerado = Integer.parseInt(idNode.getTextContent());

        NodeList empNodes = root.getElementsByTagName("empregado");

        for (int i = 0; i < empNodes.getLength(); i++) {

            Element e = (Element) empNodes.item(i);

            int id = Integer.parseInt(e.getAttribute("id"));
            String tipo = e.getAttribute("tipo");

            String nome = textoFilho(e, "nome");
            String endereco = textoFilho(e, "endereco");
            boolean sindicalizado = Boolean.parseBoolean(textoFilho(e, "sindicalizado"));
            double salario = Double.parseDouble(textoFilho(e, "salario"));

            Empregado emp;

            if (tipo.equals("horista")) {
                emp = new Horista(id, nome, endereco, sindicalizado, salario);
            } else if (tipo.equals("assalariado")) {
                emp = new Assalariado(id, nome, endereco, sindicalizado, salario);
            } else {
                double comissao = Double.parseDouble(textoFilho(e, "comissao"));
                emp = new Comissionado(id, nome, endereco, sindicalizado, salario, comissao);
            }

            String idSindicato = textoFilho(e, "idSindicato");
            if (idSindicato != null) {
                emp.setIdSindicato(idSindicato);
            }

            emp.setTaxaSindical(Double.parseDouble(textoFilho(e, "taxaSindical")));
            emp.setMetodoPagamento(textoFilho(e, "metodoPagamento"));

            String banco = textoFilho(e, "banco");
            if (banco != null) {
                emp.setBanco(banco);
            }

            String agencia = textoFilho(e, "agencia");
            if (agencia != null) {
                emp.setAgencia(agencia);
            }

            String contaCorrente = textoFilho(e, "contaCorrente");
            if (contaCorrente != null) {
                emp.setContaCorrente(contaCorrente);
            }

            emp.carregarDadosExtras(e);

            NodeList taxasNodes = e.getElementsByTagName("taxa");

            for (int j = 0; j < taxasNodes.getLength(); j++) {
                Element tEl = (Element) taxasNodes.item(j);
                emp.getTaxasServico().put(tEl.getAttribute("data"), Double.parseDouble(tEl.getTextContent()));
            }

            empregados.put(id, emp);
        }
    }

    private void criarFilho(Document doc, Element pai, String nomeTag, String valor) {
        Element filho = doc.createElement(nomeTag);
        filho.setTextContent(valor);
        pai.appendChild(filho);
    }

    private String textoFilho(Element pai, String nomeTag) {
        NodeList nodes = pai.getElementsByTagName(nomeTag);
        if (nodes.getLength() == 0) {
            return null;
        }
        return nodes.item(0).getTextContent();
    }

}
