package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import br.ufal.ic.p2.wepayu.Exception.IdentificacaoEmpregadoNulaException;
import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNuloException;
import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoEmpregadoNulaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.Exception.MembroNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.NadaParaDesfazerException;
import br.ufal.ic.p2.wepayu.Exception.NadaParaRefazerException;
import br.ufal.ic.p2.wepayu.Exception.BancoNuloException;
import br.ufal.ic.p2.wepayu.Exception.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.Exception.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.Exception.ValorNaoBooleanoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNegativaException; 
import br.ufal.ic.p2.wepayu.Exception.SistemaEncerradoException;
import br.ufal.ic.p2.wepayu.Exception.NomeNaoEncontradoException;

import br.ufal.ic.p2.wepayu.Exception.PersistenciaException;

public class Facade {

    private Sistema sistema;

    private int parseId(String emp)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException {
        if (emp == null || emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        try {
            return Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }
    }

    public Facade() {
        sistema = new Sistema();

        try {
            sistema.carregarXML(ARQUIVO_XML);
        } catch (PersistenciaException e) 
            { //excessao cria exclusivo para o sistema
            
        }
    }

    private void persistir() {
        try {
            sistema.salvarXML(ARQUIVO_XML);
        } catch (PersistenciaException e) {
            System.err.println("Aviso: falha ao salvar estado em XML - " + e.getMessage());
        }
    }

    public void zerarSistema() 
        throws SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            sistema.zerarSistema();
            sucesso = true;
            persistir();

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }

    public int criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeInvalidoException, EnderecoInvalidoException, TipoInvalidoException,
            SalarioNuloException, SalarioNaoNumericoException, SalarioInvalidoException,
            TipoNaoAplicavelException, SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            if (salario.equals("")) {
                throw new SalarioNuloException();
            }

            double salarioConvertido;

            try {
                salarioConvertido = Double.parseDouble(salario.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new SalarioNaoNumericoException();
            }

            int id = sistema.criarEmpregado(nome, endereco, tipo, salarioConvertido, null);
            sucesso = true;
            persistir();
            return id;

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public Object getAtributoEmpregado(String emp, String atributo)

            throws EmpregadoNaoExisteException,
                IdentificacaoEmpregadoNulaException,
                AtributoNaoExisteException,
                TipoNaoAplicavelException,
                EmpregadoNaoRecebeEmBancoException,
                EmpregadoNaoEhSindicalizadoException,
                EmpregadoNaoEhComissionadoException {

        int id = parseId(emp);

        return sistema.getAtributoEmpregado(id, atributo);
    }   

    public int criarEmpregado(
            String nome,
            String endereco,
            String tipo,
            String salario,
            String comissao
    ) throws NomeInvalidoException,
            EnderecoInvalidoException,
            TipoInvalidoException,
            SalarioNuloException,
            SalarioNaoNumericoException,
            SalarioInvalidoException,
            ComissaoNulaException,
            ComissaoNaoNumericaException,
            ComissaoNegativaException,
            TipoNaoAplicavelException,
            SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            if (salario.equals("")) {
                throw new SalarioNuloException();
            }

            double salarioConvertido;

            try {
                salarioConvertido = Double.parseDouble(salario.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new SalarioNaoNumericoException();
            }

            if (comissao.equals("")) {
                throw new ComissaoNulaException();
            }

            double comissaoConvertida;

            try {
                comissaoConvertida = Double.parseDouble(comissao.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new ComissaoNaoNumericaException();
            }

            if (comissaoConvertida < 0) {
                throw new ComissaoNegativaException();
            }

            int id = sistema.criarEmpregado(nome, endereco, tipo, salarioConvertido, comissaoConvertida);
            sucesso = true;
            persistir();
            return id;

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public void removerEmpregado(String emp)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            int id = parseId(emp);

            sistema.removerEmpregado(id);
            sucesso = true;
            persistir();

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public void lancaCartao(
            String emp,
            String data,
            String horas)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                TipoNaoAplicavelException,
                DataInvalidaException,
                HorasPositivasException,
                EmpregadoNaoEhHoristaException,
                SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            int id = parseId(emp);

            double horasConvertidas;

            try {
                horasConvertidas = Double.parseDouble(horas.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new HorasPositivasException();
            }

            sistema.lancaCartao(id, data, horasConvertidas);
            sucesso = true;
            persistir();

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public String getHorasNormaisTrabalhadas(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        int id = parseId(emp);

        double total = sistema.getHorasNormaisTrabalhadas(
            id,
            dataInicial,
            dataFinal
        );

    return String.valueOf(total).replace(".", ",").replace(",0", "");

    }

    public String getHorasExtrasTrabalhadas(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        int id = parseId(emp);

        double total = sistema.getHorasExtrasTrabalhadas(
            id,
            dataInicial,
            dataFinal
        );

    
        return String.valueOf(total) .replace(".", ",") .replace(",0", "");
    }

    public void lancaVenda(
            String emp,
            String data,
            String valor)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInvalidaException,
                ValorPositivoException,
                SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            int id = parseId(emp);

            double valorConvertido;

            try {
                valorConvertido = Double.parseDouble(valor.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new ValorPositivoException();
            }

            sistema.lancaVenda(id, data, valorConvertido);
            sucesso = true;
            persistir();

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public String getVendasRealizadas(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        int id = parseId(emp);

        double total = sistema.getVendasRealizadas(
            id,
            dataInicial,
            dataFinal
        );

        return String.format(
            Locale.US,
            "%.2f",
            total
        ).replace(".", ",");
    }

    public void alteraEmpregado(
            String emp,
            String atributo,
            String valor,
            String idSindicato,
            String taxaSindical,
            String valor1,
            String comissao,
            String salario,
            String banco,
            String agencia,
            String contaCorrente)
            throws EmpregadoNaoExisteException,
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
        AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
            MetodoPagamentoInvalidoException, BancoNuloException,
            AgenciaNulaException,
            ContaCorrenteNulaException,
            ValorNaoBooleanoException,
            IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException,
            TaxaSindicalNaoNumericaException,
            TaxaSindicalNegativaException,
            SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            int id = parseId(emp);

            sistema.alteraEmpregado(
                id, atributo, valor, idSindicato, taxaSindical,
                valor1, comissao, salario, banco, agencia, contaCorrente
            );
            sucesso = true;
            persistir();

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public void alteraEmpregado(
        String emp,
        String atributo,
        String valor,
        String comissao)
        throws EmpregadoNaoExisteException,
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
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException, SistemaEncerradoException {
        alteraEmpregado(
        emp,
        atributo,
        valor,
        null,
        null,
        null,
        comissao,
        null,
        null,
        null,
        null
    );
}

    public void alteraEmpregado(
        String emp,
        String atributo,
        String valor)
        
        throws EmpregadoNaoExisteException,
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
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException, SistemaEncerradoException {
        alteraEmpregado(
            emp,
            atributo,
            valor,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
            );
    }

    public void alteraEmpregado(
            String emp,
            String atributo,
            String valor,
            String idSindicato,
            String taxaSindical)
            
        throws EmpregadoNaoExisteException,
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
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException, SistemaEncerradoException {
        alteraEmpregado(
            emp,
            atributo,
            valor,
            idSindicato,
            taxaSindical,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }


    public void alteraEmpregado(
            String emp,
            String atributo,
            String valor1,
            String banco,
            String agencia,
            String contaCorrente)
        throws EmpregadoNaoExisteException,
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
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException, SistemaEncerradoException {
        alteraEmpregado(
            emp,
            atributo,
            null,
            null,
            null,
            valor1,
            null,
            null,
            banco,
            agencia,
            contaCorrente
        );
    }


    public void lancaTaxaServico(
            String membro,
            String data,
            String valor)
            throws IdentificacaoMembroNulaException,
                MembroNaoExisteException,
                DataInvalidaException,
                ValorPositivoException,
                SistemaEncerradoException {

        verificarSistemaEncerrado();
        sistema.salvarParaUndo();

        boolean sucesso = false;

        try {
            double valorDouble = Double.parseDouble(valor.replace(",", "."));

            sistema.lancaTaxaServico(membro, data, valorDouble);
            sucesso = true;
            persistir();

        } finally {
            if (!sucesso) {
                sistema.descartarUltimoUndo();
            }
        }
    }
    public String getTaxasServico(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhSindicalizadoException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        int id = parseId(emp);

        double total = sistema.getTaxasServico(
            id,
            dataInicial,
            dataFinal
        );

        return String.format(
            Locale.US,
            "%.2f",
            total
        ).replace(".", ",");
    }

    public String totalFolha(String data)
        throws DataInvalidaException {

        return String.format(
            Locale.US,
            "%.2f",
            sistema.totalFolha(data)
        ).replace(".", ",");

        }
    public void rodaFolha(String data, String saida)
        throws DataInvalidaException, FileNotFoundException, SistemaEncerradoException {

    verificarSistemaEncerrado();
    sistema.salvarParaUndo();

    boolean sucesso = false;

    try {
        sistema.rodaFolha(data, saida);
        sucesso = true;
    } finally {
        if (!sucesso) {
            sistema.descartarUltimoUndo();
        }
    }
}

    private boolean encerrado = false;

    private void verificarSistemaEncerrado() throws SistemaEncerradoException {
        if (encerrado) {
            throw new SistemaEncerradoException();
        }
    }

    public void undo() throws SistemaEncerradoException, NadaParaDesfazerException {
        verificarSistemaEncerrado();
        sistema.undo();
    }

    public void redo() throws SistemaEncerradoException, NadaParaRefazerException {
        verificarSistemaEncerrado();
        sistema.redo();
    }

    public int getNumeroDeEmpregados() {
        return sistema.getNumeroDeEmpregados();
    }

    public String getEmpregadoPorNome(String nome, String indice)
            throws NomeNaoEncontradoException {

        int idx;

        try {
            idx = Integer.parseInt(indice);
        } catch (NumberFormatException e) {
            throw new NomeNaoEncontradoException();  // <- aqui também
        }

        return String.valueOf(sistema.getEmpregadoPorNome(nome, idx));
    }

    private static final String ARQUIVO_XML = "empregados.xml";


    public void encerrarSistema() {
        encerrado = true;
    }
}