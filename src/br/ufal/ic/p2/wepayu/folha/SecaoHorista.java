package br.ufal.ic.p2.wepayu.folha;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.time.LocalDate;
import java.util.Locale;

class SecaoHoristas extends SecaoFolha {

    @Override
    String getTipo() {
        return "horista";
    }

    @Override
    String getTitulo() {
        return "HORISTAS";
    }

    @Override
    String getCabecalho() {
        return "Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo";
    }

    @Override
    String getSeparadorColunas() {
        return "==================================== ===== ===== ============= ========= =============== ======================================";
    }

    @Override
    int getQuantidadeColunasEspecificas() {
        return 2;
    }

    @Override
    double[] colunasEspecificas(Empregado empregado, LocalDate data) {
        return new double[] {
            empregado.getHorasNormais(data),
            empregado.getHorasExtras(data)
        };
    }

    @Override
    String formatar(String rotulo, double[] especificas,
                    double bruto, double descontos, double liquido) {
        return String.format(Locale.US, "%-36s %5.0f %5.0f %13s %9s %15s",
                rotulo, especificas[0], especificas[1],
                moeda(bruto), moeda(descontos), moeda(liquido));
    }
}