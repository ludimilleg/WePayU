package br.ufal.ic.p2.wepayu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.ufal.ic.p2.wepayu.exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.exception.NomeNaoEncontradoException;
import br.ufal.ic.p2.wepayu.exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.models.Empregado;

final class ConsultasEmpregados {

    Object getAtributo(Map<Integer, Empregado> empregados, int id, String atributo)
            throws EmpregadoNaoExisteException, TipoNaoAplicavelException,
            AtributoNaoExisteException, EmpregadoNaoRecebeEmBancoException,
            EmpregadoNaoEhSindicalizadoException, EmpregadoNaoEhComissionadoException {
        Empregado empregado = obter(empregados, id);
        switch (atributo) {
            case "nome":
                return empregado.getNome();
            case "endereco":
                return empregado.getEndereco();
            case "tipo":
                return empregado.getTipo();
            case "salario":
                return moeda(empregado.getSalario());
            case "comissao":
                if (!"comissionado".equals(empregado.getTipo())) {
                    throw new EmpregadoNaoEhComissionadoException();
                }
                return moeda(empregado.getComissao());
            case "sindicalizado":
                return empregado.isSindicalizado();
            case "metodoPagamento":
                return empregado.getMetodoPagamento();
            case "agendaPagamento":
                return empregado.getAgendaPagamento();
            case "banco":
                validarRecebimentoEmBanco(empregado);
                return empregado.getBanco();
            case "agencia":
                validarRecebimentoEmBanco(empregado);
                return empregado.getAgencia();
            case "contaCorrente":
                validarRecebimentoEmBanco(empregado);
                return empregado.getContaCorrente();
            case "idSindicato":
                validarSindicalizado(empregado);
                return empregado.getIdSindicato();
            case "taxaSindical":
                validarSindicalizado(empregado);
                return moeda(empregado.getTaxaSindical());
            default:
                throw new AtributoNaoExisteException();
        }
    }

    double horasNormais(Map<Integer, Empregado> empregados, int id,
                        String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEhHoristaException,
            DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosteriorDataFinalException {
        Empregado empregado = obter(empregados, id);
        LocalDate[] periodo = parsePeriodo(dataInicial, dataFinal);
        return empregado.getHorasNormaisTrabalhadas(periodo[0], periodo[1]);
    }

    double horasExtras(Map<Integer, Empregado> empregados, int id,
                       String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEhHoristaException,
            DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosteriorDataFinalException {
        Empregado empregado = obter(empregados, id);
        LocalDate[] periodo = parsePeriodo(dataInicial, dataFinal);
        return empregado.getHorasExtrasTrabalhadas(periodo[0], periodo[1]);
    }

    double vendasRealizadas(Map<Integer, Empregado> empregados, int id,
                            String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEhComissionadoException,
            DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosteriorDataFinalException {
        Empregado empregado = obter(empregados, id);
        LocalDate[] periodo = parsePeriodo(dataInicial, dataFinal);
        return empregado.getVendasRealizadasNoPeriodo(periodo[0], periodo[1]);
    }

    double taxasServico(Map<Integer, Empregado> empregados, int id,
                        String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEhSindicalizadoException,
            DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosteriorDataFinalException {
        Empregado empregado = obter(empregados, id);
        validarSindicalizado(empregado);
        LocalDate[] periodo = parsePeriodo(dataInicial, dataFinal);
        double total = 0;
        DateTimeFormatter formato = DataUtil.FORMATO;
        for (Map.Entry<String, Double> taxa : empregado.getTaxasServico().entrySet()) {
            LocalDate dataTaxa = LocalDate.parse(taxa.getKey(), formato);
            if (!dataTaxa.isBefore(periodo[0]) && dataTaxa.isBefore(periodo[1])) {
                total += taxa.getValue();
            }
        }
        return total;
    }

    int numeroDeEmpregados(Map<Integer, Empregado> empregados) {
        return empregados.size();
    }

    int empregadoPorNome(Map<Integer, Empregado> empregados, String nome, int indice)
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
            throw new NomeNaoEncontradoException();
        }
        return encontrados.get(indice - 1).getId();
    }

    private Empregado obter(Map<Integer, Empregado> empregados, int id)
            throws EmpregadoNaoExisteException {
        Empregado empregado = empregados.get(id);
        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }
        return empregado;
    }

    private void validarRecebimentoEmBanco(Empregado empregado)
            throws EmpregadoNaoRecebeEmBancoException {
        if (!"banco".equals(empregado.getMetodoPagamento())) {
            throw new EmpregadoNaoRecebeEmBancoException();
        }
    }

    private void validarSindicalizado(Empregado empregado)
            throws EmpregadoNaoEhSindicalizadoException {
        if (!empregado.isSindicalizado()) {
            throw new EmpregadoNaoEhSindicalizadoException();
        }
    }

    private LocalDate[] parsePeriodo(String dataInicial, String dataFinal)
            throws DataInicialInvalidaException, DataFinalInvalidaException,
            DataInicialPosteriorDataFinalException {
        LocalDate inicial;
        try {
            inicial = LocalDate.parse(dataInicial, DataUtil.FORMATO);
        } catch (DateTimeParseException e) {
            throw new DataInicialInvalidaException();
        }
        LocalDate finalDate;
        try {
            finalDate = LocalDate.parse(dataFinal, DataUtil.FORMATO);
        } catch (DateTimeParseException e) {
            throw new DataFinalInvalidaException();
        }
        if (inicial.isAfter(finalDate)) {
            throw new DataInicialPosteriorDataFinalException();
        }
        return new LocalDate[] {inicial, finalDate};
    }

    private String moeda(double valor) {
        return String.format(Locale.US, "%.2f", valor).replace(".", ",");
    }
}
