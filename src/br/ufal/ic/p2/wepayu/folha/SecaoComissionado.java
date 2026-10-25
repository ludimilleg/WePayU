package br.ufal.ic.p2.wepayu.folha;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.time.LocalDate;

class SecaoComissionados extends SecaoFolha {

    @Override
    String getTipo() {
        return "comissionado";
    }

    @Override
    String getTitulo() {
        return "COMISSIONADOS";
    }

    @Override
    String getCabecalho() {
        return "Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo";
    }

    @Override
    String getSeparadorColunas() {
        return "===================== ======== ======== ======== ============= ========= =============== ======================================";
    }

    @Override
    int getQuantidadeColunasEspecificas() {
        return 3;
    }

    @Override
    double[] colunasEspecificas(Empregado empregado, LocalDate data) {
        return new double[] {
            empregado.getSalarioFixoNoPeriodo(data),
            empregado.getVendasNoPeriodo(data),
            empregado.getComissaoNoPeriodo(data)
        };
    }

    @Override
    String formatar(String rotulo, double[] especificas,
                    double bruto, double descontos, double liquido) {
        return String.format("%-21s %8s %8s %8s %13s %9s %15s",
                rotulo,
                moeda(especificas[0]), moeda(especificas[1]), moeda(especificas[2]),
                moeda(bruto), moeda(descontos), moeda(liquido));
    }
}