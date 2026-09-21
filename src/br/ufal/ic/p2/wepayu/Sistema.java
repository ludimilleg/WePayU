package br.ufal.ic.p2.wepayu;
import java.util.HashMap;
import java.util.Locale;

import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
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
}