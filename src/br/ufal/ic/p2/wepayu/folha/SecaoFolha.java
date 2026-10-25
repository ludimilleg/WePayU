package br.ufal.ic.p2.wepayu.folha;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

abstract class SecaoFolha {

    private static final int LARGURA = 127;
    private static final int MARGEM_TITULO = 21;

    private double totalBruto;

    abstract String getTipo();

    abstract String getTitulo();

    abstract String getCabecalho();

    abstract String getSeparadorColunas();

    abstract int getQuantidadeColunasEspecificas();

    abstract double[] colunasEspecificas(Empregado empregado, LocalDate data);

    abstract String formatar(String rotulo, double[] especificas,
                             double bruto, double descontos, double liquido);

    double getTotalBruto() {
        return totalBruto;
    }

    static String moeda(double valor) {
        return String.format(Locale.US, "%.2f", valor).replace(".", ",");
    }

    void escrever(PrintWriter saida, List<Empregado> empregados, LocalDate data) {

        saida.println("=".repeat(LARGURA));
        saida.println(montarTitulo());
        saida.println("=".repeat(LARGURA));
        saida.println(getCabecalho());
        saida.println(getSeparadorColunas());

        double[] totaisEspecificos = new double[getQuantidadeColunasEspecificas()];
        double bruto = 0;
        double descontos = 0;
        double liquido = 0;

        for (Empregado empregado : empregados) {

            if (!getTipo().equals(empregado.getTipo()) || !empregado.recebeNaData(data)) {
                continue;
            }

            double[] especificas = colunasEspecificas(empregado, data);
            double empregadoBruto = empregado.calculaPagamento(data);
            double empregadoDescontos = empregado.getDescontos(data);
            double empregadoLiquido = empregado.getSalarioLiquido(data);

            for (int i = 0; i < totaisEspecificos.length; i++) {
                totaisEspecificos[i] += especificas[i];
            }

            bruto += empregadoBruto;
            descontos += empregadoDescontos;
            liquido += empregadoLiquido;

            saida.println(formatar(empregado.getNome(), especificas,
                    empregadoBruto, empregadoDescontos, empregadoLiquido)
                    + " " + empregado.getMetodoPagamentoFormatado());
        }

        saida.println();
        saida.println(formatar("TOTAL " + getTitulo(), totaisEspecificos, bruto, descontos, liquido));
        saida.println();

        totalBruto = bruto;
    }

    private String montarTitulo() {
        String esquerda = "=".repeat(MARGEM_TITULO) + " ";
        String direita = " " + "=".repeat(LARGURA - esquerda.length() - getTitulo().length() - 1);
        return esquerda + getTitulo() + direita;
    }
}