package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.AgendaPagamentoInvalidaException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class AgendaPagamento {

    public static final String SEMANAL = "semanal 5";
    public static final String QUINZENAL = "semanal 2 5";
    public static final String MENSAL = "mensal $";

    private static final Set<String> PADROES = Collections.unmodifiableSet(
            new LinkedHashSet<>(Arrays.asList(SEMANAL, QUINZENAL, MENSAL)));

    private AgendaPagamento() {
    }

    public static Set<String> agendasPadrao() {
        return PADROES;
    }

    public static boolean disponivel(String descricao) {
        return PADROES.contains(descricao);
    }

    public static boolean descricaoValida(String descricao) {
        if (descricao == null) {
            return false;
        }
        String[] partes = descricao.trim().split("\\s+");
        try {
            if (partes.length == 2 && "mensal".equals(partes[0])) {
                return "$".equals(partes[1])
                        || numeroNoIntervalo(partes[1], 1, 28);
            }
            if (partes.length == 2 && "semanal".equals(partes[0])) {
                return numeroNoIntervalo(partes[1], 1, 7);
            }
            if (partes.length == 3 && "semanal".equals(partes[0])) {
                return numeroNoIntervalo(partes[1], 1, 52)
                        && numeroNoIntervalo(partes[2], 1, 7);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    public static String normalizar(String descricao) {
        if (!descricaoValida(descricao)) {
            throw new AgendaPagamentoInvalidaException();
        }
        String[] partes = descricao.trim().split("\\s+");
        if ("mensal".equals(partes[0]) && "$".equals(partes[1])) {
            return "mensal $";
        }
        if (partes.length == 2) {
            return partes[0] + " " + Integer.parseInt(partes[1]);
        }
        return partes[0] + " " + Integer.parseInt(partes[1])
                + " " + Integer.parseInt(partes[2]);
    }

    public static boolean dataDePagamento(String descricao, LocalDate data) {
        String[] partes = partes(descricao);
        if ("mensal".equals(partes[0])) {
            if ("$".equals(partes[1])) {
                return data.equals(ultimoDiaUtilDoMes(data));
            }
            return data.getDayOfMonth() == Integer.parseInt(partes[1]);
        }

        int intervalo;
        int diaDaSemana;
        if (partes.length == 2) {
            intervalo = 1;
            diaDaSemana = Integer.parseInt(partes[1]);
        } else {
            intervalo = Integer.parseInt(partes[1]);
            diaDaSemana = Integer.parseInt(partes[2]);
        }
        int semana = data.get(WeekFields.ISO.weekOfWeekBasedYear());
        return data.getDayOfWeek().getValue() == diaDaSemana
                && semana % intervalo == 0;
    }

    public static LocalDate inicioPeriodo(String descricao, LocalDate dataPagamento) {
        String[] partes = partes(descricao);
        if ("mensal".equals(partes[0])) {
            return dataPagamento.withDayOfMonth(1);
        }
        int semanas = partes.length == 2 ? 1 : Integer.parseInt(partes[1]);
        return dataPagamento.minusDays(semanas * 7L - 1);
    }

    public static int semanasNoPeriodo(String descricao) {
        String[] partes = partes(descricao);
        if ("mensal".equals(partes[0])) {
            return 0;
        }
        return partes.length == 2 ? 1 : Integer.parseInt(partes[1]);
    }

    public static boolean ehMensal(String descricao) {
        return partes(descricao)[0].equals("mensal");
    }

    private static String[] partes(String descricao) {
        return normalizar(descricao).split(" ");
    }

    private static boolean numeroNoIntervalo(String valor, int minimo, int maximo) {
        if (!valor.matches("\\d+")) {
            return false;
        }
        int numero = Integer.parseInt(valor);
        return numero >= minimo && numero <= maximo;
    }

    private static LocalDate ultimoDiaUtilDoMes(LocalDate data) {
        LocalDate ultimoDia = data.with(TemporalAdjusters.lastDayOfMonth());
        while (ultimoDia.getDayOfWeek() == DayOfWeek.SATURDAY
                || ultimoDia.getDayOfWeek() == DayOfWeek.SUNDAY) {
            ultimoDia = ultimoDia.minusDays(1);
        }
        return ultimoDia;
    }
}
