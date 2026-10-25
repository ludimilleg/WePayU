package br.ufal.ic.p2.wepayu.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EstadoSistema {

    private final int idGerado;
    private final Map<Integer, Empregado> empregados;

    public EstadoSistema(int idGerado, Map<Integer, Empregado> empregados) {
        this.idGerado = idGerado;
        this.empregados = empregados;
    }

    public int getIdGerado() {
        return idGerado;
    }

    public Map<Integer, Empregado> getEmpregados() {
        return Collections.unmodifiableMap(empregados);
    }

    public EstadoSistema copiar() {

        Map<Integer, Empregado> copia = new HashMap<>();

        for (Map.Entry<Integer, Empregado> entrada : empregados.entrySet()) {
            copia.put(entrada.getKey(), entrada.getValue().copiar());
        }

        return new EstadoSistema(idGerado, copia);
    }
}