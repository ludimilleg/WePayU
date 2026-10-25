package br.ufal.ic.p2.wepayu;

import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.models.Assalariado;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;

final class CadastroEmpregados {

    private final HashMap<Integer, Empregado> empregados = new HashMap<>();
    private int idGerado;

    int gerarId() {
        return ++idGerado;
    }

    void zerar() {
        idGerado = 0;
        empregados.clear();
    }

    int criar(String nome, String endereco, String tipo, double salario, Double comissao)
            throws NomeInvalidoException, EnderecoInvalidoException, TipoInvalidoException,
            SalarioInvalidoException, TipoNaoAplicavelException {
        if (nome == null || nome.isEmpty()) {
            throw new NomeInvalidoException();
        }
        if (endereco == null || endereco.isEmpty()) {
            throw new EnderecoInvalidoException();
        }
        if (!"horista".equals(tipo) && !"assalariado".equals(tipo)
                && !"comissionado".equals(tipo)) {
            throw new TipoInvalidoException();
        }
        if (!Double.isFinite(salario) || salario < 0) {
            throw new SalarioInvalidoException();
        }
        if ("comissionado".equals(tipo) != (comissao != null)) {
            throw new TipoNaoAplicavelException();
        }

        int id = gerarId();
        Empregado empregado;
        switch (tipo) {
            case "horista":
                empregado = new Horista(id, nome, endereco, false, salario);
                break;
            case "assalariado":
                empregado = new Assalariado(id, nome, endereco, false, salario);
                break;
            default:
                empregado = new Comissionado(id, nome, endereco, false, salario, comissao);
                break;
        }
        empregados.put(id, empregado);
        return id;
    }

    void remover(int id) throws EmpregadoNaoExisteException {
        if (empregados.remove(id) == null) {
            throw new EmpregadoNaoExisteException();
        }
    }

    Empregado obter(int id) {
        return empregados.get(id);
    }

    HashMap<Integer, Empregado> todos() {
        return empregados;
    }

    int getIdGerado() {
        return idGerado;
    }

    void restaurar(int id, Map<Integer, Empregado> empregadosRestaurados) {
        idGerado = id;
        empregados.clear();
        empregados.putAll(empregadosRestaurados);
    }
}
