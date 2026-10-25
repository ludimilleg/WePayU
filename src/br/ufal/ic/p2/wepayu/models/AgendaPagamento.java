package br.ufal.ic.p2.wepayu.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public final class AgendaPagamento {

    public static final String SEMANAL = "semanal 5";
    public static final String QUINZENAL = "semanal 2 5";
    public static final String MENSAL = "mensal $";

    private static final LocalDate PRIMEIRO_PAGAMENTO_QUINZENAL =
            LocalDate.of(2005, 1, 14);

    private AgendaPagamento() {
    }

    public static boolean disponivel(String descricao) {
        return SEMANAL.equals(descricao)
                || QUINZENAL.equals(descricao)
                || MENSAL.equals(descricao);
    }

    public static boolean dataDePagamento(String descricao, LocalDate data) {
        if (MENSAL.equals(descricao)) {
            return data.equals(ultimoDiaUtilDoMes(data));
        }
        if (SEMANAL.equals(descricao)) {
            return data.getDayOfWeek() == DayOfWeek.FRIDAY;
        }
        if (QUINZENAL.equals(descricao)) {
            long dias = ChronoUnit.DAYS.between(PRIMEIRO_PAGAMENTO_QUINZENAL, data);
            return dias >= 0 && dias % 14 == 0;
        }
        throw new IllegalArgumentException("Agenda de pagamento inválida.");
    }

    public static LocalDate inicioPeriodo(String descricao, LocalDate dataPagamento) {
        if (MENSAL.equals(descricao)) {
            return dataPagamento.withDayOfMonth(1);
        }
        if (SEMANAL.equals(descricao)) {
            return dataPagamento.minusDays(6);
        }
        if (QUINZENAL.equals(descricao)) {
            return dataPagamento.minusDays(13);
        }
        throw new IllegalArgumentException("Agenda de pagamento inválida.");
    }

    public static int semanasNoPeriodo(String descricao) {
        if (SEMANAL.equals(descricao)) {
            return 1;
        }
        if (QUINZENAL.equals(descricao)) {
            return 2;
        }
        if (MENSAL.equals(descricao)) {
            return 0;
        }
        throw new IllegalArgumentException("Agenda de pagamento inválida.");
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
