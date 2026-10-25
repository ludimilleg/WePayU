package br.ufal.ic.p2.wepayu.folha;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class FolhaDePagamento {

    public double totalBruto(Collection<Empregado> empregados, LocalDate data) {

        double total = 0;

        for (Empregado empregado : empregados) {
            total += empregado.calculaPagamento(data);
        }

        return total;
    }

    public void gerar(Collection<Empregado> empregados, LocalDate data, String arquivo)
            throws FileNotFoundException {

        List<Empregado> ordenados = new ArrayList<>(empregados);
        ordenados.sort(Comparator.comparing(Empregado::getNome));

        List<SecaoFolha> secoes = List.of(
            new SecaoHoristas(),
            new SecaoAssalariados(),
            new SecaoComissionados()
        );

        try (PrintWriter saida = new PrintWriter(arquivo)) {

            String cabecalho = "FOLHA DE PAGAMENTO DO DIA "
                    + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            saida.println(cabecalho);
            saida.println("=".repeat(cabecalho.length()));
            saida.println();

            double total = 0;

            for (SecaoFolha secao : secoes) {
                secao.escrever(saida, ordenados, data);
                total += secao.getTotalBruto();
            }

            saida.println("TOTAL FOLHA: " + SecaoFolha.moeda(total));
        }
    }
}