package br.ufal.ic.p2.wepayu.folha;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.time.LocalDate;

class SecaoAssalariados extends SecaoFolha {

    @Override
    String getTipo() {
        return "assalariado";
    }

    @Override
    String getTitulo() {
        return "ASSALARIADOS";
    }

    @Override
    String getCabecalho() {
        return "Nome                                             Salario Bruto Descontos Salario Liquido Metodo";
    }

    @Override
    String getSeparadorColunas() {
        return "================================================ ============= ========= =============== ======================================";
    }

    @Override
    int getQuantidadeColunasEspecificas() {
        return 0;
    }

    @Override
    double[] colunasEspecificas(Empregado empregado, LocalDate data) {
        return new double[0];
    }

    @Override
    String formatar(String rotulo, double[] especificas,
                    double bruto, double descontos, double liquido) {
        return String.format("%-48s %13s %9s %15s",
                rotulo, moeda(bruto), moeda(descontos), moeda(liquido));
    }
}