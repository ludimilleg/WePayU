package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Facade {

    private Sistema sistema;

    public Facade() {
        sistema = new Sistema();
    }

    public void zerarSistema()  {
        sistema.zerarSistema();
    }

    public int criarEmpregado(String nome, String endereco, String tipo, String salario) throws NomeInvalidoException,
         EnderecoInvalidoException,
         TipoInvalidoException,
         SalarioNuloException,
         SalarioNaoNumericoException,
         SalarioInvalidoException,
         TipoNaoAplicavelException {

    if (salario.equals("")) {
        throw new SalarioNuloException();
    }

    double salarioConvertido;

    try {
        salarioConvertido = Double.parseDouble(salario.replace(",", "."));
    } catch (NumberFormatException e) {
        throw new SalarioNaoNumericoException();
    }

    return sistema.criarEmpregado(
        nome,
        endereco,
        tipo,
        salarioConvertido,
        null
    );
}

public Object getAtributoEmpregado(String emp, String atributo)
    throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNulaException, AtributoNaoExisteException, TipoNaoAplicavelException {

    if (emp.equals("")) {
        throw new IdentificacaoEmpregadoNulaException();
    }

    int id;

    try {
        id = Integer.parseInt(emp);
    } catch (NumberFormatException e) {
        throw new EmpregadoNaoExisteException();
    }

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
         TipoNaoAplicavelException {

    if (salario.equals("")) {
        throw new SalarioNuloException();
    }

    double salarioConvertido;

    try {
        salarioConvertido = Double.parseDouble(
            salario.replace(",", ".")
        );
    } catch (NumberFormatException e) {
        throw new SalarioNaoNumericoException();
    }

    if (comissao.equals("")) {
        throw new ComissaoNulaException();
    }

    double comissaoConvertida;

    try {
        comissaoConvertida = Double.parseDouble(
            comissao.replace(",", ".")
        );
    } catch (NumberFormatException e) {
        throw new ComissaoNaoNumericaException();
    }

    if (comissaoConvertida < 0) {
        throw new ComissaoNegativaException();
    }

    return sistema.criarEmpregado(
        nome,
        endereco,
        tipo,
        salarioConvertido,
        comissaoConvertida
    );
}

public void encerrarSistema() {
}
}