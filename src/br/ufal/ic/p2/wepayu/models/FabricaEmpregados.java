package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoNaoAplicavelException;

public final class FabricaEmpregados {

    private FabricaEmpregados() {
    }

    public static Empregado criar(
            String tipo,
            int id,
            String nome,
            String endereco,
            double salario,
            Double comissao)
            throws TipoInvalidoException, TipoNaoAplicavelException {

        switch (tipo) {
            case "horista":
                return new Horista(id, nome, endereco, false, salario);

            case "assalariado":
                return new Assalariado(id, nome, endereco, false, salario);

            case "comissionado":
                if (comissao == null) {
                    throw new TipoNaoAplicavelException();
                }
                return new Comissionado(id, nome, endereco, false, salario, comissao);

            default:
                throw new TipoInvalidoException();
        }
    }
}