package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.AgendaPagamentoIndisponivelException;
import br.ufal.ic.p2.wepayu.exception.AgendaPagamentosJaExisteException;
import br.ufal.ic.p2.wepayu.exception.DescricaoAgendaInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoSindicatoJaExisteException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.exception.MembroNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.exception.SalarioNuloException;
import br.ufal.ic.p2.wepayu.exception.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.exception.BancoNuloException;
import br.ufal.ic.p2.wepayu.exception.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.exception.ComissaoNaoNumericaException;
import br.ufal.ic.p2.wepayu.exception.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.exception.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalNegativaException;
import br.ufal.ic.p2.wepayu.exception.ValorNaoBooleanoException;
import br.ufal.ic.p2.wepayu.exception.SistemaEncerradoException;
import java.util.List;
import br.ufal.ic.p2.wepayu.exception.NadaParaDesfazerException;
import br.ufal.ic.p2.wepayu.exception.NadaParaRefazerException;
import br.ufal.ic.p2.wepayu.exception.NomeNaoEncontradoException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoEmpregadoNulaException;


import br.ufal.ic.p2.wepayu.exception.PersistenciaException;
import br.ufal.ic.p2.wepayu.exception.FalhaPersistenciaSistemaException;
import br.ufal.ic.p2.wepayu.folha.FolhaDePagamento;
import java.io.FileNotFoundException;
import br.ufal.ic.p2.wepayu.models.EstadoSistema;
import br.ufal.ic.p2.wepayu.models.AgendaPagamento;

import java.util.LinkedHashSet;
import java.util.Set;
public class Sistema {

    private static final String ARQUIVO_XML = "empregados.xml";

    private final CadastroEmpregados cadastro;
    private final ConsultasEmpregados consultas;
    private Set<String> agendasPagamento;
    private boolean encerrado;

    public Sistema() {
        cadastro = new CadastroEmpregados();
        consultas = new ConsultasEmpregados();
        agendasPagamento = new LinkedHashSet<>(AgendaPagamento.agendasPadrao());
        encerrado = false;
    }

    private void verificarDisponibilidade() throws SistemaEncerradoException {
        if (encerrado) {
            throw new SistemaEncerradoException();
        }
    }

    void encerrar() {
        encerrado = true;
    }

    public void zerarSistema() throws SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            cadastro.zerar();
            agendasPagamento = new LinkedHashSet<>(AgendaPagamento.agendasPadrao());
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    public void criarAgendaDePagamentos(String descricao)
            throws DescricaoAgendaInvalidaException, AgendaPagamentosJaExisteException,
            SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            if (!AgendaPagamento.descricaoValida(descricao)) {
                throw new DescricaoAgendaInvalidaException();
            }
            String normalizada = AgendaPagamento.normalizar(descricao);
            if (agendasPagamento.contains(normalizada)) {
                throw new AgendaPagamentosJaExisteException();
            }
            agendasPagamento.add(normalizada);
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    private int criarEmpregado(String nome, String endereco, String tipo,
                               double salario, Double comissao)
        throws NomeInvalidoException,
               EnderecoInvalidoException,
               TipoInvalidoException,
               SalarioInvalidoException,
               TipoNaoAplicavelException {

        return cadastro.criar(nome, endereco, tipo, salario, comissao);
    }

    public int criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeInvalidoException, EnderecoInvalidoException, TipoInvalidoException,
            SalarioNuloException, SalarioNaoNumericoException, SalarioInvalidoException,
            TipoNaoAplicavelException, SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            int id = criarEmpregado(nome, endereco, tipo, converterSalario(salario), null);
            confirmarMutacao();
            sucesso = true;
            return id;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    public int criarEmpregado(String nome, String endereco, String tipo, String salario,
                              String comissao)
            throws NomeInvalidoException, EnderecoInvalidoException, TipoInvalidoException,
            SalarioNuloException, SalarioNaoNumericoException, SalarioInvalidoException,
            ComissaoNulaException, ComissaoNaoNumericaException, ComissaoNegativaException,
            TipoNaoAplicavelException, SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            double salarioConvertido = converterSalario(salario);
            double comissaoConvertida;
            if (comissao == null || comissao.isEmpty()) {
                throw new ComissaoNulaException();
            }
            try {
                comissaoConvertida = ConversorDecimal.converter(comissao);
            } catch (NumberFormatException e) {
                throw new ComissaoNaoNumericaException();
            }
            if (!Double.isFinite(comissaoConvertida) || comissaoConvertida < 0) {
                throw new ComissaoNegativaException();
            }
            int id = criarEmpregado(nome, endereco, tipo, salarioConvertido, comissaoConvertida);
            confirmarMutacao();
            sucesso = true;
            return id;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    private double converterSalario(String salario)
            throws SalarioNuloException, SalarioNaoNumericoException {
        if (salario == null || salario.isEmpty()) {
            throw new SalarioNuloException();
        }
        try {
            return ConversorDecimal.converter(salario);
        } catch (NumberFormatException e) {
            throw new SalarioNaoNumericoException();
        }
    }

    private int parseId(String empregado)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException {
        if (empregado == null || empregado.isEmpty()) {
            throw new IdentificacaoEmpregadoNulaException();
        }
        try {
            return Integer.parseInt(empregado);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }
    }

    public Object getAtributoEmpregado(String empregado, String atributo)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNulaException,
            TipoNaoAplicavelException, AtributoNaoExisteException,
            EmpregadoNaoRecebeEmBancoException, EmpregadoNaoEhSindicalizadoException,
            EmpregadoNaoEhComissionadoException {
        return getAtributoEmpregado(parseId(empregado), atributo);
    }

   public Object getAtributoEmpregado(int id, String atributo)
        throws EmpregadoNaoExisteException,
               TipoNaoAplicavelException,
               AtributoNaoExisteException,
               EmpregadoNaoRecebeEmBancoException,
               EmpregadoNaoEhSindicalizadoException, EmpregadoNaoEhComissionadoException {
        return consultas.getAtributo(cadastro.todos(), id, atributo);
    }

    private void removerEmpregado(int id)
        throws EmpregadoNaoExisteException {
        cadastro.remover(id);
    }

    public void removerEmpregado(String empregado)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNulaException,
            SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            removerEmpregado(parseId(empregado));
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    private void lancaCartao(int id, String data, double horas)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInvalidaException,
                HorasPositivasException {

        Empregado empregado = cadastro.obter(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        empregado.lancarCartao(data, horas);
    }

    public void lancaCartao(String empregado, String data, String horas)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException,
            EmpregadoNaoEhHoristaException, DataInvalidaException, HorasPositivasException,
            SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            int id = parseId(empregado);
            double valor;
            try {
                valor = ConversorDecimal.converter(horas);
            } catch (NumberFormatException e) {
                throw new HorasPositivasException();
            }
            lancaCartao(id, data, valor);
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
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

        return consultas.horasNormais(cadastro.todos(), id, dataInicial, dataFinal);
    }

    public double getHorasNormaisTrabalhadas(
            String empregado, String dataInicial, String dataFinal)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException,
            EmpregadoNaoEhHoristaException, DataInicialInvalidaException,
            DataFinalInvalidaException, DataInicialPosteriorDataFinalException {
        return getHorasNormaisTrabalhadas(parseId(empregado), dataInicial, dataFinal);
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

        return consultas.horasExtras(cadastro.todos(), id, dataInicial, dataFinal);
    }

    public double getHorasExtrasTrabalhadas(
            String empregado, String dataInicial, String dataFinal)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException,
            EmpregadoNaoEhHoristaException, DataInicialInvalidaException,
            DataFinalInvalidaException, DataInicialPosteriorDataFinalException {
        return getHorasExtrasTrabalhadas(parseId(empregado), dataInicial, dataFinal);
    }

    private void lancaVenda(
            int id,
            String data,
            double valor)
            throws EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInvalidaException,
                ValorPositivoException {

        Empregado empregado = cadastro.obter(id);

        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        empregado.lancarVenda(data, valor);
    }

    public void lancaVenda(String empregado, String data, String valor)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException,
            EmpregadoNaoEhComissionadoException, DataInvalidaException,
            ValorPositivoException, SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            int id = parseId(empregado);
            double valorConvertido;
            try {
                valorConvertido = ConversorDecimal.converter(valor);
            } catch (NumberFormatException e) {
                throw new ValorPositivoException();
            }
            lancaVenda(id, data, valorConvertido);
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
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

        return consultas.vendasRealizadas(cadastro.todos(), id, dataInicial, dataFinal);
    }

    public double getVendasRealizadas(
            String empregado, String dataInicial, String dataFinal)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException,
            EmpregadoNaoEhComissionadoException, DataInicialInvalidaException,
            DataFinalInvalidaException, DataInicialPosteriorDataFinalException {
        return getVendasRealizadas(parseId(empregado), dataInicial, dataFinal);
    }

    private void alteraEmpregado(
        int id,
        AlteracaoEmpregado alteracao)
        throws EmpregadoNaoExisteException,
       AgendaPagamentoIndisponivelException,
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
       new AlteradorEmpregado(cadastro.todos(), agendasPagamento).alterar(id, alteracao);
    }    

    public void alteraEmpregado(String empregado, AlteracaoEmpregado alteracao)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNulaException,
            IdentificacaoSindicatoJaExisteException, NomeInvalidoException,
            EnderecoInvalidoException, SalarioNuloException, SalarioNaoNumericoException,
            SalarioInvalidoException, TipoInvalidoException, ComissaoNulaException,
            ComissaoNaoNumericaException, ComissaoNegativaException,
            EmpregadoNaoEhComissionadoException, MetodoPagamentoInvalidoException,
            BancoNuloException, AgenciaNulaException, ContaCorrenteNulaException,
            ValorNaoBooleanoException, IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException, TaxaSindicalNaoNumericaException,
            TaxaSindicalNegativaException, AtributoNaoExisteException,
            AgendaPagamentoIndisponivelException, SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            alteraEmpregado(parseId(empregado), alteracao);
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    private void lancaTaxaServico(
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

        for (Empregado empregado : cadastro.todos().values()) {

            if (empregado.getIdSindicato() != null &&
                empregado.getIdSindicato().equals(membro)) {

                empregadoEncontrado = empregado;
                break;
            }
        }

        if (empregadoEncontrado == null) {
            throw new MembroNaoExisteException();
        }

        if (!Double.isFinite(valor) || valor <= 0) {
            throw new ValorPositivoException();
        }

        DateTimeFormatter formato = DataUtil.FORMATO;

        try {
            LocalDate.parse(data, formato);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        empregadoEncontrado.registrarTaxaServico(data, valor);
    }

    public void lancaTaxaServico(String membro, String data, String valor)
            throws IdentificacaoMembroNulaException, MembroNaoExisteException,
            DataInvalidaException, ValorPositivoException, SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            double valorConvertido;
            try {
                valorConvertido = ConversorDecimal.converter(valor);
            } catch (NumberFormatException e) {
                throw new ValorPositivoException();
            }
            lancaTaxaServico(membro, data, valorConvertido);
            confirmarMutacao();
            sucesso = true;
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
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

        return consultas.taxasServico(cadastro.todos(), id, dataInicial, dataFinal);
    }

    public double getTaxasServico(String empregado, String dataInicial, String dataFinal)
            throws IdentificacaoEmpregadoNulaException, EmpregadoNaoExisteException,
            EmpregadoNaoEhSindicalizadoException, DataInicialInvalidaException,
            DataFinalInvalidaException, DataInicialPosteriorDataFinalException {
        return getTaxasServico(parseId(empregado), dataInicial, dataFinal);
    }

    private LocalDate parseDataPagamento(String data) throws DataInvalidaException {
        try {
            return LocalDate.parse(data, DataUtil.FORMATO);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }
    }

    public double totalFolha(String data) throws DataInvalidaException {
        return new FolhaDePagamento().totalBruto(cadastro.todos().values(),
                parseDataPagamento(data));
    }

    public void rodaFolha(String data, String saida)
            throws DataInvalidaException, FileNotFoundException, SistemaEncerradoException {
        iniciarMutacao();
        boolean sucesso = false;
        try {
            new FolhaDePagamento().gerar(cadastro.todos().values(),
                    parseDataPagamento(data), saida);
            sucesso = true;
            historico.confirmarOperacao();
        } finally {
            if (!sucesso) {
                descartarUltimoUndo();
            }
        }
    }

    private final HistoricoSistema historico = new HistoricoSistema();
    private final PersistenciaXML persistencia = new PersistenciaXML();

    private EstadoSistema criarEstadoAtual() {
        return new EstadoSistema(cadastro.getIdGerado(), cadastro.todos(), agendasPagamento);
    }

    private void aplicarEstado(EstadoSistema estado) {
        cadastro.restaurar(estado.getIdGerado(), estado.getEmpregados());
        agendasPagamento = new LinkedHashSet<>(estado.getAgendasPagamento());
    }

    private void iniciarMutacao() throws SistemaEncerradoException {
        verificarDisponibilidade();
        salvarParaUndo();
    }

    private void confirmarMutacao() {
        try {
            salvarXML(ARQUIVO_XML);
        } catch (PersistenciaException e) {
            throw new FalhaPersistenciaSistemaException(ARQUIVO_XML, e);
        }
        confirmarOperacao();
    }

    private void salvarParaUndo() {
        historico.salvarParaUndo(criarEstadoAtual());
    }

    private void descartarUltimoUndo() {
        EstadoSistema estadoAnterior = historico.descartarUltimoUndo();
        if (estadoAnterior != null) {
            aplicarEstado(estadoAnterior);
        }
    }

    private void confirmarOperacao() {
        historico.confirmarOperacao();
    }

    public void undo() throws NadaParaDesfazerException, SistemaEncerradoException {
        verificarDisponibilidade();
        EstadoSistema estadoAtual = criarEstadoAtual();
        EstadoSistema estadoDesfeito = historico.undo(estadoAtual);
        aplicarEstado(estadoDesfeito);
        try {
            salvarXML(ARQUIVO_XML);
        } catch (PersistenciaException e) {
            aplicarEstado(estadoAtual);
            historico.cancelarUndo(estadoDesfeito);
            throw new FalhaPersistenciaSistemaException(ARQUIVO_XML, e);
        }
    }

    public void redo() throws NadaParaRefazerException, SistemaEncerradoException {
        verificarDisponibilidade();
        EstadoSistema estadoAtual = criarEstadoAtual();
        EstadoSistema estadoRefeito = historico.redo(estadoAtual);
        aplicarEstado(estadoRefeito);
        try {
            salvarXML(ARQUIVO_XML);
        } catch (PersistenciaException e) {
            aplicarEstado(estadoAtual);
            historico.cancelarRedo(estadoRefeito);
            throw new FalhaPersistenciaSistemaException(ARQUIVO_XML, e);
        }
    }

    public int getNumeroDeEmpregados() {
        return consultas.numeroDeEmpregados(cadastro.todos());
    }

    public int getEmpregadoPorNome(String nome, int indice)
        throws NomeNaoEncontradoException {

        return consultas.empregadoPorNome(cadastro.todos(), nome, indice);
    }

    public int getEmpregadoPorNome(String nome, String indice)
            throws NomeNaoEncontradoException {
        int indiceConvertido;
        try {
            indiceConvertido = Integer.parseInt(indice);
        } catch (NumberFormatException e) {
            throw new NomeNaoEncontradoException();
        }
        return getEmpregadoPorNome(nome, indiceConvertido);
    }

    private void salvarXML(String caminho) throws PersistenciaException {
        persistencia.salvar(caminho, criarEstadoAtual());
    }

    void carregarXML(String caminho) throws PersistenciaException {
        aplicarEstado(persistencia.carregar(caminho, criarEstadoAtual()));
    }

}
