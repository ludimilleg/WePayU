package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.AgendaPagamentoIndisponivelException;
import br.ufal.ic.p2.wepayu.Exception.BancoNuloException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.Exception.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.Exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNuloException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNegativaException;
import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.ValorNaoBooleanoException;
import br.ufal.ic.p2.wepayu.models.Assalariado;
import br.ufal.ic.p2.wepayu.models.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;

import java.util.Map;
import java.util.Set;

final class AlteradorEmpregado {
    private final Map<Integer, Empregado> empregados;
    private final Set<String> agendasPagamento;

    AlteradorEmpregado(Map<Integer, Empregado> empregados, Set<String> agendasPagamento) {
        this.empregados = empregados;
        this.agendasPagamento = agendasPagamento;
    }

    void alterar(int id, AlteracaoEmpregado alteracao)
            throws EmpregadoNaoExisteException, IdentificacaoSindicatoJaExisteException,
            NomeInvalidoException, EnderecoInvalidoException, SalarioNuloException,
            SalarioNaoNumericoException, SalarioInvalidoException, TipoInvalidoException,
            ComissaoNulaException, ComissaoNaoNumericaException, ComissaoNegativaException,
            EmpregadoNaoEhComissionadoException, MetodoPagamentoInvalidoException,
            BancoNuloException, AgenciaNulaException, ContaCorrenteNulaException,
            ValorNaoBooleanoException, IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException, TaxaSindicalNaoNumericaException,
            TaxaSindicalNegativaException, AtributoNaoExisteException,
            AgendaPagamentoIndisponivelException {
        Empregado empregado = empregados.get(id);
        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }

        switch (alteracao.getAtributo()) {
            case "nome":
                alterarNome(empregado, alteracao.getValor());
                break;
            case "endereco":
                alterarEndereco(empregado, alteracao.getValor());
                break;
            case "salario":
                alterarSalario(empregado, alteracao.getValor());
                break;
            case "tipo":
                alterarTipo(id, empregado, alteracao);
                break;
            case "comissao":
                alterarComissao(empregado, alteracao.getValor());
                break;
            case "metodoPagamento":
                alterarMetodoPagamento(empregado, alteracao);
                break;
            case "agendaPagamento":
                String agenda = alteracao.getValorAuxiliar() == null
                        ? alteracao.getValor() : alteracao.getValorAuxiliar();
                alterarAgendaPagamento(empregado, agenda);
                break;
            case "sindicalizado":
                alterarSindicalizacao(empregado, alteracao);
                break;
            default:
                throw new AtributoNaoExisteException();
        }
    }

    private void alterarAgendaPagamento(Empregado empregado, String descricao)
            throws AgendaPagamentoIndisponivelException {
        if (!AgendaPagamento.descricaoValida(descricao)
                || !agendasPagamento.contains(AgendaPagamento.normalizar(descricao))) {
            throw new AgendaPagamentoIndisponivelException();
        }
        empregado.setAgendaPagamento(AgendaPagamento.normalizar(descricao));
    }

    private void alterarNome(Empregado empregado, String nome) throws NomeInvalidoException {
        if (nome == null || nome.isEmpty()) {
            throw new NomeInvalidoException();
        }
        empregado.setNome(nome);
    }

    private void alterarEndereco(Empregado empregado, String endereco)
            throws EnderecoInvalidoException {
        if (endereco == null || endereco.isEmpty()) {
            throw new EnderecoInvalidoException();
        }
        empregado.setEndereco(endereco);
    }

    private void alterarSalario(Empregado empregado, String valor)
            throws SalarioNuloException, SalarioNaoNumericoException,
            SalarioInvalidoException {
        if (valor == null || valor.isEmpty()) {
            throw new SalarioNuloException();
        }
        double salario;
        try {
            salario = ConversorDecimal.converter(valor);
        } catch (NumberFormatException e) {
            throw new SalarioNaoNumericoException();
        }
        if (!Double.isFinite(salario) || salario < 0) {
            throw new SalarioInvalidoException();
        }
        empregado.setSalario(salario);
    }

    private void alterarTipo(int id, Empregado empregado, AlteracaoEmpregado alteracao)
            throws TipoInvalidoException, ComissaoNulaException,
            ComissaoNaoNumericaException, ComissaoNegativaException,
            SalarioNuloException, SalarioNaoNumericoException, SalarioInvalidoException {
        String tipo = alteracao.getValor();
        if (!tipo.equals("horista") && !tipo.equals("assalariado")
                && !tipo.equals("comissionado")) {
            throw new TipoInvalidoException();
        }

        double salario = empregado.getSalario();
        if (tipo.equals("horista")) {
            String salarioNovo = alteracao.getSalario();
            if (salarioNovo == null || salarioNovo.isEmpty()) {
                salarioNovo = alteracao.getComissao();
            }
            if (salarioNovo != null && !salarioNovo.isEmpty()) {
                salario = parseSalario(salarioNovo);
            }
        }

        Empregado novoEmpregado;
        if (tipo.equals("horista")) {
            novoEmpregado = new Horista(id, empregado.getNome(), empregado.getEndereco(),
                    empregado.isSindicalizado(), salario);
        } else if (tipo.equals("assalariado")) {
            novoEmpregado = new Assalariado(id, empregado.getNome(), empregado.getEndereco(),
                    empregado.isSindicalizado(), salario);
        } else {
            double comissao = parseComissao(alteracao.getComissao());
            novoEmpregado = new Comissionado(id, empregado.getNome(), empregado.getEndereco(),
                    empregado.isSindicalizado(), salario, comissao);
        }
        empregado.copiarCamposComuns(novoEmpregado);
        empregados.put(id, novoEmpregado);
    }

    private double parseSalario(String valor)
            throws SalarioNuloException, SalarioNaoNumericoException, SalarioInvalidoException {
        if (valor == null || valor.isEmpty()) {
            throw new SalarioNuloException();
        }
        double salario;
        try {
            salario = ConversorDecimal.converter(valor);
        } catch (NumberFormatException e) {
            throw new SalarioNaoNumericoException();
        }
        if (!Double.isFinite(salario) || salario < 0) {
            throw new SalarioInvalidoException();
        }
        return salario;
    }

    private void alterarComissao(Empregado empregado, String valor)
            throws ComissaoNulaException, ComissaoNaoNumericaException,
            ComissaoNegativaException, EmpregadoNaoEhComissionadoException {
        empregado.alterarComissao(parseComissao(valor));
    }

    private double parseComissao(String valor)
            throws ComissaoNulaException, ComissaoNaoNumericaException,
            ComissaoNegativaException {
        if (valor == null || valor.isEmpty()) {
            throw new ComissaoNulaException();
        }
        double comissao;
        try {
            comissao = ConversorDecimal.converter(valor);
        } catch (NumberFormatException e) {
            throw new ComissaoNaoNumericaException();
        }
        if (!Double.isFinite(comissao) || comissao < 0) {
            throw new ComissaoNegativaException();
        }
        return comissao;
    }

    private void alterarMetodoPagamento(Empregado empregado, AlteracaoEmpregado alteracao)
            throws MetodoPagamentoInvalidoException, BancoNuloException,
            AgenciaNulaException, ContaCorrenteNulaException {
        String metodo = alteracao.getValorAuxiliar() != null
                ? alteracao.getValorAuxiliar() : alteracao.getValor();
        if (!"emMaos".equals(metodo) && !"correios".equals(metodo)
                && !"banco".equals(metodo)) {
            throw new MetodoPagamentoInvalidoException();
        }

        if (metodo.equals("banco")) {
            if (isVazio(alteracao.getBanco())) {
                throw new BancoNuloException();
            }
            if (isVazio(alteracao.getAgencia())) {
                throw new AgenciaNulaException();
            }
            if (isVazio(alteracao.getContaCorrente())) {
                throw new ContaCorrenteNulaException();
            }
            empregado.setBanco(alteracao.getBanco());
            empregado.setAgencia(alteracao.getAgencia());
            empregado.setContaCorrente(alteracao.getContaCorrente());
        }
        empregado.setMetodoPagamento(metodo);
    }

    private void alterarSindicalizacao(Empregado empregado, AlteracaoEmpregado alteracao)
            throws ValorNaoBooleanoException, IdentificacaoSindicatoNulaException,
            TaxaSindicalNulaException, TaxaSindicalNaoNumericaException,
            TaxaSindicalNegativaException, IdentificacaoSindicatoJaExisteException {
        String valor = alteracao.getValor();
        if (!"true".equals(valor) && !"false".equals(valor)) {
            throw new ValorNaoBooleanoException();
        }
        if (valor.equals("false")) {
            empregado.setSindicalizado(false);
            empregado.setIdSindicato(null);
            empregado.setTaxaSindical(0);
            return;
        }

        String idSindicato = alteracao.getIdSindicato();
        if (isVazio(idSindicato)) {
            throw new IdentificacaoSindicatoNulaException();
        }
        String taxaTexto = alteracao.getTaxaSindical();
        if (isVazio(taxaTexto)) {
            throw new TaxaSindicalNulaException();
        }
        double taxa;
        try {
            taxa = ConversorDecimal.converter(taxaTexto);
        } catch (NumberFormatException e) {
            throw new TaxaSindicalNaoNumericaException();
        }
        if (!Double.isFinite(taxa) || taxa < 0) {
            throw new TaxaSindicalNegativaException();
        }
        for (Empregado outro : empregados.values()) {
            if (outro != empregado && outro.isSindicalizado()
                    && idSindicato.equals(outro.getIdSindicato())) {
                throw new IdentificacaoSindicatoJaExisteException();
            }
        }
        empregado.setSindicalizado(true);
        empregado.setIdSindicato(idSindicato);
        empregado.setTaxaSindical(taxa);
    }

    private boolean isVazio(String valor) {
        return valor == null || valor.isEmpty();
    }
}
