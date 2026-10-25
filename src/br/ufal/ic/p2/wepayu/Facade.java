package br.ufal.ic.p2.wepayu;

import java.util.Locale;
import java.io.FileNotFoundException;

import br.ufal.ic.p2.wepayu.exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.AgendaPagamentosJaExisteException;
import br.ufal.ic.p2.wepayu.exception.DescricaoAgendaInvalidaException;
import br.ufal.ic.p2.wepayu.exception.FalhaCarregamentoSistemaException;
import br.ufal.ic.p2.wepayu.exception.AgendaPagamentoIndisponivelException;
import br.ufal.ic.p2.wepayu.exception.ComissaoNaoNumericaException;
import br.ufal.ic.p2.wepayu.exception.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.exception.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.exception.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.SalarioNuloException;
import br.ufal.ic.p2.wepayu.exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoEmpregadoNulaException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.exception.MembroNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.NadaParaDesfazerException;
import br.ufal.ic.p2.wepayu.exception.NadaParaRefazerException;
import br.ufal.ic.p2.wepayu.exception.BancoNuloException;
import br.ufal.ic.p2.wepayu.exception.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.exception.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.exception.ValorNaoBooleanoException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.exception.SistemaEncerradoException;
import br.ufal.ic.p2.wepayu.exception.NomeNaoEncontradoException;

import br.ufal.ic.p2.wepayu.exception.PersistenciaException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalNegativaException;

public class Facade {

    private Sistema sistema;

    public Facade() {
        sistema = new Sistema();

        try {
            sistema.carregarXML(ARQUIVO_XML);
        } catch (PersistenciaException e) {
            throw new FalhaCarregamentoSistemaException(ARQUIVO_XML, e);
        }
    }

    public void zerarSistema() 
        throws SistemaEncerradoException {
        sistema.zerarSistema();
    }

    public void criarAgendaDePagamentos(String descricao)
            throws AgendaPagamentosJaExisteException, DescricaoAgendaInvalidaException,
            SistemaEncerradoException {
        sistema.criarAgendaDePagamentos(descricao);
    }

    public int criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeInvalidoException, EnderecoInvalidoException, TipoInvalidoException,
            SalarioNuloException, SalarioNaoNumericoException, SalarioInvalidoException,
            TipoNaoAplicavelException, SistemaEncerradoException {

        return sistema.criarEmpregado(nome, endereco, tipo, salario);
    }
    public Object getAtributoEmpregado(String emp, String atributo)

            throws EmpregadoNaoExisteException,
                IdentificacaoEmpregadoNulaException,
                AtributoNaoExisteException,
                TipoNaoAplicavelException,
                EmpregadoNaoRecebeEmBancoException,
                EmpregadoNaoEhSindicalizadoException,
                EmpregadoNaoEhComissionadoException {

        return sistema.getAtributoEmpregado(emp, atributo);
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

        return sistema.criarEmpregado(nome, endereco, tipo, salario, comissao);
    }
    public void removerEmpregado(String emp)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                SistemaEncerradoException {

        sistema.removerEmpregado(emp);
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

        sistema.lancaCartao(emp, data, horas);
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

        double total = sistema.getHorasNormaisTrabalhadas(
            emp,
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

        double total = sistema.getHorasExtrasTrabalhadas(
            emp,
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

        sistema.lancaVenda(emp, data, valor);
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

        double total = sistema.getVendasRealizadas(
            emp,
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
            AgendaPagamentoIndisponivelException,
            SistemaEncerradoException {

        executarAlteracao(
                emp,
                AlteracaoEmpregado.para(atributo, valor)
                        .comDadosSindicais(idSindicato, taxaSindical)
                        .comValorAuxiliar(valor1)
                        .comComissao(comissao)
                        .comSalario(salario)
                        .comDadosBancarios(banco, agencia, contaCorrente));
    }

    private void executarAlteracao(String emp, AlteracaoEmpregado alteracao)
            throws EmpregadoNaoExisteException, IdentificacaoSindicatoJaExisteException,
            NomeInvalidoException, EnderecoInvalidoException, SalarioNuloException,
            SalarioNaoNumericoException, SalarioInvalidoException, TipoInvalidoException,
            ComissaoNulaException, ComissaoNaoNumericaException, ComissaoNegativaException,
            EmpregadoNaoEhComissionadoException, AtributoNaoExisteException,
            IdentificacaoEmpregadoNulaException, MetodoPagamentoInvalidoException,
            BancoNuloException, AgenciaNulaException, ContaCorrenteNulaException,
            ValorNaoBooleanoException, IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException, TaxaSindicalNaoNumericaException,
            TaxaSindicalNegativaException, AgendaPagamentoIndisponivelException,
            SistemaEncerradoException {
        sistema.alteraEmpregado(emp, alteracao);
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
        TaxaSindicalNegativaException, AgendaPagamentoIndisponivelException,
        SistemaEncerradoException {
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
        TaxaSindicalNegativaException, AgendaPagamentoIndisponivelException,
        SistemaEncerradoException {
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
        TaxaSindicalNegativaException, AgendaPagamentoIndisponivelException,
        SistemaEncerradoException {
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
        TaxaSindicalNegativaException, AgendaPagamentoIndisponivelException,
        SistemaEncerradoException {
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

        sistema.lancaTaxaServico(membro, data, valor);
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

        double total = sistema.getTaxasServico(
            emp,
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
        sistema.rodaFolha(data, saida);
    }

    public void undo() throws SistemaEncerradoException, NadaParaDesfazerException {
        sistema.undo();
    }

    public void redo() throws SistemaEncerradoException, NadaParaRefazerException {
        sistema.redo();
    }

    public int getNumeroDeEmpregados() {
        return sistema.getNumeroDeEmpregados();
    }

    public String getEmpregadoPorNome(String nome, String indice)
            throws NomeNaoEncontradoException {

        return String.valueOf(sistema.getEmpregadoPorNome(nome, indice));
    }

    private static final String ARQUIVO_XML = "empregados.xml";


    public void encerrarSistema() {
        sistema.encerrar();
    }
}