package br.ufal.ic.p2.wepayu.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;

public class EstadoSistema {

    private final int idGerado;
    private final Map<Integer, Empregado> empregados;
    private final Set<String> agendasPagamento;

    public EstadoSistema(int idGerado, Map<Integer, Empregado> empregados) {
        this(idGerado, empregados, AgendaPagamento.agendasPadrao());
    }

    public EstadoSistema(int idGerado, Map<Integer, Empregado> empregados,
                         Set<String> agendasPagamento) {
        this.idGerado = idGerado;
        this.empregados = copiarEmpregados(empregados);
        this.agendasPagamento = new LinkedHashSet<>(agendasPagamento);
    }

    public int getIdGerado() {
        return idGerado;
    }

    public Map<Integer, Empregado> getEmpregados() {
        return Collections.unmodifiableMap(copiarEmpregados(empregados));
    }

    public Set<String> getAgendasPagamento() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(agendasPagamento));
    }

    public EstadoSistema copiar() {
        return new EstadoSistema(idGerado, empregados, agendasPagamento);
    }

    private Map<Integer, Empregado> copiarEmpregados(Map<Integer, Empregado> originais) {
        Map<Integer, Empregado> copia = new HashMap<>();

        for (Map.Entry<Integer, Empregado> entrada : originais.entrySet()) {
            copia.put(entrada.getKey(), entrada.getValue().copiar());
        }

        return copia;
    }
}