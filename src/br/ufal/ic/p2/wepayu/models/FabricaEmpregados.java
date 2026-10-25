package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;

public final class FabricaEmpregados {

    private FabricaEmpregados() {
    }

    public static Empregado criar(
            String tipo,
            int id,
            String nome,
            String endereco,
            boolean sindicalizado,
            double salario,
            Double comissao)
            throws TipoInvalidoException, TipoNaoAplicavelException {

        switch (tipo) {
            case "horista":
                return new Horista(id, nome, endereco, sindicalizado, salario);

            case "assalariado":
                return new Assalariado(id, nome, endereco, sindicalizado, salario);

            case "comissionado":
                if (comissao == null) {
                    throw new TipoNaoAplicavelException();
                }
                return new Comissionado(id, nome, endereco, sindicalizado, salario, comissao);

            default:
                throw new TipoInvalidoException();
        }
    }
}